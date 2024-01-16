package com.droidcon.forecaster

import com.droidcon.forecaster.state.WeatherScreenState
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.Dispatchers
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

class SearchQueryValidationTest {

    private val queryLengthValidator = QueryLengthValidator()
    private val weatherRepository = InMemoryWeatherRepository(emptyMap())
    private val backgroundDispatcher = Dispatchers.Unconfined

    @ParameterizedTest
    @CsvSource(
        "''",
        "' '",
        "'     '"
    )
    fun emptyQuery(emptyQuery: String) {
        val weatherViewModel = WeatherViewModel(
            weatherRepository,
            queryLengthValidator,
            backgroundDispatcher
        )

        weatherViewModel.fetchWeatherFor(emptyQuery)

        assertThat(weatherViewModel.uiState.value)
            .isEqualTo(WeatherScreenState(isBadQuery = true))
    }

    @ParameterizedTest
    @CsvSource(
        ".",
        "AB",
        " AB ",
        "AB  "
    )
    fun shortQuery(query: String) {
        val weatherViewModel = WeatherViewModel(
            weatherRepository,
            queryLengthValidator,
            backgroundDispatcher
        )

        weatherViewModel.fetchWeatherFor(query)

        assertThat(weatherViewModel.uiState.value)
            .isEqualTo(WeatherScreenState(isBadQuery = true))
    }
}