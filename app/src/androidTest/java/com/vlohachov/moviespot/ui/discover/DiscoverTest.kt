package com.vlohachov.moviespot.ui.discover

import android.content.Context
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.assertIsNotSelected
import androidx.compose.ui.test.assertIsSelected
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onChildren
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.core.app.ApplicationProvider
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.vlohachov.domain.model.genre.Genre
import com.vlohachov.moviespot.R
import com.vlohachov.moviespot.core.DummyGenres
import com.vlohachov.moviespot.core.ViewState
import com.vlohachov.moviespot.data.TestGenres
import com.vlohachov.moviespot.ui.components.bar.AppBarDefaults
import com.vlohachov.moviespot.ui.theme.MoviesPotTheme
import io.mockk.every
import io.mockk.justRun
import io.mockk.mockk
import io.mockk.slot
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
    fun navigateUpTest(): Unit = with(composeRule) {
        every { viewModel.uiState } returns MutableStateFlow(value = DiscoverViewState())
        every { navigator.navigateUp() } returns true

        setContent {
            MoviesPotTheme {
                Discover(navigator = navigator, viewModel = viewModel)
            }
        }

        onNodeWithTag(testTag = AppBarDefaults.BackButtonTestTag, useUnmergedTree = true)
            .assertExists(errorMessageOnFail = "No Button back component found.")
            .assertIsDisplayed()
            .performClick()

        verify(exactly = 1) { navigator.navigateUp() }
    }

    @Test
    fun discoverDisabledTest(): Unit = with(composeRule) {
        every { viewModel.uiState } returns MutableStateFlow(value = DiscoverViewState())

        setContent {
            MoviesPotTheme {
                Discover(navigator = navigator, viewModel = viewModel)
            }
        }

        onNodeWithTag(testTag = DiscoverDefaults.DiscoverButtonTestTag, useUnmergedTree = true)
            .assertExists(errorMessageOnFail = "No Button back component found.")
            .assertIsDisplayed()
            .assertIsNotEnabled()
    }

    @Test
    fun discoverEnabledTest(): Unit = with(composeRule) {
        every { viewModel.uiState } returns MutableStateFlow(
            value = DiscoverViewState(discoverEnabled = true)
        )
        justRun { navigator.navigate(direction = any()) }

        setContent {
            MoviesPotTheme {
                Discover(navigator = navigator, viewModel = viewModel)
            }
        }

        onNodeWithTag(testTag = DiscoverDefaults.DiscoverButtonTestTag, useUnmergedTree = true)
            .assertExists(errorMessageOnFail = "No Button back component found.")
            .assertIsDisplayed()
            .assertIsEnabled()
            .performClick()

        verify(exactly = 1) { navigator.navigate(direction = any()) }
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

    @Test
    fun uiStateYearInputTest(): Unit = with(composeRule) {
        val year = "2022"
        val stateFlow = MutableStateFlow(value = DiscoverViewState())
        val yearSlot = slot<String>()

        every { viewModel.uiState } returns stateFlow
        every { viewModel.onYear(year = capture(lst = yearSlot)) } answers {
            stateFlow.tryEmit(value = DiscoverViewState(year = yearSlot.captured))
        }

        setContent {
            MoviesPotTheme {
                Discover(navigator = navigator, viewModel = viewModel)
            }
        }

        onNodeWithTag(testTag = DiscoverDefaults.YearClearTestTag, useUnmergedTree = true)
            .assertDoesNotExist()
        onNodeWithTag(testTag = DiscoverDefaults.YearTestTag, useUnmergedTree = true)
            .assertExists(errorMessageOnFail = "No Year input component found")
            .assertIsDisplayed()
            .assertTextEquals("")
            .performTextInput(text = year)
        onNodeWithTag(testTag = DiscoverDefaults.YearClearTestTag, useUnmergedTree = true)
            .assertExists(errorMessageOnFail = "No Clear input component found")
            .assertIsDisplayed()
        onNodeWithTag(testTag = DiscoverDefaults.YearTestTag, useUnmergedTree = true)
            .assertExists(errorMessageOnFail = "No Year input component found")
            .assertIsDisplayed()
            .assertTextEquals(year)

        verify(exactly = 1) { viewModel.onYear(year = year) }
    }

    @Test
    fun uiStateClearYearInputTest(): Unit = with(composeRule) {
        val year = "2022"
        val stateFlow = MutableStateFlow(value = DiscoverViewState(year = year))
        val yearSlot = slot<String>()

        every { viewModel.uiState } returns stateFlow
        every { viewModel.onYear(year = capture(lst = yearSlot)) } answers {
            stateFlow.tryEmit(value = DiscoverViewState(year = yearSlot.captured))
        }

        setContent {
            MoviesPotTheme {
                Discover(navigator = navigator, viewModel = viewModel)
            }
        }

        onNodeWithTag(testTag = DiscoverDefaults.YearTestTag, useUnmergedTree = true)
            .assertExists(errorMessageOnFail = "No Year input component found")
            .assertIsDisplayed()
            .assertTextEquals(year)
        onNodeWithTag(testTag = DiscoverDefaults.YearClearTestTag, useUnmergedTree = true)
            .assertExists(errorMessageOnFail = "No Clear input component found")
            .assertIsDisplayed()
            .performClick()
        onNodeWithTag(testTag = DiscoverDefaults.YearTestTag, useUnmergedTree = true)
            .assertExists(errorMessageOnFail = "No Year input component found")
            .assertIsDisplayed()
            .assertTextEquals("")
        onNodeWithTag(testTag = DiscoverDefaults.YearClearTestTag, useUnmergedTree = true)
            .assertDoesNotExist()

        verify(exactly = 1) { viewModel.onYear(year = "") }
    }

    @Test
    fun uiStateSelectGenreTest(): Unit = with(composeRule) {
        val stateFlow = MutableStateFlow(
            value = DiscoverViewState(genresViewState = ViewState.Success(data = TestGenres))
        )
        val genreSlot = slot<Genre>()

        every { viewModel.uiState } returns stateFlow
        every { viewModel.onSelect(genre = capture(lst = genreSlot)) } answers {
            stateFlow.tryEmit(value = stateFlow.value.copy(selectedGenres = listOf(element = genreSlot.captured)))
        }

        setContent {
            MoviesPotTheme {
                Discover(navigator = navigator, viewModel = viewModel)
            }
        }

        onNodeWithTag(testTag = DiscoverDefaults.GenresTestTag, useUnmergedTree = true)
            .assertExists(errorMessageOnFail = "No Genres component found.")
            .assertIsDisplayed()
            .onChildren()
            .onFirst()
            .assertIsNotSelected()
            .performClick()
            .assertIsSelected()

        verify(exactly = 1) { viewModel.onSelect(genre = any()) }
    }

    @Test
    fun uiStateClearSelectionGenreTest(): Unit = with(composeRule) {
        val stateFlow = MutableStateFlow(
            value = DiscoverViewState(
                genresViewState = ViewState.Success(data = TestGenres),
                selectedGenres = TestGenres.subList(fromIndex = 0, toIndex = 1),
            )
        )

        every { viewModel.uiState } returns stateFlow
        every { viewModel.onClearSelection(genre = any()) } answers {
            stateFlow.tryEmit(value = stateFlow.value.copy(selectedGenres = listOf()))
        }

        setContent {
            MoviesPotTheme {
                Discover(navigator = navigator, viewModel = viewModel)
            }
        }

        onNodeWithTag(testTag = DiscoverDefaults.GenresTestTag, useUnmergedTree = true)
            .assertExists(errorMessageOnFail = "No Genres component found.")
            .assertIsDisplayed()
            .onChildren()
            .onFirst()
            .assertIsSelected()
            .performClick()
            .assertIsNotSelected()

        verify(exactly = 1) { viewModel.onClearSelection(genre = any()) }
    }

    @Test
    fun uiStateErrorTest(): Unit = with(composeRule) {
        val error = Exception("Loading failed.")

        every { viewModel.uiState } returns MutableStateFlow(value = DiscoverViewState(error = error))
        justRun { viewModel.onErrorConsumed() }

        setContent {
            MoviesPotTheme {
                Discover(navigator = navigator, viewModel = viewModel)
            }
        }

        onNodeWithTag(testTag = DiscoverDefaults.GenresErrorTestTag, useUnmergedTree = true)
            .assertExists(errorMessageOnFail = "No Error component found")
            .assertIsDisplayed()

        mainClock.advanceTimeBy(milliseconds = 4_000)

        verify(exactly = 1) { viewModel.onErrorConsumed() }
    }

    @Test
    fun previewTest(): Unit = with(composeRule) {
        setContent {
            DiscoverContentPreview()
        }

        onNodeWithTag(testTag = DiscoverDefaults.ContentTestTag, useUnmergedTree = true)
            .assertExists(errorMessageOnFail = "No Content component found.")
            .assertIsDisplayed()
        onNodeWithTag(testTag = DiscoverDefaults.GenresTestTag, useUnmergedTree = true)
            .assertExists(errorMessageOnFail = "No Genres component found.")
            .assertIsDisplayed()
            .onChildren()
            .assertCountEquals(expectedSize = DummyGenres.size)
        onNodeWithTag(testTag = DiscoverDefaults.YearTestTag, useUnmergedTree = true)
            .assertExists(errorMessageOnFail = "No Year input component found")
            .assertIsDisplayed()
            .assertTextEquals("2022")
        onNodeWithTag(testTag = DiscoverDefaults.DiscoverButtonTestTag, useUnmergedTree = true)
            .assertExists(errorMessageOnFail = "No Button back component found.")
            .assertIsDisplayed()
            .assertIsEnabled()
    }
}
