package com.droidcon.forecaster.data

data class WeatherData(
    val city: String,
    val country: String,
    val region: String,
    val temperature: Int,
    val iconUrl: String,
    val description: String,
    val feelsLike: Int
)