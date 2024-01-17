package com.droidcon.forecaster

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.droidcon.forecaster.data.WeatherResult
import com.droidcon.forecaster.state.WeatherScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val weatherRepository: WeatherRepository,
    private val queryLengthValidator: QueryLengthValidator,
    private val backgroundDispatcher: CoroutineDispatcher
) : ViewModel() {

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

    private fun updateScreenStateFor(weatherResult: WeatherResult) {
        when (weatherResult) {
            is WeatherResult.Unavailable -> updateScreenStateWithWeatherUnavailableError()
            is WeatherResult.Error -> updateScreenStateWithLoadingError()
            is WeatherResult.Loaded -> updateScreenStateWithWeatherData(weatherResult)
        }
    }

    private fun updateScreenStateWithLoading() {
        _uiState.update { it.copy(isLoading = true) }
    }

    private fun updateScreenStateWithWeatherUnavailableError() {
        _uiState.update { it.copy(isLoading = false, isWeatherUnavailable = true) }
    }

    private fun updateScreenStateWithLoadingError() {
        _uiState.update { it.copy(isLoading = false, isWeatherLoadingError = true) }
    }

    private fun updateScreenStateWithWeatherData(weatherResult: WeatherResult.Loaded) {
        _uiState.update { it.copy(isLoading = false, weatherData = weatherResult.data) }
    }

    private fun updateScreenStateWithBadQuery() {
        _uiState.update { it.copy(isBadQuery = true) }
    }
}