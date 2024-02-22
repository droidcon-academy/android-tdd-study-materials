package com.droidcon.forecaster

import com.droidcon.forecaster.data.WeatherData

class WeatherDataBuilder {

    private var city: String = ""
    private var country: String = ""
    private var region: String = ""
    private var temperature: Int = 0
    private var iconUrl: String = ""
    private var description: String = ""
    private var feelsLike: Int = 0

    fun withCity(city: String) = apply {
        this.city = city
    }

    fun withCountry(country: String) = apply {
        this.country = country
    }

    fun withRegion(region: String) = apply {
        this.region = region
    }

    fun withTemperature(temperature: Int) = apply {
        this.temperature = temperature
    }

    fun withIconUrl(iconUrl: String) = apply {
        this.iconUrl = iconUrl
    }

    fun withDescription(description: String) = apply {
        this.description = description
    }

    fun withFeelsLike(feelsLike: Int) = apply {
        this.feelsLike = feelsLike
    }

    fun build(): WeatherData {
        return WeatherData(
            city,
            country,
            region,
            temperature,
            iconUrl,
            description,
            feelsLike
        )
    }

    companion object {
        fun aWeatherData() = WeatherDataBuilder()
    }
}