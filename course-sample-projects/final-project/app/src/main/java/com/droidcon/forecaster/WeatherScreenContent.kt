package com.droidcon.forecaster

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.droidcon.forecaster.data.WeatherData
import com.droidcon.forecaster.state.WeatherScreenState
import com.droidcon.forecaster.ui.theme.ForecasterTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeatherScreenContent(
    modifier: Modifier = Modifier,
    weatherScreenState: WeatherScreenState,
    onNewSearch: (query: String) -> Unit
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            CenterAlignedTopAppBar(
                modifier = Modifier.fillMaxWidth(),
                title = {
                    Text(text = stringResource(id = R.string.app_name))
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            SearchBox(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .align(Alignment.TopCenter),
                isBadQueryError = weatherScreenState.isBadQuery,
                onNewSearch = onNewSearch
            )
            when (weatherScreenState.weatherData) {
                null -> NoWeatherInformation(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    isLoadingError = weatherScreenState.isWeatherLoadingError,
                    isUnavailable = weatherScreenState.isWeatherUnavailable
                )
                else -> WeatherInformation(
                    weatherData = weatherScreenState.weatherData
                )
            }

            AnimatedVisibility(visible = weatherScreenState.isLoading) {
                val loadingDescription = stringResource(id = R.string.cd_loading_indicator)
                CircularProgressIndicator(
                    modifier = Modifier.semantics {
                        contentDescription = loadingDescription
                    }
                )
            }
        }
    }
}

@Composable
private fun SearchBox(
    modifier: Modifier = Modifier,
    isBadQueryError: Boolean,
    onNewSearch: (query: String) -> Unit
) {
    val focusManager = LocalFocusManager.current
    var query by remember { mutableStateOf("") }
    OutlinedTextField(
        modifier = modifier,
        value = query,
        onValueChange = { query = it },
        label = { Text(text = stringResource(id = R.string.search_hint)) },
        isError = isBadQueryError,
        supportingText = {
            if (isBadQueryError) {
                Text(text = stringResource(id = R.string.bad_query_error))
            }
        },
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        keyboardActions = KeyboardActions(
            onSearch = {
                focusManager.clearFocus()
                onNewSearch(query)
            }
        )
    )
}

@Composable
private fun NoWeatherInformation(
    modifier: Modifier = Modifier,
    isLoadingError: Boolean,
    isUnavailable: Boolean
) {
    Column(
        modifier = modifier
    ) {
        if (!isLoadingError && !isUnavailable) {
            Text(
                text = stringResource(id = R.string.idle_weather_state),
                style = MaterialTheme.typography.headlineMedium,
                textAlign = TextAlign.Center
            )
        }
        if (!isLoadingError && isUnavailable) {
            Text(
                text = stringResource(id = R.string.location_unavailable_error),
                style = MaterialTheme.typography.headlineMedium,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.error
            )
        }
        if (isLoadingError && !isUnavailable) {
            Text(
                text = stringResource(id = R.string.location_loading_error),
                style = MaterialTheme.typography.headlineMedium,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.error
            )
        }
    }
}

@Composable
private fun WeatherInformation(
    modifier: Modifier = Modifier,
    weatherData: WeatherData
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row {
            Text(
                text = weatherData.city,
                style = MaterialTheme.typography.headlineSmall
            )
            Spacer(modifier = Modifier.width(24.dp))
            Text(
                text = weatherData.country,
                style = MaterialTheme.typography.headlineSmall
            )
        }
        Text(text = weatherData.region)
        Box {
            Text(
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(horizontal = 12.dp),
                text = weatherData.temperature.toString(),
                style = MaterialTheme.typography.headlineLarge,
                fontSize = 164.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = .6f)
            )
            Text(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(top = 32.dp),
                text = "C",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = .6f)
            )
        }
        Row {
            Text(
                text = weatherData.description,
                style = MaterialTheme.typography.headlineSmall
            )
            Text(
                text = "  |  ",
                style = MaterialTheme.typography.headlineSmall
            )
            Text(
                text = stringResource(
                    id = R.string.feels_like_template,
                    weatherData.feelsLike
                ),
                style = MaterialTheme.typography.headlineSmall
            )
        }
        Spacer(modifier = Modifier.height(64.dp))
    }
}

@Preview
@Composable
private fun PreviewWeatherScreenIdle() {
    ForecasterTheme {
        WeatherScreenContent(
            modifier = Modifier.fillMaxSize(),
            weatherScreenState = WeatherScreenState(),
            onNewSearch = {}
        )
    }
}

@Preview
@Composable
private fun PreviewWeatherScreenLoading() {
    ForecasterTheme {
        WeatherScreenContent(
            modifier = Modifier.fillMaxSize(),
            weatherScreenState = WeatherScreenState(isLoading = true),
            onNewSearch = {}
        )
    }
}

@Preview
@Composable
private fun PreviewWeatherScreenContent() {
    val weatherData = WeatherData(
        city = "City",
        country = "Country",
        region = "Region",
        temperature = 15,
        iconUrl = "",
        description = "Clear",
        feelsLike = 15
    )
    ForecasterTheme {
        WeatherScreenContent(
            modifier = Modifier.fillMaxSize(),
            weatherScreenState = WeatherScreenState(weatherData = weatherData),
            onNewSearch = {}
        )
    }
}

@Preview
@Composable
private fun PreviewWeatherScreenUnavailable() {
    ForecasterTheme {
        WeatherScreenContent(
            modifier = Modifier.fillMaxSize(),
            weatherScreenState = WeatherScreenState(isWeatherUnavailable = true),
            onNewSearch = {}
        )
    }
}

@Preview
@Composable
private fun PreviewWeatherScreenLoadingError() {
    ForecasterTheme {
        WeatherScreenContent(
            modifier = Modifier.fillMaxSize(),
            weatherScreenState = WeatherScreenState(isWeatherLoadingError = true),
            onNewSearch = {}
        )
    }
}