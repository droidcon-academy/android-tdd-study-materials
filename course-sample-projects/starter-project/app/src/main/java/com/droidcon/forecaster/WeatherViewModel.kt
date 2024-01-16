package com.droidcon.forecaster

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.droidcon.forecaster.data.WeatherResult
import com.droidcon.forecaster.state.WeatherScreenState
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

class WeatherViewModel(
    private val weatherRepository: WeatherRepository,
    private val queryLengthValidator: QueryLengthValidator,
    private val backgroundDispatcher: CoroutineDispatcher
): ViewModel() {

    private val _uiState = MutableStateFlow(WeatherScreenState())
    val uiState: StateFlow<WeatherScreenState> = _uiState.asStateFlow()

    fun fetchWeatherFor(location: String) {
        if (queryLengthValidator.isValidQuery(location)) {
            updateScreenStateWithLoading()
            viewModelScope.launch {
                val weatherResult = withContext(backgroundDispatcher) {
                    weatherRepository.loadWeatherFor(location)
                }
                updateScreenStateFor(weatherResult)
            }
        } else {
            updateScreenStateWithBadQuery()
        }
    }

    private fun updateScreenStateWithLoading() {
        _uiState.update { it.copy(isLoading = true) }
    }

    private fun updateScreenStateFor(weatherResult: WeatherResult) {
        when (weatherResult) {
            is WeatherResult.Unavailable -> {
                _uiState.update { it.copy(isLoading = false, isWeatherUnavailable = true) }
            }
            is WeatherResult.Error -> {
                _uiState.update { it.copy(isLoading = false, isWeatherLoadingError = true) }
            }
            is WeatherResult.Loaded -> {
                _uiState.update { it.copy(isLoading = false, weatherData = weatherResult.data) }
            }
        }
    }

    private fun updateScreenStateWithBadQuery() {
        _uiState.update { it.copy(isBadQuery = true) }
    }
}