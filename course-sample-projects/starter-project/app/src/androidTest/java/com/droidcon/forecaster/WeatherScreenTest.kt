package com.droidcon.forecaster

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.unit.dp
import androidx.test.platform.app.InstrumentationRegistry
import com.droidcon.forecaster.data.WeatherData
import com.droidcon.forecaster.state.WeatherScreenState
import com.droidcon.forecaster.ui.theme.ForecasterTheme
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class WeatherScreenTest {

    private val weatherData = WeatherData(
        city = "City",
        country = "Country",
        region = "Region",
        temperature = 15,
        iconUrl = "",
        description = "Clear",
        feelsLike = 15
    )

    @get: Rule
    val composeTestRule = createComposeRule()

    private lateinit var context: Context

    @Before
    fun setUp() {
        context = InstrumentationRegistry.getInstrumentation().targetContext
    }

    @Test
    fun toolbarWithAppName() {
        composeTestRule.setContent {
            ForecasterTheme {
                WeatherScreenContent(
                    modifier = Modifier.fillMaxSize(),
                    weatherScreenState = WeatherScreenState(weatherData = weatherData)
                )
            }
        }

        val toolbarTitle = context.getString(R.string.app_name)
        composeTestRule.onNodeWithText(toolbarTitle)
            .assertIsDisplayed()
    }

    @Test
    fun weatherLocation() {
        composeTestRule.setContent {
            ForecasterTheme {
                WeatherScreenContent(
                    modifier = Modifier.fillMaxSize(),
                    weatherScreenState = WeatherScreenState(weatherData = weatherData)
                )
            }
        }

        composeTestRule.onNodeWithText(weatherData.city).assertIsDisplayed()
        composeTestRule.onNodeWithText(weatherData.country).assertIsDisplayed()
        composeTestRule.onNodeWithText(weatherData.region).assertIsDisplayed()
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeatherScreenContent(
    modifier: Modifier = Modifier,
    weatherScreenState: WeatherScreenState
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
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                weatherScreenState.weatherData?.let { weatherData ->
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
                }
            }
        }
    }
}
