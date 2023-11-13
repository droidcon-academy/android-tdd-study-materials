package com.droidcon.forecaster

import com.droidcon.forecaster.WeatherDataBuilder.Companion.aWeatherDataFor
import com.droidcon.forecaster.data.WeatherData
import com.droidcon.forecaster.data.WeatherResult
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

abstract class WeatherRepositoryContractTest {

    private val location = "Location"
    private val weatherAtLocation = aWeatherDataFor()
        .city(location)
        .country("Country")
        .region("Region")
        .temperature(20)
        .feelsLike(19)
        .build()

    @Test
    fun forecastAvailable() = runTest {
        val repository = weatherRepositoryWith(location, weatherAtLocation)

        val result = repository.loadWeatherFor(location)

        assertThat(result).isEqualTo(WeatherResult.Loaded(weatherAtLocation))
    }

    @Test
    fun forecastNotAvailable() = runBlocking {
        val repository = weatherRepositoryWithout(location, weatherAtLocation)

        val result = repository.loadWeatherFor(location)

        assertThat(result).isEqualTo(WeatherResult.Unavailable)
    }

    @Test
    fun forecastLoadingError() = runBlocking {
        val missingWeatherAtLocation = null
        val repository = weatherRepositoryWith(location, missingWeatherAtLocation)

        val result = repository.loadWeatherFor(location)

        assertThat(result).isEqualTo(WeatherResult.Error)
    }

    protected abstract fun weatherRepositoryWith(
        location: String,
        weatherAtLocation: WeatherData?
    ): WeatherRepository

    protected abstract fun weatherRepositoryWithout(
        location: String,
        weatherAtLocation: WeatherData?
    ): WeatherRepository
}