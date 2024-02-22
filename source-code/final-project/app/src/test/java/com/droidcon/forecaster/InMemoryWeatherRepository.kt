package com.droidcon.forecaster

import com.droidcon.forecaster.data.WeatherData
import com.droidcon.forecaster.data.WeatherResult

class InMemoryWeatherRepository(
    private val weatherForLocation: Map<String, WeatherData?>
) : WeatherRepository {

    override suspend fun loadWeatherFor(location: String): WeatherResult {
        if (weatherForLocation.containsKey(location)) {
            val result = weatherForLocation[location]
            return if (result == null) WeatherResult.Error else WeatherResult.Loaded(result)
        }
        return WeatherResult.Unavailable
    }
}