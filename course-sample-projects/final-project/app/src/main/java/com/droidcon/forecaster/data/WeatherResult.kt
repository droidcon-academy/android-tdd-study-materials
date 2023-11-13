package com.droidcon.forecaster.data

sealed class WeatherResult {

    data class Loaded(val data: WeatherData) : WeatherResult()

    object Error : WeatherResult()

    object Unavailable : WeatherResult()
}