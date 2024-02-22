package com.droidcon.forecaster.di

import com.droidcon.forecaster.QueryLengthValidator
import com.droidcon.forecaster.RemoteWeatherRepository
import com.droidcon.forecaster.WeatherApi
import com.droidcon.forecaster.WeatherRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideWeatherRepository(retrofit: Retrofit): WeatherRepository {
        val weatherApi = retrofit.create(WeatherApi::class.java)
        return RemoteWeatherRepository(weatherApi)
    }

    @Provides
    @Singleton
    fun provideQueryLengthValidator(): QueryLengthValidator {
        return QueryLengthValidator()
    }

    @Provides
    @Singleton
    fun provideBackgroundDispatcher(): CoroutineDispatcher {
        return Dispatchers.IO
    }
}