package com.droidcon.forecaster

import com.droidcon.forecaster.WeatherDataBuilder.Companion.aWeatherData
import com.droidcon.forecaster.data.WeatherResult
import com.google.common.truth.Truth.*
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

class InMemoryWeatherRepositoryTest {

    @Test
    fun weatherAvailable() = runTest {
        val location = "location"
        val weatherAtLocation = aWeatherData().withCity(location).build()
        val weatherForLocation = mapOf(location to weatherAtLocation)
        val repository = InMemoryWeatherRepository(weatherForLocation)

        val result = repository.loadWeatherFor(location)

        assertThat(result).isEqualTo(WeatherResult.Loaded(weatherAtLocation))
    }

    @Test
    fun noWeatherAvailable() = runTest {
        val location = "location"
        val weatherAtLocation = aWeatherData().withCity(location).build()
        val weatherForLocation = mapOf("anythingBut$location" to weatherAtLocation)
        val repository = InMemoryWeatherRepository(weatherForLocation)

        val result = repository.loadWeatherFor(location)

        assertThat(result).isEqualTo(WeatherResult.Unavailable)
    }

    @Test
    fun loadingWeatherError() = runTest {
        val location = "location"
        val noWeatherAvailableForLocation = mapOf(location to null)
        val repository = InMemoryWeatherRepository(noWeatherAvailableForLocation)

        val result = repository.loadWeatherFor(location)

        assertThat(result).isEqualTo(WeatherResult.Error)
    }
}