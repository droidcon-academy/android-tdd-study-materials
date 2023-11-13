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
    private val lengthValidator: QueryLengthValidator,
    private val backgroundDispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _uiState = MutableStateFlow(WeatherScreenState())
    val uiState: StateFlow<WeatherScreenState> = _uiState.asStateFlow()

    fun fetchWeatherFor(location: String) {
        if (lengthValidator.isValidQuery(location)) {
            updateScreenStateWithLoading()
            viewModelScope.launch {
                val result = withContext(backgroundDispatcher) {
                    weatherRepository.loadWeatherFor(location)
                }
                updateScreenStateFor(result)
            }
        } else {
            updateScreenStateWithBadQuery()
        }
    }

    private fun updateScreenStateFor(result: WeatherResult) {
        when (result) {
            is WeatherResult.Unavailable -> updateScreenStateWithUnavailableError()
            is WeatherResult.Error -> updateScreenStateWithLoadingError()
            is WeatherResult.Loaded -> updateScreenStateWithWeatherData(result)
        }
    }

    private fun updateScreenStateWithWeatherData(result: WeatherResult.Loaded) {
        _uiState.update { it.copy(isLoading = false, weatherData = result.data) }
    }

    private fun updateScreenStateWithLoadingError() {
        _uiState.update { it.copy(isLoading = false, isWeatherLoadingError = true) }
    }

    private fun updateScreenStateWithUnavailableError() {
        _uiState.update { it.copy(isLoading = false, isWeatherUnavailable = true) }
    }

    private fun updateScreenStateWithLoading() {
        _uiState.update { it.copy(isLoading = true) }
    }

    private fun updateScreenStateWithBadQuery() {
        _uiState.update { it.copy(isBadQuery = true) }
    }
}