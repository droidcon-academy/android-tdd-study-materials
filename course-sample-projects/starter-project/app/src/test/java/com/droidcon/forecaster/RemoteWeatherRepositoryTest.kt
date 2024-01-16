package com.droidcon.forecaster

import com.droidcon.forecaster.data.WeatherData
import com.droidcon.forecaster.data.WeatherResult
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Query

class RemoteWeatherRepositoryTest : WeatherRepositoryContractTest() {

    interface WeatherApi {

        @GET("/current?access_key=somekey")
        suspend fun fetchWeatherForLocation(
            @Query("query") query: String
        ): WeatherResponse
    }

    @Serializable
    data class WeatherResponse(
        val location: Location,
        val current: Current
    ) {

        @Serializable
        data class Location(
            val name: String,
            val country: String,
            val region: String
        )

        @Serializable
        data class Current(
            val temperature: Int,
            @SerialName("weather_icons") val icons: List<String>,
            @SerialName("weather_descriptions") val descriptions: List<String>,
            @SerialName("feelslike") val feelsLike: Int
        )
    }

    class RemoteWeatherRepository(
        private val weatherApi: WeatherApi
    ) : WeatherRepository {

        override suspend fun loadWeatherFor(location: String): WeatherResult {
            return try {
                val response = weatherApi.fetchWeatherForLocation(location)
                val weatherData = response.toWeatherData()
                WeatherResult.Loaded(weatherData)
            } catch (httpException: HttpException) {
                if (httpException.code() == 404) {
                    WeatherResult.Error
                } else if (httpException.code() == 400) {
                    WeatherResult.Unavailable
                } else {
                    throw httpException
                }
            }
        }

        private fun WeatherResponse.toWeatherData() = WeatherData(
            city = location.name,
            country = location.country,
            region = location.region,
            temperature = current.temperature,
            iconUrl = current.icons.first(),
            description = current.descriptions.joinToString(),
            feelsLike = current.feelsLike
        )
    }

    private val mockServer = MockWebServer()
    private val json = Json { ignoreUnknownKeys = true }
    private val contentType = "application/json".toMediaType()
    private val retrofit = Retrofit.Builder()
        .baseUrl(mockServer.url("/"))
        .addConverterFactory(json.asConverterFactory(contentType))
        .build()
    private val weatherApi = retrofit.create(WeatherApi::class.java)

    override fun weatherRepositoryWith(
        location: String,
        weatherAtLocation: WeatherData?
    ): WeatherRepository {
        val weatherForLocation = mapOf(location to weatherAtLocation)
        val dispatcher = CustomDispatcher(weatherForLocation)
        mockServer.dispatcher = dispatcher
        return RemoteWeatherRepository(weatherApi)
    }

    override fun weatherRepositoryWithout(
        location: String,
        weatherAtLocation: WeatherData?
    ): WeatherRepository {
        return weatherRepositoryWith("otherThan$location", weatherAtLocation)
    }

    private class CustomDispatcher(
        private val weatherForLocation: Map<String, WeatherData?>
    ) : Dispatcher() {

        override fun dispatch(request: RecordedRequest): MockResponse {
            val query = request.requestUrl?.queryParameter("query") ?: ""
            if (!weatherForLocation.containsKey(query)) {
                return MockResponse()
                    .setResponseCode(400)
                    .setBody("""{"error": "backend unavailable"}""")
            }
            val weatherAtLocation = weatherForLocation[query]
                ?: return MockResponse()
                    .setResponseCode(404)
                    .setBody("""{"error": "No data found for $query"}""")
            return MockResponse()
                .setResponseCode(200)
                .setBody(
                    """{
                    "location": {
                        "name": "${weatherAtLocation.city}",
                        "country": "${weatherAtLocation.country}",
                        "region": "${weatherAtLocation.region}"
                    },
                    "current": {
                        "temperature": "${weatherAtLocation.temperature}",
                        "weather_icons": ["${weatherAtLocation.iconUrl}"],
                        "weather_descriptions": ["${weatherAtLocation.description}"],
                        "feelslike": "${weatherAtLocation.feelsLike}"
                    }
                }"""
                )
        }
    }
}