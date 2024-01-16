package com.droidcon.forecaster

import com.droidcon.forecaster.data.WeatherResult

interface WeatherRepository {
    suspend fun loadWeatherFor(location: String): WeatherResult
}