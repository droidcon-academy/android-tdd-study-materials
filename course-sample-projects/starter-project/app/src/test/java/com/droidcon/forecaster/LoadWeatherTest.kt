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

        assertThat(result).isEqualTo(WeatherResult.Loaded(WeatherData.Empty))
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

        assertThat(result).isEqualTo(WeatherResult.Loaded(weatherInRotterdam))
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

        assertThat(result).isEqualTo(WeatherResult.Loaded(weatherInBerlin))
    }

    @Test
    fun errorLoadingWeather() {
        val location = "London"
        val weatherViewModel = WeatherViewModel()
        val weatherLoadingError = WeatherResult.Error

        val result = weatherViewModel.fetchWeatherFor(location)

        assertThat(result).isEqualTo(weatherLoadingError)
    }

    class WeatherViewModel {

        private val weatherForLocation = mutableMapOf(
            "Rotterdam" to WeatherData("Rotterdam", "", "", 20, "", "", 0),
            "Berlin" to WeatherData("Berlin", "Germany", "", 22, "", "", 0),
            "London" to null
        )

        fun fetchWeatherFor(location: String): WeatherResult {
            val result = if (weatherForLocation.containsKey(location)) {
                weatherForLocation[location]
            } else {
                WeatherData.Empty
            }
            return if (result == null) WeatherResult.Error else WeatherResult.Loaded(result)
        }
    }

    sealed class WeatherResult {
        object Error : WeatherResult()

        data class Loaded(val data: WeatherData) : WeatherResult()
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