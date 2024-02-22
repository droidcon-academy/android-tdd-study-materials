package com.droidcon.forecaster

import com.droidcon.forecaster.WeatherDataBuilder.Companion.aWeatherDataFor
import com.droidcon.forecaster.state.WeatherScreenState
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(CoroutinesTestExtension::class)
class ScreenStateDeliveryTest {

    private val location = "location"
    private val weatherAtLocation = aWeatherDataFor()
        .city(location)
        .build()
    private val weatherForLocation = mapOf(location to weatherAtLocation)

    @Test
    fun screenStatesDeliveredInOrder() = runTest {
        val repository = InMemoryWeatherRepository(weatherForLocation)
        val validator = QueryLengthValidator()
        val dispatcher = UnconfinedTestDispatcher()
        val weatherViewModel = WeatherViewModel(repository, validator, dispatcher)

        val deliveredStates = observeFlow(weatherViewModel.uiState) {
            weatherViewModel.fetchWeatherFor(location)
        }

        assertThat(deliveredStates).isEqualTo(
            listOf(
                WeatherScreenState(isLoading = true),
                WeatherScreenState(weatherData = weatherAtLocation)
            )
        )
    }
}