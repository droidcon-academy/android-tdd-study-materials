package com.droidcon.forecaster

import com.droidcon.forecaster.WeatherDataBuilder.Companion.aWeatherData
import com.droidcon.forecaster.state.WeatherScreenState
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.Dispatchers
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(CoroutinesTestExtension::class)
class LoadWeatherTest {

    private val rotterdam = "Rotterdam"
    private val berlin = "Berlin"
    private val london = "London"
    private val weatherInRotterdam = aWeatherData()
        .withCity(rotterdam)
        .withTemperature(20)
        .build()
    private val weatherInBerlin = aWeatherData()
        .withCity(berlin)
        .withCountry("Germany")
        .withTemperature(22)
        .build()
    private val weatherNotAvailable = null

    private val queryLengthValidator = QueryLengthValidator()
    private val backgroundDispatcher = Dispatchers.Unconfined

    @Test
    fun noWeatherAvailable() {
        val location = "location"
        val weatherViewModel = WeatherViewModel(
            InMemoryWeatherRepository(emptyMap()),
            queryLengthValidator,
            backgroundDispatcher
        )

        weatherViewModel.fetchWeatherFor(location)

        assertThat(weatherViewModel.uiState.value)
            .isEqualTo(WeatherScreenState(isWeatherUnavailable = true))
    }

    @Test
    fun weatherAvailable() {
        val weatherForLocation = mutableMapOf(
            rotterdam to weatherInRotterdam,
            berlin to weatherInBerlin,
            london to weatherNotAvailable
        )
        val weatherViewModel = WeatherViewModel(
            InMemoryWeatherRepository(weatherForLocation),
            queryLengthValidator,
            backgroundDispatcher
        )

        weatherViewModel.fetchWeatherFor(rotterdam)

        assertThat(weatherViewModel.uiState.value)
            .isEqualTo(WeatherScreenState(weatherData = weatherInRotterdam))
    }

    @Test
    fun weatherAvailableForAnotherCity() {
        val weatherForLocation = mutableMapOf(
            rotterdam to weatherInRotterdam,
            berlin to weatherInBerlin,
            london to weatherNotAvailable
        )
        val weatherViewModel = WeatherViewModel(
            InMemoryWeatherRepository(weatherForLocation),
            queryLengthValidator,
            backgroundDispatcher
        )

        weatherViewModel.fetchWeatherFor(berlin)

        assertThat(weatherViewModel.uiState.value)
            .isEqualTo(WeatherScreenState(weatherData = weatherInBerlin))
    }

    @Test
    fun errorLoadingWeather() {
        val weatherForLocation = mutableMapOf(
            rotterdam to weatherInRotterdam,
            berlin to weatherInBerlin,
            london to weatherNotAvailable
        )
        val weatherViewModel = WeatherViewModel(
            InMemoryWeatherRepository(weatherForLocation),
            queryLengthValidator,
            backgroundDispatcher
        )

        weatherViewModel.fetchWeatherFor(london)

        assertThat(weatherViewModel.uiState.value)
            .isEqualTo(WeatherScreenState(isWeatherLoadingError = true))
    }
}