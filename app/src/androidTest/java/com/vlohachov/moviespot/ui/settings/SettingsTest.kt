package com.vlohachov.moviespot.ui.settings

import android.content.Context
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onChildren
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.core.app.ApplicationProvider
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.vlohachov.moviespot.BuildConfig
import com.vlohachov.moviespot.R
import com.vlohachov.moviespot.data.TestSettings
import com.vlohachov.shared.ui.component.bar.AppBarDefaults
import com.vlohachov.shared.ui.component.bar.ErrorBarDefaults
import com.vlohachov.shared.ui.state.ViewState
import com.vlohachov.shared.ui.theme.MoviesPotTheme
import io.mockk.every
import io.mockk.justRun
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.flowOf
import org.junit.Rule
import org.junit.Test

class SettingsTest {

    @get:Rule
    val composeRule = createComposeRule()

    private val navigator = mockk<DestinationsNavigator>()
    private val viewModel = mockk<SettingsViewModel>()

    private val context = ApplicationProvider.getApplicationContext<Context>()

    @Test
    fun titleTest(): Unit = with(composeRule) {
        every { viewModel.viewState } returns flowOf(value = ViewState.Loading)
        every { viewModel.error } returns null

        setContent {
            MoviesPotTheme {
                Settings(navigator = navigator, viewModel = viewModel)
            }
        }

        onNodeWithText(text = context.getString(R.string.settings))
            .assertExists(errorMessageOnFail = "No Title component found.")
            .assertIsDisplayed()
    }

    @Test
    fun backButtonTest(): Unit = with(composeRule) {
        every { viewModel.viewState } returns flowOf(value = ViewState.Loading)
        every { navigator.navigateUp() } returns true
        every { viewModel.error } returns null

        setContent {
            MoviesPotTheme {
                Settings(navigator = navigator, viewModel = viewModel)
            }
        }

        onNodeWithTag(testTag = AppBarDefaults.BackButtonTestTag)
            .assertExists(errorMessageOnFail = "No Back button component found.")
            .assertIsDisplayed()
            .assertHasClickAction()
            .performClick()

        verify(exactly = 1) { navigator.navigateUp() }
    }

    @Test
    fun contentLoadingTest(): Unit = with(composeRule) {
        every { viewModel.viewState } returns flowOf(value = ViewState.Loading)
        every { viewModel.error } returns null

        setContent {
            MoviesPotTheme {
                Settings(navigator = navigator, viewModel = viewModel)
            }
        }

        onNodeWithTag(testTag = SettingsDefaults.ContentTestTag)
            .assertExists(errorMessageOnFail = "No Content component found")
            .assertIsDisplayed()
        onNodeWithTag(testTag = SettingsDefaults.LoadingTestTag)
            .assertExists(errorMessageOnFail = "No Loading component found")
            .assertIsDisplayed()
    }

    @Test
    fun contentLoadedTest(): Unit = with(composeRule) {
        every { viewModel.viewState } returns flowOf(value = ViewState.Success(data = TestSettings))
        every { viewModel.error } returns null

        setContent {
            MoviesPotTheme {
                Settings(navigator = navigator, viewModel = viewModel)
            }
        }

        onNodeWithTag(testTag = SettingsDefaults.ContentTestTag)
            .assertExists(errorMessageOnFail = "No Content component found")
            .assertIsDisplayed()
        onNodeWithTag(testTag = SettingsDefaults.LoadingTestTag)
            .assertDoesNotExist()
        onNodeWithTag(testTag = SettingsDefaults.DynamicThemeTestTag)
            .assertExists(errorMessageOnFail = "No DynamicTheme component found")
            .assertIsDisplayed()
            .onChildren()
            .assertCountEquals(expectedSize = 2)
    }

    @Test
    fun contentErrorTest(): Unit = with(composeRule) {
        val error = NullPointerException()

        every { viewModel.viewState } returns flowOf(value = ViewState.Error(error = error))
        every { viewModel.error } returns null
        justRun { viewModel.onError(error = any()) }

        setContent {
            MoviesPotTheme {
                Settings(navigator = navigator, viewModel = viewModel)
            }
        }

        onNodeWithTag(testTag = SettingsDefaults.ContentTestTag)
            .assertExists(errorMessageOnFail = "No Content component found")
            .assertIsDisplayed()
        onNodeWithTag(testTag = SettingsDefaults.LoadingTestTag)
            .assertDoesNotExist()
        onNodeWithTag(testTag = SettingsDefaults.DynamicThemeTestTag)
            .assertDoesNotExist()

        verify(exactly = 1) { viewModel.onError(error = error) }
    }

    @Test
    fun applyDynamicThemeTest(): Unit = with(composeRule) {
        every { viewModel.viewState } returns flowOf(value = ViewState.Success(data = TestSettings))
        every { viewModel.error } returns null
        justRun { viewModel.applyDynamicTheme(apply = any()) }

        setContent {
            MoviesPotTheme {
                Settings(navigator = navigator, viewModel = viewModel)
            }
        }

        onNodeWithTag(testTag = SettingsDefaults.DynamicThemeToggleTestTag)
            .assertExists(errorMessageOnFail = "No Toggle component found.")
            .assertIsDisplayed()
            .assertHasClickAction()
            .performClick()

        verify(exactly = 1) { viewModel.applyDynamicTheme(apply = any()) }
    }

    @Test
    fun appVersionTest(): Unit = with(composeRule) {
        every { viewModel.viewState } returns flowOf(value = ViewState.Loading)
        every { viewModel.error } returns null

        setContent {
            MoviesPotTheme {
                Settings(navigator = navigator, viewModel = viewModel)
            }
        }

        onNodeWithText(text = context.getString(R.string.app_version, BuildConfig.VERSION_NAME))
            .assertExists(errorMessageOnFail = "No AppVersion component found.")
            .assertIsDisplayed()
    }

    @Test
    fun authorTest(): Unit = with(composeRule) {
        every { viewModel.viewState } returns flowOf(value = ViewState.Loading)
        every { viewModel.error } returns null

        setContent {
            MoviesPotTheme {
                Settings(navigator = navigator, viewModel = viewModel)
            }
        }

        onNodeWithTag(testTag = SettingsDefaults.AuthorTestTag)
            .assertExists(errorMessageOnFail = "No AppVersion component found.")
            .assertIsDisplayed()
            .assertTextEquals(
                buildString {
                    append(context.getString(R.string.author))
                    append(context.getString(R.string.author_name))
                }
            )
    }

    @Test
    fun errorTest(): Unit = with(composeRule) {
        every { viewModel.viewState } returns flowOf(value = ViewState.Loading)
        every { viewModel.error } returns NullPointerException()
        justRun { viewModel.onErrorConsumed() }

        setContent {
            MoviesPotTheme {
                Settings(navigator = navigator, viewModel = viewModel)
            }
        }

        onNodeWithTag(testTag = ErrorBarDefaults.ErrorTestTag)
            .assertExists(errorMessageOnFail = "No Error component found.")
            .assertIsDisplayed()

        mainClock.advanceTimeBy(milliseconds = 4_000)

        verify(exactly = 1) { viewModel.onErrorConsumed() }
    }
}
