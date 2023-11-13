package com.droidcon.forecaster.data

import retrofit2.HttpException

fun WeatherResponse.toWeatherData() = WeatherData(
    city = location.name,
    country = location.country,
    region = location.region,
    temperature = current.temperature,
    iconUrl = current.icons.first(),
    description = current.descriptions.joinToString(),
    feelsLike = current.feelsLike
)

fun HttpException.toResult(): WeatherResult {
    return if (code() == 404) {
        WeatherResult.Error
    } else {
        WeatherResult.Unavailable
    }
}
