package com.droidcon.forecaster

import android.content.Context
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
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
                ) {}
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
                ) {}
            }
        }

        composeTestRule.onNodeWithText(weatherData.city).assertIsDisplayed()
        composeTestRule.onNodeWithText(weatherData.country).assertIsDisplayed()
        composeTestRule.onNodeWithText(weatherData.region).assertIsDisplayed()
    }

    @Test
    fun temperatureData() {
        composeTestRule.setContent {
            ForecasterTheme {
                WeatherScreenContent(
                    modifier = Modifier.fillMaxSize(),
                    weatherScreenState = WeatherScreenState(weatherData = weatherData)
                ) {}
            }
        }

        composeTestRule.onNodeWithText(weatherData.temperature.toString()).assertIsDisplayed()
        composeTestRule.onNodeWithText(weatherData.description).assertIsDisplayed()
        composeTestRule.onNodeWithText(weatherData.feelsLike.toString()).assertIsDisplayed()
    }

    @Test
    fun searchBox() {
        composeTestRule.setContent {
            ForecasterTheme {
                WeatherScreenContent(
                    modifier = Modifier.fillMaxSize(),
                    weatherScreenState = WeatherScreenState(weatherData = weatherData)
                ) {}
            }
        }

        val typeLocation = context.getString(R.string.search_hint)
        composeTestRule.onNodeWithText(typeLocation).assertIsDisplayed()
    }

    @Test
    fun loadingIndicator() {
        composeTestRule.setContent {
            ForecasterTheme {
                WeatherScreenContent(
                    modifier = Modifier.fillMaxSize(),
                    weatherScreenState = WeatherScreenState(isLoading = true)
                ) {}
            }
        }

        val loadingContentDescription = context.getString(R.string.cd_loading_indicator)
        composeTestRule.onNodeWithContentDescription(loadingContentDescription)
            .assertIsDisplayed()
    }

    @Test
    fun badQueryError() {
        composeTestRule.setContent {
            ForecasterTheme {
                WeatherScreenContent(
                    modifier = Modifier.fillMaxSize(),
                    weatherScreenState = WeatherScreenState(isBadQuery = true)
                ) {}
            }
        }

        val badQueryError = context.getString(R.string.bad_query_error)
        composeTestRule.onNodeWithText(badQueryError).assertIsDisplayed()
    }
}