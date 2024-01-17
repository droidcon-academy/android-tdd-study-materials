package com.droidcon.forecaster

import android.content.Context
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.platform.app.InstrumentationRegistry
import com.droidcon.forecaster.ui.theme.ForecasterTheme
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class WeatherScreenTest {

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
                    modifier = Modifier.fillMaxSize()
                )
            }
        }

        val toolbarTitle = context.getString(R.string.app_name)
        composeTestRule.onNodeWithText(toolbarTitle)
            .assertIsDisplayed()
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeatherScreenContent(modifier: Modifier = Modifier) {
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
        Box(modifier = Modifier.padding(paddingValues))
    }
}
