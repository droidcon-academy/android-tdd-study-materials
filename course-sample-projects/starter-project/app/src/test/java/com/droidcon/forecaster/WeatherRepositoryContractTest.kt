package com.droidcon.forecaster

import com.droidcon.forecaster.WeatherDataBuilder.Companion.aWeatherData
import com.droidcon.forecaster.data.WeatherData
import com.droidcon.forecaster.data.WeatherResult
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

abstract class WeatherRepositoryContractTest {

    private val location = "location"
    private val weatherAtLocation = aWeatherData().withCity(location).build()

    @Test
    fun weatherAvailable() = runTest {
        val repository = weatherRepositoryWith(location, weatherAtLocation)

        val result = repository.loadWeatherFor(location)

        assertThat(result).isEqualTo(WeatherResult.Loaded(weatherAtLocation))
    }

    @Test
    fun noWeatherAvailable() = runTest {
        val repository = weatherRepositoryWithout(location, weatherAtLocation)

        val result = repository.loadWeatherFor(location)

        assertThat(result).isEqualTo(WeatherResult.Unavailable)
    }

    @Test
    fun loadingWeatherError() = runTest {
        val missingWeatherAtLocation = null
        val repository = weatherRepositoryWith(location, missingWeatherAtLocation)

        val result = repository.loadWeatherFor(location)

        assertThat(result).isEqualTo(WeatherResult.Error)
    }

    abstract fun weatherRepositoryWith(
        location: String,
        weatherAtLocation: WeatherData?
    ): WeatherRepository

    abstract fun weatherRepositoryWithout(
        location: String,
        weatherAtLocation: WeatherData?
    ): WeatherRepository
}