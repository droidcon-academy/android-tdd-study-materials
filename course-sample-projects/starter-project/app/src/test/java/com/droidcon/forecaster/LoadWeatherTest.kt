package com.droidcon.forecaster

import com.droidcon.forecaster.WeatherDataBuilder.Companion.aWeatherData
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import org.junit.jupiter.api.Test

class LoadWeatherTest {

    @Test
    fun noWeatherAvailable() {
        val location = "location"
        val weatherViewModel = WeatherViewModel(InMemoryWeatherRepository())

        weatherViewModel.fetchWeatherFor(location)

        assertThat(weatherViewModel.uiState.value)
            .isEqualTo(WeatherScreenState(isWeatherUnavailable = true))
    }

    @Test
    fun weatherAvailable() {
        val rotterdam = "Rotterdam"
        val weatherInRotterdam = aWeatherData()
            .withCity("Rotterdam")
            .withTemperature(20)
            .build()
        val weatherViewModel = WeatherViewModel(InMemoryWeatherRepository())

        weatherViewModel.fetchWeatherFor(rotterdam)

        assertThat(weatherViewModel.uiState.value)
            .isEqualTo(WeatherScreenState(weatherData = weatherInRotterdam))
    }

    @Test
    fun weatherAvailableForAnotherCity() {
        val berlin = "Berlin"
        val weatherInBerlin = aWeatherData()
            .withCity(berlin)
            .withCountry("Germany")
            .withTemperature(22)
            .build()
        val weatherViewModel = WeatherViewModel(InMemoryWeatherRepository())

        weatherViewModel.fetchWeatherFor(berlin)

        assertThat(weatherViewModel.uiState.value)
            .isEqualTo(WeatherScreenState(weatherData = weatherInBerlin))
    }

    @Test
    fun errorLoadingWeather() {
        val location = "London"
        val weatherViewModel = WeatherViewModel(InMemoryWeatherRepository())

        weatherViewModel.fetchWeatherFor(location)

        assertThat(weatherViewModel.uiState.value)
            .isEqualTo(WeatherScreenState(isWeatherLoadingError = true))
    }

    class WeatherViewModel(private val weatherRepository: InMemoryWeatherRepository) {

        private val _uiState = MutableStateFlow(WeatherScreenState())
        val uiState: StateFlow<WeatherScreenState> = _uiState.asStateFlow()

        fun fetchWeatherFor(location: String) {
            when (val weatherResult = weatherRepository.loadWeatherFor(location)) {
                is WeatherResult.Unavailable -> _uiState.update { it.copy(isWeatherUnavailable = true) }
                is WeatherResult.Error -> _uiState.update { it.copy(isWeatherLoadingError = true) }
                is WeatherResult.Loaded -> _uiState.update { it.copy(weatherData = weatherResult.data) }
            }
        }
    }

    data class WeatherScreenState(
        val isWeatherUnavailable: Boolean = false,
        val isWeatherLoadingError: Boolean = false,
        val weatherData: WeatherData = WeatherData.Empty
    )

    sealed class WeatherResult {
        object Error : WeatherResult()

        object Unavailable : WeatherResult()

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

    class InMemoryWeatherRepository {
        private val weatherForLocation = mutableMapOf(
            "Rotterdam" to WeatherData("Rotterdam", "", "", 20, "", "", 0),
            "Berlin" to WeatherData("Berlin", "Germany", "", 22, "", "", 0),
            "London" to null
        )

        fun loadWeatherFor(location: String): WeatherResult {
            return if (weatherForLocation.containsKey(location)) {
                val weatherData = weatherForLocation[location]
                if (weatherData == null) WeatherResult.Error else WeatherResult.Loaded(weatherData)
            } else {
                WeatherResult.Unavailable
            }
        }
    }
}