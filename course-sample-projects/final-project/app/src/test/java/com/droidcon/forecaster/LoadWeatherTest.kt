package com.droidcon.forecaster

import com.droidcon.forecaster.WeatherDataBuilder.Companion.aWeatherDataFor
import com.droidcon.forecaster.data.WeatherData
import com.droidcon.forecaster.state.WeatherScreenState
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(CoroutinesTestExtension::class)
class LoadWeatherTest {

    private val rotterdam = "Rotterdam"
    private val berlin = "Berlin"
    private val london = "London"
    private val weatherInRotterdam = aWeatherDataFor()
        .city(rotterdam)
        .country("Netherlands")
        .temperature(20)
        .build()
    private val weatherInBerlin = aWeatherDataFor()
        .city(berlin)
        .country("Germany")
        .temperature(22)
        .build()
    private val weatherNotAvailable = null

    private val queryLengthValidator = QueryLengthValidator()
    private val backgroundDispatcher = UnconfinedTestDispatcher()

    @Test
    fun noWeatherAvailable() = runTest {
        val somewhere = ":irrelevant:"
        val weatherForLocation = emptyMap<String, WeatherData?>()
        val weatherViewModel = WeatherViewModel(
            InMemoryWeatherRepository(weatherForLocation),
            queryLengthValidator,
            backgroundDispatcher
        )

        weatherViewModel.fetchWeatherFor(somewhere)

        assertThat(weatherViewModel.uiState.value)
            .isEqualTo(WeatherScreenState(isWeatherUnavailable = true))
    }

    @Test
    fun weatherAvailable() = runTest {
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
    fun weatherAvailableForAnotherCity() = runTest {
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
    fun errorLoadingWeather() = runTest {
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