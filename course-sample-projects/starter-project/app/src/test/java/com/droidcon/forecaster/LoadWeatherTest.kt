package com.droidcon.forecaster

import com.droidcon.forecaster.WeatherDataBuilder.Companion.aWeatherData
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
            .withTemperature(20)
            .build()
        val weatherViewModel = WeatherViewModel()

        val result = weatherViewModel.fetchWeatherFor(rotterdam)

        assertThat(result).isEqualTo(weatherInRotterdam)
    }

    @Test
    fun weatherAvailableForAnotherCity() {
        val berlin = "Berlin"
        val weatherInBerlin = aWeatherData()
            .withCity(berlin)
            .withCountry("Germany")
            .withTemperature(22)
            .build()
        val weatherViewModel = WeatherViewModel()

        val result = weatherViewModel.fetchWeatherFor(berlin)

        assertThat(result).isEqualTo(weatherInBerlin)
    }

    class WeatherViewModel {

        fun fetchWeatherFor(location: String): WeatherData {
            val weatherForLocation = mutableMapOf(
                "Rotterdam" to WeatherData("Rotterdam", "", "", 20, "", "", 0),
                "Berlin" to WeatherData("Berlin", "Germany", "", 22, "", "", 0)
            )
            if (location == "Rotterdam" || location == "Berlin") {
                return weatherForLocation.getValue(location)
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