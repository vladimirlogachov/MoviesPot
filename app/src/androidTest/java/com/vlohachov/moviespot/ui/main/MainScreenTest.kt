package com.vlohachov.moviespot.ui.main

import android.content.Context
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onChildren
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.core.app.ApplicationProvider
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.vlohachov.moviespot.R
import com.vlohachov.moviespot.core.ViewState
import com.vlohachov.moviespot.data.TestMovies
import com.vlohachov.moviespot.ui.components.PosterDefaults
import com.vlohachov.moviespot.ui.components.movie.MoviesSectionDefaults
import com.vlohachov.moviespot.ui.theme.MoviesPotTheme
import io.mockk.every
import io.mockk.justRun
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Rule
import org.junit.Test

class MainScreenTest {

    @get:Rule
    val composeRule = createComposeRule()

    private val navigator = mockk<DestinationsNavigator>()
    private val viewModel = mockk<MainViewModel>()

    private val context = ApplicationProvider.getApplicationContext<Context>()

    @Test
    fun titleTest(): Unit = with(composeRule) {
        every { viewModel.uiState } returns MutableStateFlow(value = MainViewState())

        setContent {
            MoviesPotTheme {
                Main(navigator = navigator, viewModel = viewModel)
            }
        }

        onNodeWithText(text = context.getString(R.string.app_name))
            .assertExists(errorMessageOnFail = "No Title component found.")
            .assertIsDisplayed()
    }

    @Test
    fun searchButtonTest(): Unit = with(composeRule) {
        every { viewModel.uiState } returns MutableStateFlow(value = MainViewState())
        justRun { navigator.navigate(direction = any()) }

        setContent {
            MoviesPotTheme {
                Main(navigator = navigator, viewModel = viewModel)
            }
        }

        onNodeWithTag(testTag = MainScreenDefaults.SearchButtonTestTag)
            .assertExists(errorMessageOnFail = "No Search button component found.")
            .assertIsDisplayed()
            .assertHasClickAction()
            .performClick()

        verify(exactly = 1) { navigator.navigate(direction = any()) }
    }

    @Test
    fun discoverButtonTest(): Unit = with(composeRule) {
        every { viewModel.uiState } returns MutableStateFlow(value = MainViewState())
        justRun { navigator.navigate(direction = any()) }

        setContent {
            MoviesPotTheme {
                Main(navigator = navigator, viewModel = viewModel)
            }
        }

        onNodeWithTag(testTag = MainScreenDefaults.DiscoverButtonTestTag)
            .assertExists(errorMessageOnFail = "No Discover button component found.")
            .assertIsDisplayed()
            .assertHasClickAction()
            .performClick()

        verify(exactly = 1) { navigator.navigate(direction = any()) }
    }

    @Test
    fun errorTest(): Unit = with(composeRule) {
        every { viewModel.uiState } returns MutableStateFlow(
            value = MainViewState(error = Exception())
        )
        justRun { viewModel.onErrorConsumed() }

        setContent {
            MoviesPotTheme {
                Main(navigator = navigator, viewModel = viewModel)
            }
        }

        onNodeWithTag(testTag = MainScreenDefaults.ErrorBarTestTag)
            .assertExists(errorMessageOnFail = "No Error component found.")
            .assertIsDisplayed()

        mainClock.advanceTimeBy(milliseconds = 4_000)

        verify(exactly = 1) { viewModel.onErrorConsumed() }
    }

    @Test
    fun sectionsTest(): Unit = with(composeRule) {
        every { viewModel.uiState } returns MutableStateFlow(
            value = MainViewState()
        )

        setContent {
            MoviesPotTheme {
                Main(navigator = navigator, viewModel = viewModel)
            }
        }

        onNodeWithTag(testTag = MainScreenDefaults.SectionsTestTag)
            .assertExists(errorMessageOnFail = "No Error component found.")
            .assertIsDisplayed()
            .onChildren()
            .assertCountEquals(expectedSize = 4)
    }

    @Test
    fun moreButtonTest(): Unit = with(composeRule) {
        every { viewModel.uiState } returns MutableStateFlow(
            value = MainViewState(
                upcomingViewState = ViewState.Success(data = TestMovies),
            )
        )
        justRun { navigator.navigate(direction = any()) }

        setContent {
            MoviesPotTheme {
                Main(navigator = navigator, viewModel = viewModel)
            }
        }

        onAllNodesWithTag(testTag = MoviesSectionDefaults.MoreButtonTestTag, useUnmergedTree = true)
            .onFirst()
            .assertIsDisplayed()
            .assertHasClickAction()
            .performClick()

        verify(exactly = 1) { navigator.navigate(direction = any()) }
    }

    @Test
    fun seeDetailsTest(): Unit = with(composeRule) {
        every { viewModel.uiState } returns MutableStateFlow(
            value = MainViewState(upcomingViewState = ViewState.Success(data = TestMovies))
        )
        justRun { navigator.navigate(direction = any()) }

        setContent {
            MoviesPotTheme {
                Main(navigator = navigator, viewModel = viewModel)
            }
        }

        onAllNodesWithTag(testTag = PosterDefaults.PosterTestTag, useUnmergedTree = true)
            .onFirst()
            .assertHasClickAction()
            .performClick()

        verify(exactly = 1) { navigator.navigate(direction = any()) }
    }
}
