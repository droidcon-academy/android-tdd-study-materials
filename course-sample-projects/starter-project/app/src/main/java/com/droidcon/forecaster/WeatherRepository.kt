package com.droidcon.forecaster

import com.droidcon.forecaster.data.WeatherResult

interface WeatherRepository {
    fun loadWeatherFor(location: String): WeatherResult
}