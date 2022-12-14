package com.vlohachov.moviespot.ui.main

import android.content.Context
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
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
    fun moreButtonsTest(): Unit = with(composeRule) {
        every { viewModel.uiState } returns MutableStateFlow(
            value = MainViewState(
                upcomingViewState = ViewState.Success(data = TestMovies),
                nowPlayingViewState = ViewState.Success(data = TestMovies),
                popularViewState = ViewState.Success(data = TestMovies),
                topRatedViewState = ViewState.Success(data = TestMovies),
            )
        )
        justRun { navigator.navigate(direction = any()) }

        setContent {
            MoviesPotTheme {
                Main(navigator = navigator, viewModel = viewModel)
            }
        }

        val moreButtons = onAllNodesWithTag(testTag = MoviesSectionDefaults.MoreButtonTestTag)
            .assertCountEquals(expectedSize = 4)

        repeat(times = 4) { index ->
            moreButtons[index]
                .assertIsDisplayed()
                .assertHasClickAction()
                .performClick()
        }

        verify(exactly = 4) { navigator.navigate(direction = any()) }
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

        onAllNodesWithTag(testTag = PosterDefaults.PosterTestTag)
            .assertCountEquals(expectedSize = TestMovies.size)
            .onFirst()
            .assertHasClickAction()
            .performClick()

        verify(exactly = 1) { navigator.navigate(direction = any()) }
    }
}