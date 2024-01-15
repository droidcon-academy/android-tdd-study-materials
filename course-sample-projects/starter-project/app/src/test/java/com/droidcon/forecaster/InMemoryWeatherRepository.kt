package com.droidcon.forecaster

import com.droidcon.forecaster.data.WeatherData
import com.droidcon.forecaster.data.WeatherResult

class InMemoryWeatherRepository(
    private val weatherForLocation: Map<String, WeatherData?>
) : WeatherRepository {

    override fun loadWeatherFor(location: String): WeatherResult {
        return if (weatherForLocation.containsKey(location)) {
            val weatherData = weatherForLocation[location]
            if (weatherData == null) WeatherResult.Error else WeatherResult.Loaded(weatherData)
        } else {
            WeatherResult.Unavailable
        }
    }
}