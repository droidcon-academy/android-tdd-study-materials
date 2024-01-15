package com.droidcon.forecaster

import com.droidcon.forecaster.LoadWeatherTest.WeatherDataBuilder.Companion.aWeatherData
import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test

class LoadWeatherTest {

    @Test
    fun noWeatherAvailable() {
        val location = "location"
        val weatherViewModel = WeatherViewModel()

        val result = weatherViewModel.fetchWeatherFor(location)

        assertThat(result).isEqualTo(WeatherData.Empty)
    }

    @Test
    fun weatherAvailable() {
        val rotterdam = "Rotterdam"
        val weatherInRotterdam = aWeatherData()
            .withCity("Rotterdam")
            .withCountry("Netherlands")
            .withRegion("South Holland")
            .withTemperature(20)
            .withDescription("Clear")
            .withFeelsLike(18)
            .build()

        val weatherViewModel = WeatherViewModel()

        val result = weatherViewModel.fetchWeatherFor(rotterdam)

        assertThat(result).isEqualTo(weatherInRotterdam)
    }

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

    class WeatherViewModel {

        fun fetchWeatherFor(location: String): WeatherData {
            if (location == "Rotterdam") {
                return WeatherData(
                    "Rotterdam",
                    "Netherlands",
                    "South Holland",
                    20,
                    "",
                    "Clear",
                    18
                )
            }
            return WeatherData.Empty
        }
    }

    data class WeatherData(
        val city: String,
        val country: String,
        val region: String,
        val temperature: Int,
        val iconUrl: String,
        val description: String,
        val feelsLike: Int
    ) {

        companion object {
            val Empty = WeatherData(
                city = "",
                country = "",
                region = "",
                temperature = 0,
                iconUrl = "",
                description = "",
                feelsLike = 0
            )
        }
    }
}