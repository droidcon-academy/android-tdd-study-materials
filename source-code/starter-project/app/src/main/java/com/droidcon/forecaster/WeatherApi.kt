package com.droidcon.forecaster

import com.droidcon.forecaster.data.WeatherResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {

    @GET("/current?")
    suspend fun fetchWeatherForLocation(
        @Query("query") query: String
    ): WeatherResponse
}