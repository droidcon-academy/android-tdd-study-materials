package com.droidcon.forecaster.data

sealed class WeatherResult {
    object Error : WeatherResult()

    object Unavailable : WeatherResult()

    data class Loaded(val data: WeatherData) : WeatherResult()
}