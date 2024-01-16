package com.droidcon.forecaster

import com.droidcon.forecaster.data.WeatherData

class InMemoryWeatherRepositoryTest : WeatherRepositoryContractTest() {

    override fun weatherRepositoryWith(
        location: String,
        weatherAtLocation: WeatherData?
    ): WeatherRepository {
        val weatherForLocation = mapOf(location to weatherAtLocation)
        return InMemoryWeatherRepository(weatherForLocation)
    }

    override fun weatherRepositoryWithout(
        location: String,
        weatherAtLocation: WeatherData?
    ): WeatherRepository {
        val weatherForLocation = mapOf("anythingBut$location" to weatherAtLocation)
        return InMemoryWeatherRepository(weatherForLocation)
    }
}