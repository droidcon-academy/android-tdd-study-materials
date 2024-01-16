package com.droidcon.forecaster

import com.droidcon.forecaster.data.WeatherData
import com.droidcon.forecaster.data.WeatherResponse
import com.droidcon.forecaster.data.WeatherResult
import retrofit2.HttpException

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