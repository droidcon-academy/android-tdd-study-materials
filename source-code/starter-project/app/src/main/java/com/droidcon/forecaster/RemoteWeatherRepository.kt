package com.droidcon.forecaster

import com.droidcon.forecaster.data.WeatherResult
import com.droidcon.forecaster.data.toResult
import com.droidcon.forecaster.data.toWeatherData
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
            httpException.toResult()
        }
    }
}