package com.droidcon.forecaster

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun WeatherScreen(
    weatherViewModel: WeatherViewModel = viewModel()
) {
    val state by weatherViewModel.uiState.collectAsStateWithLifecycle()
    WeatherScreenContent(
        modifier = Modifier.fillMaxSize(),
        weatherScreenState = state,
        onNewSearch = weatherViewModel::fetchWeatherFor
    )
}