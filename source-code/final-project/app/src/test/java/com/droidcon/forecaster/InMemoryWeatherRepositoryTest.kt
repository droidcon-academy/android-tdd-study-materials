package com.droidcon.forecaster

import com.droidcon.forecaster.data.WeatherData

class InMemoryWeatherRepositoryTest : WeatherRepositoryContractTest() {

    override fun weatherRepositoryWithout(
        location: String,
        weatherAtLocation: WeatherData?
    ): WeatherRepository {
        return weatherRepositoryWith("anythingBuy$location", weatherAtLocation)
    }

    override fun weatherRepositoryWith(
        location: String,
        weatherAtLocation: WeatherData?
    ): WeatherRepository {
        val weatherForLocation = mapOf(location to weatherAtLocation)
        return InMemoryWeatherRepository(weatherForLocation)
    }
}