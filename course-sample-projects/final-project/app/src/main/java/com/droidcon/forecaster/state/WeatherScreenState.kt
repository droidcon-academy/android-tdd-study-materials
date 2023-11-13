package com.droidcon.forecaster.state

import com.droidcon.forecaster.data.WeatherData

data class WeatherScreenState(
    val isLoading: Boolean = false,
    val isWeatherUnavailable: Boolean = false,
    val isWeatherLoadingError: Boolean = false,
    val isBadQuery: Boolean = false,
    val weatherData: WeatherData? = null
)