package com.droidcon.forecaster.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class WeatherResponse(
    val location: Location,
    val current: Current
) {

    @Serializable
    data class Location(
        val name: String,
        val country: String,
        val region: String
    )

    @Serializable
    data class Current(
        val temperature: Int,
        @SerialName("weather_icons") val icons: List<String>,
        @SerialName("weather_descriptions") val descriptions: List<String>,
        @SerialName("feelslike") val feelsLike: Int
    )
}