package com.droidcon.forecaster

import com.droidcon.forecaster.data.WeatherResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {

    @GET("/current?access_key=somekey")
    suspend fun fetchWeatherForLocation(
        @Query("query") query: String
    ): WeatherResponse
}