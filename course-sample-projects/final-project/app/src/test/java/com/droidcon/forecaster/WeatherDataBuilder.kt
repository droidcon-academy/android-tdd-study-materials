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

    fun city(city: String): WeatherDataBuilder = apply {
        this.city = city
    }

    fun country(country: String): WeatherDataBuilder = apply {
        this.country = country
    }

    fun region(region: String): WeatherDataBuilder = apply {
        this.region = region
    }

    fun temperature(value: Int): WeatherDataBuilder = apply {
        this.temperature = value
    }

    fun description(description: String): WeatherDataBuilder = apply {
        this.description = description
    }

    fun feelsLike(value: Int): WeatherDataBuilder = apply {
        this.feelsLike = value
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
        fun aWeatherDataFor(): WeatherDataBuilder {
            return WeatherDataBuilder()
        }
    }
}