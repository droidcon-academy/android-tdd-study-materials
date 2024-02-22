package com.droidcon.forecaster

import com.droidcon.forecaster.WeatherDataBuilder.Companion.aWeatherData
import com.droidcon.forecaster.state.WeatherScreenState
import com.google.common.truth.Truth.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@OptIn(ExperimentalCoroutinesApi::class)
@ExtendWith(CoroutinesTestExtension::class)
class ScreenStateDeliveryTest {

    @Test
    fun screenStatesDeliveredInOrder() = runTest {
        val location = "location"
        val weatherAtLocation = aWeatherData().withCity(location).build()
        val dispatcher = UnconfinedTestDispatcher()
        val validator = QueryLengthValidator()
        val weatherForLocation = mapOf(location to weatherAtLocation)
        val repository = InMemoryWeatherRepository(weatherForLocation)
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