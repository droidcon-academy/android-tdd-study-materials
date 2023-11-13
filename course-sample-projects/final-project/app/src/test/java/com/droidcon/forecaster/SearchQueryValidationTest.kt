package com.droidcon.forecaster

import com.droidcon.forecaster.data.WeatherData
import com.droidcon.forecaster.state.WeatherScreenState
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

@ExtendWith(CoroutinesTestExtension::class)
class SearchQueryValidationTest {

    private val queryLengthValidator = QueryLengthValidator()
    private val backgroundDispatcher = UnconfinedTestDispatcher()

    @Test
    fun emptyQuery() = runTest {
        val emptyQuery = ""
        val weatherForLocation = emptyMap<String, WeatherData?>()
        val weatherViewModel = WeatherViewModel(
            InMemoryWeatherRepository(weatherForLocation),
            queryLengthValidator,
            backgroundDispatcher
        )

        weatherViewModel.fetchWeatherFor(emptyQuery)

        assertThat(weatherViewModel.uiState.value)
            .isEqualTo(WeatherScreenState(isBadQuery = true))
    }

    @ParameterizedTest
    @CsvSource(
        "' '",
        "'      '",
    )
    fun blankQuery(query: String) = runTest {
        val weatherForLocation = emptyMap<String, WeatherData?>()
        val weatherViewModel = WeatherViewModel(
            InMemoryWeatherRepository(weatherForLocation),
            queryLengthValidator,
            backgroundDispatcher
        )

        weatherViewModel.fetchWeatherFor(query)

        assertThat(weatherViewModel.uiState.value)
            .isEqualTo(WeatherScreenState(isBadQuery = true))
    }

    @ParameterizedTest
    @CsvSource(
        ".",
        "AB",
        " AB ",
        "AB  ",
    )
    fun shortQuery(query: String) = runTest {
        val weatherForLocation = emptyMap<String, WeatherData?>()
        val weatherViewModel = WeatherViewModel(
            InMemoryWeatherRepository(weatherForLocation),
            queryLengthValidator,
            backgroundDispatcher
        )

        weatherViewModel.fetchWeatherFor(query)

        assertThat(weatherViewModel.uiState.value)
            .isEqualTo(WeatherScreenState(isBadQuery = true))
    }

    @ParameterizedTest
    @CsvSource(
        "1, ' '",
        "2, ' A '",
        "3, ' AB '",
        "4, ' ABC  '",
    )
    fun customizableLengthRequirement(requiredLength: Int, query: String) = runTest {
        val weatherForLocation = emptyMap<String, WeatherData?>()
        val weatherViewModel = WeatherViewModel(
            InMemoryWeatherRepository(weatherForLocation),
            QueryLengthValidator(requiredLength),
            backgroundDispatcher
        )

        weatherViewModel.fetchWeatherFor(query)

        assertThat(weatherViewModel.uiState.value)
            .isEqualTo(WeatherScreenState(isBadQuery = true))
    }
}