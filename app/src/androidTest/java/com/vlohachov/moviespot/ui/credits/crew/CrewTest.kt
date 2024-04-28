package com.vlohachov.moviespot.ui.credits.crew

import android.content.Context
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onChildren
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollToIndex
import androidx.test.core.app.ApplicationProvider
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.vlohachov.moviespot.R
import com.vlohachov.moviespot.core.ViewState
import com.vlohachov.moviespot.data.TestCrewMembers
import com.vlohachov.moviespot.ui.components.button.ScrollToTopDefaults
import com.vlohachov.shared.domain.model.movie.credit.CrewMember
import com.vlohachov.shared.ui.theme.MoviesPotTheme
import io.mockk.every
import io.mockk.justRun
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Rule
import org.junit.Test

class CrewTest {

    @get:Rule
    val composeRule = createComposeRule()

    private val navigator = mockk<DestinationsNavigator>()
    private val viewModel = mockk<CrewViewModel>()

    private val context = ApplicationProvider.getApplicationContext<Context>()

    @Test
    fun titleTest(): Unit = with(composeRule) {
        every { viewModel.uiState } returns MutableStateFlow(value = CrewViewState())

        setContent {
            MoviesPotTheme {
                Crew(navigator = navigator, movieId = 0, viewModel = viewModel)
            }
        }

        onNodeWithText(text = context.getString(R.string.crew))
            .assertExists(errorMessageOnFail = "No Title component found.")
            .assertIsDisplayed()
    }

    @Test
    fun uiStateContentLoadingTest(): Unit = with(composeRule) {
        every { viewModel.uiState } returns MutableStateFlow(value = CrewViewState())

        setContent {
            MoviesPotTheme {
                Crew(navigator = navigator, movieId = 0, viewModel = viewModel)
            }
        }

        onNodeWithTag(testTag = CrewDefaults.ContentErrorTestTag, useUnmergedTree = true)
            .assertExists(errorMessageOnFail = "No Error component found.")
            .assertIsNotDisplayed()
        onNodeWithTag(testTag = CrewDefaults.ContentTestTag, useUnmergedTree = true)
            .assertExists(errorMessageOnFail = "No Content component found.")
            .assertIsDisplayed()
            .onChildren()
            .assertCountEquals(expectedSize = 1)
        onNodeWithTag(testTag = CrewDefaults.ContentLoadingTestTag, useUnmergedTree = true)
            .assertExists(errorMessageOnFail = "No Progress component found.")
            .assertIsDisplayed()
    }

    @Test
    fun uiStateContentLoadedTest(): Unit = with(composeRule) {
        every { viewModel.uiState } returns MutableStateFlow(
            value = CrewViewState(viewState = ViewState.Success(data = TestCrewMembers))
        )

        setContent {
            MoviesPotTheme {
                Crew(navigator = navigator, movieId = 0, viewModel = viewModel)
            }
        }

        onNodeWithTag(testTag = CrewDefaults.ContentErrorTestTag, useUnmergedTree = true)
            .assertExists(errorMessageOnFail = "No Error component found.")
            .assertIsNotDisplayed()
        onNodeWithTag(testTag = CrewDefaults.ContentTestTag, useUnmergedTree = true)
            .assertExists(errorMessageOnFail = "No Content component found.")
            .assertIsDisplayed()
            .onChildren()
            .assertCountEquals(expectedSize = TestCrewMembers.size)
    }

    @Test
    fun uiStateContentErrorTest(): Unit = with(composeRule) {
        val error = Exception("Loading failed.")

        every { viewModel.uiState } returns MutableStateFlow(
            value = CrewViewState(viewState = ViewState.Error(error = error))
        )
        justRun { viewModel.onError(error = any()) }

        setContent {
            MoviesPotTheme {
                Crew(navigator = navigator, movieId = 0, viewModel = viewModel)
            }
        }

        verify(exactly = 1) { viewModel.onError(error = error) }
    }

    @Test
    fun uiStateErrorTest(): Unit = with(composeRule) {
        val error = Exception("Loading failed.")

        every { viewModel.uiState } returns MutableStateFlow(value = CrewViewState(error = error))
        justRun { viewModel.onErrorConsumed() }

        setContent {
            MoviesPotTheme {
                Crew(navigator = navigator, movieId = 0, viewModel = viewModel)
            }
        }

        onNodeWithTag(testTag = CrewDefaults.ContentErrorTestTag, useUnmergedTree = true)
            .assertExists(errorMessageOnFail = "No Error component found.")
            .assertIsDisplayed()

        mainClock.advanceTimeBy(milliseconds = 4_000)

        verify(exactly = 1) { viewModel.onErrorConsumed() }
    }

    @Test
    fun navigateUpTest(): Unit = with(composeRule) {
        every { viewModel.uiState } returns MutableStateFlow(value = CrewViewState())
        every { navigator.navigateUp() } returns true

        setContent {
            MoviesPotTheme {
                Crew(navigator = navigator, movieId = 0, viewModel = viewModel)
            }
        }

        onNode(matcher = hasClickAction(), useUnmergedTree = true)
            .assertExists(errorMessageOnFail = "No Button back component found.")
            .assertIsDisplayed()
            .performClick()

        verify(exactly = 1) { navigator.navigateUp() }
    }

    @Test
    fun scrollToTopTest(): Unit = with(composeRule) {
        val largeList = mutableListOf<CrewMember>()

        repeat(times = 5) { largeList += TestCrewMembers }

        every { viewModel.uiState } returns MutableStateFlow(
            value = CrewViewState(
                viewState = ViewState.Success(data = largeList)
            )
        )

        setContent {
            MoviesPotTheme {
                Crew(navigator = navigator, movieId = 0, viewModel = viewModel)
            }
        }

        onNodeWithTag(testTag = CrewDefaults.ContentTestTag, useUnmergedTree = true)
            .assertExists(errorMessageOnFail = "No Content component found.")
            .assertIsDisplayed()
            .performScrollToIndex(index = largeList.size - 1)

        onNodeWithTag(testTag = ScrollToTopDefaults.ScrollToTopTestTag, useUnmergedTree = true)
            .assertExists(errorMessageOnFail = "No ScrollToTop component found.")
            .assertIsDisplayed()
            .performClick()

        onNodeWithTag(testTag = ScrollToTopDefaults.ScrollToTopTestTag, useUnmergedTree = true)
            .assertDoesNotExist()
    }
}
