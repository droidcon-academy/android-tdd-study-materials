package com.droidcon.forecaster

import com.google.common.truth.Truth.*
import org.junit.jupiter.api.Test

class LoadWeatherTest {

    @Test
    fun noWeatherAvailable() {
        val location = "location"

        val result = WeatherViewModel().fetchWeatherFor(location)

        assertThat(result).isEqualTo(WeatherData.Empty)
    }

    class WeatherViewModel {

        fun fetchWeatherFor(location: String): WeatherData {
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