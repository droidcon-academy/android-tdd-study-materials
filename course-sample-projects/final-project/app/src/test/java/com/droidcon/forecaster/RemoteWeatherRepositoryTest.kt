package com.droidcon.forecaster

import com.droidcon.forecaster.data.WeatherData
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest
import retrofit2.Retrofit

class RemoteWeatherRepositoryTest : WeatherRepositoryContractTest() {

    private val mockWebServer = MockWebServer()
    private val contentType = "application/json".toMediaType()
    private val json = Json { ignoreUnknownKeys = true }
    private val retrofit = Retrofit.Builder()
        .baseUrl(mockWebServer.url("/"))
        .addConverterFactory(json.asConverterFactory(contentType))
        .build()
    private val weatherApi = retrofit.create(WeatherApi::class.java)

    override fun weatherRepositoryWith(
        location: String,
        weatherAtLocation: WeatherData?
    ): WeatherRepository {
        val weatherForLocation = mapOf(location to weatherAtLocation)
        val dispatcher = CustomDispatcher(weatherForLocation)
        mockWebServer.dispatcher = dispatcher
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
            if (weatherForLocation.containsKey(query)) {
                val weatherAtLocation = weatherForLocation[query]
                    ?: return weatherForLocationUnavailableResponse()
                return weatherResponse(weatherAtLocation)
            }
            return backendUnavailableResponse()
        }

        private fun weatherResponse(weatherAtLocation: WeatherData) = MockResponse().setBody(
            """{
              "location": {
                "name": "${weatherAtLocation.city}",
                "country": "${weatherAtLocation.country}",
                "region": "${weatherAtLocation.region}"
              },
              "current": {
                "temperature": ${weatherAtLocation.temperature},
                "weather_icons": ["${weatherAtLocation.iconUrl}"],
                "weather_descriptions": ["${weatherAtLocation.description}"],
                "feelslike": ${weatherAtLocation.feelsLike}
              }
            }"""
        )

        private fun weatherForLocationUnavailableResponse(): MockResponse {
            return MockResponse()
                .setResponseCode(404)
                .setBody("""{"error":"No data found for location"}""")
        }

        private fun backendUnavailableResponse() = MockResponse()
            .setResponseCode(400)
            .setBody("""{"error":"backend unavailable"}""")
    }
}