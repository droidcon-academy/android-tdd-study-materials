package com.droidcon.forecaster

import com.droidcon.forecaster.data.WeatherResult
import com.droidcon.forecaster.state.WeatherScreenState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class WeatherViewModel(private val weatherRepository: WeatherRepository) {

    private val _uiState = MutableStateFlow(WeatherScreenState())
    val uiState: StateFlow<WeatherScreenState> = _uiState.asStateFlow()

    fun fetchWeatherFor(location: String) {
        if (location.isBlank()) {
            _uiState.update { it.copy(isBadQuery = true) }
        } else {
            when (val weatherResult = weatherRepository.loadWeatherFor(location)) {
                is WeatherResult.Unavailable -> _uiState.update { it.copy(isWeatherUnavailable = true) }
                is WeatherResult.Error -> _uiState.update { it.copy(isWeatherLoadingError = true) }
                is WeatherResult.Loaded -> _uiState.update { it.copy(weatherData = weatherResult.data) }
            }
        }
    }
}