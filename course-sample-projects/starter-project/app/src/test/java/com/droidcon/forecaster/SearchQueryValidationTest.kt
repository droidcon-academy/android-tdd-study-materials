package com.droidcon.forecaster

import com.droidcon.forecaster.state.WeatherScreenState
import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

class SearchQueryValidationTest {

    @ParameterizedTest
    @CsvSource(
        "''",
        "' '",
        "'     '"
    )
    fun emptyQuery(emptyQuery: String) {
        val weatherViewModel = WeatherViewModel(InMemoryWeatherRepository(emptyMap()))

        weatherViewModel.fetchWeatherFor(emptyQuery)

        assertThat(weatherViewModel.uiState.value)
            .isEqualTo(WeatherScreenState(isBadQuery = true))
    }
}