package com.droidcon.forecaster

import com.google.common.truth.Truth.*
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
        val weatherInRotterdam = WeatherData(
            "Rotterdam",
            "Netherlands",
            "South Holland",
            20,
            "",
            "Clear",
            18
        )
        val weatherViewModel = WeatherViewModel()

        val result = weatherViewModel.fetchWeatherFor(rotterdam)

        assertThat(result).isEqualTo(weatherInRotterdam)
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