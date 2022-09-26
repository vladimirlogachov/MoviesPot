package com.vlohachov.moviespot.ui.discover

import android.content.Context
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.core.app.ApplicationProvider
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.vlohachov.moviespot.R
import com.vlohachov.moviespot.core.ViewState
import com.vlohachov.moviespot.data.TestGenres
import com.vlohachov.moviespot.ui.theme.MoviesPotTheme
import io.mockk.every
import io.mockk.justRun
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Rule
import org.junit.Test

class DiscoverTest {

    @get:Rule
    val composeRule = createComposeRule()

    private val navigator = mockk<DestinationsNavigator>()
    private val viewModel = mockk<DiscoverViewModel>()

    private val context = ApplicationProvider.getApplicationContext<Context>()

    @Test
    fun titleTest(): Unit = with(composeRule) {
        every { viewModel.uiState } returns MutableStateFlow(value = DiscoverViewState())

        setContent {
            MoviesPotTheme {
                Discover(navigator = navigator, viewModel = viewModel)
            }
        }

        onNodeWithText(text = context.getString(R.string.discover), useUnmergedTree = true)
            .assertExists(errorMessageOnFail = "No Title component found.")
            .assertIsDisplayed()
    }

    @Test
    fun uiStateGenresLoadingTest(): Unit = with(composeRule) {
        every { viewModel.uiState } returns MutableStateFlow(value = DiscoverViewState())

        setContent {
            MoviesPotTheme {
                Discover(navigator = navigator, viewModel = viewModel)
            }
        }

        onNodeWithTag(testTag = DiscoverDefaults.GenresLoadingTestTag, useUnmergedTree = true)
            .assertExists(errorMessageOnFail = "No Progress component found.")
            .assertIsDisplayed()
    }

    @Test
    fun uiStateGenresLoadedTest(): Unit = with(composeRule) {
        every { viewModel.uiState } returns MutableStateFlow(
            value = DiscoverViewState(genresViewState = ViewState.Success(data = TestGenres))
        )

        setContent {
            MoviesPotTheme {
                Discover(navigator = navigator, viewModel = viewModel)
            }
        }

        onNodeWithTag(testTag = DiscoverDefaults.GenresTestTag, useUnmergedTree = true)
            .assertExists(errorMessageOnFail = "No Genres component found.")
            .assertIsDisplayed()
            .onChildren()
            .assertCountEquals(expectedSize = TestGenres.size)
    }

    @Test
    fun uiStateGenresErrorTest(): Unit = with(composeRule) {
        val error = Exception("Loading failed.")

        every { viewModel.uiState } returns MutableStateFlow(
            value = DiscoverViewState(genresViewState = ViewState.Error(error = error))
        )
        justRun { viewModel.onError(error = any()) }

        setContent {
            MoviesPotTheme {
                Discover(navigator = navigator, viewModel = viewModel)
            }
        }

        onNodeWithTag(testTag = DiscoverDefaults.GenresTestTag, useUnmergedTree = true)
            .assertDoesNotExist()

        verify(exactly = 1) { viewModel.onError(error = error) }
    }
}