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
    onNewSearch: (value: String) -> Unit
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
                    .align(Alignment.TopCenter)
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                onNewSearch = onNewSearch,
                isBadQuery = weatherScreenState.isBadQuery
            )
            weatherScreenState.weatherData?.let { weatherData ->
                WeatherInformation(weatherData = weatherData)
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
                text = " | ",
                style = MaterialTheme.typography.headlineSmall
            )
            Text(
                text = stringResource(
                    R.string.feels_like_template,
                    weatherData.feelsLike
                ),
                style = MaterialTheme.typography.headlineSmall
            )
        }
        Spacer(modifier = Modifier.height(64.dp))
    }
}

@Composable
private fun SearchBox(
    modifier: Modifier = Modifier,
    isBadQuery: Boolean,
    onNewSearch: (value: String) -> Unit
) {
    val focusManager = LocalFocusManager.current
    var query by remember { mutableStateOf("") }
    OutlinedTextField(
        modifier = modifier,
        value = query,
        onValueChange = { query = it },
        isError = isBadQuery,
        label = { Text(text = stringResource(id = R.string.search_hint)) },
        supportingText = {
           if (isBadQuery) {
               Text(text = stringResource(id = R.string.bad_query_error))
           }
        },
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        keyboardActions = KeyboardActions(onSearch = {
            focusManager.clearFocus()
            onNewSearch(query)
        })
    )
}

@Preview
@Composable
private fun PreviewWeatherScreenContent() {
    val weatherInRotterdam = WeatherData(
        city = "Rotterdam",
        country = "Netherlands",
        region = "South Holland",
        temperature = 16,
        iconUrl = "",
        description = "Cloudy",
        feelsLike = 15
    )
    ForecasterTheme {
        WeatherScreenContent(
            weatherScreenState = WeatherScreenState(weatherData = weatherInRotterdam),
            onNewSearch = {}
        )
    }
}

@Preview
@Composable
private fun PreviewWeatherScreenContentLoading() {
    ForecasterTheme {
        WeatherScreenContent(
            weatherScreenState = WeatherScreenState(isLoading = true),
            onNewSearch = {}
        )
    }
}