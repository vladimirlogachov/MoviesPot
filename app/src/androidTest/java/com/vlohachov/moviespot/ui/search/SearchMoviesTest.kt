package com.vlohachov.moviespot.ui.search

import android.content.Context
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onChildren
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollToIndex
import androidx.compose.ui.test.performTextInput
import androidx.paging.LoadState
import androidx.paging.LoadStates
import androidx.paging.PagingData
import androidx.test.core.app.ApplicationProvider
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.vlohachov.domain.model.movie.Movie
import com.vlohachov.moviespot.R
import com.vlohachov.moviespot.data.TestMovies
import com.vlohachov.moviespot.ui.components.bar.AppBarDefaults
import com.vlohachov.moviespot.ui.components.button.ScrollToTopDefaults
import com.vlohachov.moviespot.ui.components.movie.MoviesPaginatedGridDefaults
import com.vlohachov.shared.ui.theme.MoviesPotTheme
import io.mockk.every
import io.mockk.justRun
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import org.junit.Rule
import org.junit.Test

@ExperimentalComposeUiApi
@ExperimentalMaterial3Api
class SearchMoviesTest {

    @get:Rule
    val composeRule = createComposeRule()

    private val navigator = mockk<DestinationsNavigator>()
    private val viewModel = mockk<SearchMoviesViewModel>()

    private val context = ApplicationProvider.getApplicationContext<Context>()

    @Test
    fun titleTest(): Unit = with(composeRule) {
        every { viewModel.error } returns null
        every { viewModel.search } returns flowOf(value = "")
        every { viewModel.movies } returns flowOf(
            value = PagingData.empty(
                sourceLoadStates = LoadStates(
                    refresh = LoadState.NotLoading(endOfPaginationReached = true),
                    append = LoadState.NotLoading(endOfPaginationReached = true),
                    prepend = LoadState.NotLoading(endOfPaginationReached = true),
                )
            )
        )

        setContent {
            MoviesPotTheme {
                SearchMovies(navigator = navigator, viewModel = viewModel)
            }
        }

        onNodeWithText(text = context.getString(R.string.search), useUnmergedTree = true)
            .assertExists(errorMessageOnFail = "No Title component found.")
            .assertIsDisplayed()
    }

    @Test
    fun navigateUpTest(): Unit = with(composeRule) {
        every { viewModel.error } returns null
        every { viewModel.search } returns flowOf(value = "")
        every { viewModel.movies } returns flowOf(
            value = PagingData.empty(
                sourceLoadStates = LoadStates(
                    refresh = LoadState.NotLoading(endOfPaginationReached = true),
                    append = LoadState.NotLoading(endOfPaginationReached = true),
                    prepend = LoadState.NotLoading(endOfPaginationReached = true),
                )
            )
        )
        every { navigator.navigateUp() } returns true

        setContent {
            MoviesPotTheme {
                SearchMovies(navigator = navigator, viewModel = viewModel)
            }
        }

        onNodeWithTag(testTag = AppBarDefaults.BackButtonTestTag, useUnmergedTree = true)
            .assertExists(errorMessageOnFail = "No Button back component found.")
            .assertIsDisplayed()
            .performClick()

        verify(exactly = 1) { navigator.navigateUp() }
    }

    @Test
    fun searchTest(): Unit = with(composeRule) {
        val searchFlow = MutableStateFlow(value = "")
        val searchSlot = slot<String>()

        every { viewModel.error } returns null
        every { viewModel.search } returns searchFlow
        every { viewModel.movies } returns flowOf(
            value = PagingData.empty(
                sourceLoadStates = LoadStates(
                    refresh = LoadState.NotLoading(endOfPaginationReached = true),
                    append = LoadState.NotLoading(endOfPaginationReached = true),
                    prepend = LoadState.NotLoading(endOfPaginationReached = true),
                )
            )
        )
        every { viewModel.onSearch(search = capture(lst = searchSlot)) } answers {
            searchFlow.tryEmit(value = searchSlot.captured)
        }

        setContent {
            MoviesPotTheme {
                SearchMovies(navigator = navigator, viewModel = viewModel)
            }
        }

        onNodeWithTag(
            testTag = SearchMoviesDefaults.ClearSearchFieldTestTag,
            useUnmergedTree = true,
        ).assertDoesNotExist()
        onNodeWithTag(testTag = SearchMoviesDefaults.SearchFieldTestTag, useUnmergedTree = true)
            .assertExists(errorMessageOnFail = "No Search input component found.")
            .assertIsDisplayed()
            .performTextInput(text = "search")
        onNodeWithTag(
            testTag = SearchMoviesDefaults.ClearSearchFieldTestTag,
            useUnmergedTree = true,
        ).assertExists(errorMessageOnFail = "No Search input component found.")
            .assertIsDisplayed()

        verify(exactly = 1) { viewModel.onSearch(search = any()) }
    }

    @Test
    fun clearSearchTest(): Unit = with(composeRule) {
        val search = "search"
        val searchFlow = MutableStateFlow(value = search)

        every { viewModel.error } returns null
        every { viewModel.search } returns searchFlow
        every { viewModel.movies } returns flowOf(
            value = PagingData.empty(
                sourceLoadStates = LoadStates(
                    refresh = LoadState.NotLoading(endOfPaginationReached = true),
                    append = LoadState.NotLoading(endOfPaginationReached = true),
                    prepend = LoadState.NotLoading(endOfPaginationReached = true),
                )
            )
        )
        every { viewModel.onClear() } answers {
            searchFlow.tryEmit(value = "")
        }

        setContent {
            MoviesPotTheme {
                SearchMovies(navigator = navigator, viewModel = viewModel)
            }
        }

        onNodeWithTag(testTag = SearchMoviesDefaults.SearchFieldTestTag, useUnmergedTree = true)
            .assertExists(errorMessageOnFail = "No Search input component found.")
            .assertIsDisplayed()
            .assertTextEquals(search)
        onNodeWithTag(
            testTag = SearchMoviesDefaults.ClearSearchFieldTestTag,
            useUnmergedTree = true,
        ).assertExists(errorMessageOnFail = "No Search input component found.")
            .assertIsDisplayed()
            .performClick()
        onNodeWithTag(testTag = SearchMoviesDefaults.SearchFieldTestTag, useUnmergedTree = true)
            .assertExists(errorMessageOnFail = "No Search input component found.")
            .assertIsDisplayed()
            .assertTextEquals("")
        onNodeWithTag(
            testTag = SearchMoviesDefaults.ClearSearchFieldTestTag,
            useUnmergedTree = true,
        ).assertDoesNotExist()

        verify(exactly = 1) { viewModel.onClear() }
    }

    @Test
    fun contentLoadingTest(): Unit = with(composeRule) {
        every { viewModel.error } returns null
        every { viewModel.search } returns flowOf(value = "")
        every { viewModel.movies } returns flowOf(
            value = PagingData.empty(
                sourceLoadStates = LoadStates(
                    refresh = LoadState.Loading,
                    append = LoadState.NotLoading(endOfPaginationReached = true),
                    prepend = LoadState.NotLoading(endOfPaginationReached = true),
                )
            )
        )

        setContent {
            MoviesPotTheme {
                SearchMovies(navigator = navigator, viewModel = viewModel)
            }
        }

        onNodeWithTag(
            testTag = SearchMoviesDefaults.ContentErrorTestTag,
            useUnmergedTree = true
        ).assertExists(errorMessageOnFail = "No Error component found.")
            .assertIsNotDisplayed()
        onNodeWithTag(
            testTag = MoviesPaginatedGridDefaults.MoviesPaginatedGridTestTag,
            useUnmergedTree = true,
        ).assertExists(errorMessageOnFail = "No Content component found.")
            .assertIsDisplayed()
            .onChildren()
            .assertCountEquals(expectedSize = 1)
            .onFirst()
            .assertIsDisplayed()
        onNodeWithTag(
            testTag = SearchMoviesDefaults.ContentLoadingTestTag,
            useUnmergedTree = true
        ).assertExists(errorMessageOnFail = "No Progress component found.")
            .assertIsDisplayed()
    }

    @Test
    fun contentLoadedTest(): Unit = with(composeRule) {
        every { viewModel.error } returns null
        every { viewModel.search } returns flowOf(value = "")
        every { viewModel.movies } returns flowOf(
            value = PagingData.from(
                data = TestMovies,
                sourceLoadStates = LoadStates(
                    refresh = LoadState.NotLoading(endOfPaginationReached = true),
                    append = LoadState.NotLoading(endOfPaginationReached = true),
                    prepend = LoadState.NotLoading(endOfPaginationReached = true),
                ),
            )
        )

        setContent {
            MoviesPotTheme {
                SearchMovies(navigator = navigator, viewModel = viewModel)
            }
        }

        onNodeWithTag(
            testTag = SearchMoviesDefaults.ContentErrorTestTag,
            useUnmergedTree = true
        ).assertExists(errorMessageOnFail = "No Error component found.")
            .assertIsNotDisplayed()
        onNodeWithTag(
            testTag = MoviesPaginatedGridDefaults.MoviesPaginatedGridTestTag,
            useUnmergedTree = true,
        ).assertExists(errorMessageOnFail = "No Content component found.")
            .assertIsDisplayed()
            .onChildren()
            .assertCountEquals(expectedSize = TestMovies.size)
        onNodeWithTag(
            testTag = SearchMoviesDefaults.ContentLoadingTestTag,
            useUnmergedTree = true
        ).assertDoesNotExist()
    }

    @Test
    fun contentItemClickTest(): Unit = with(composeRule) {
        every { viewModel.error } returns null
        every { viewModel.search } returns flowOf(value = "")
        every { viewModel.movies } returns flowOf(
            value = PagingData.from(
                data = TestMovies,
                sourceLoadStates = LoadStates(
                    refresh = LoadState.NotLoading(endOfPaginationReached = true),
                    append = LoadState.NotLoading(endOfPaginationReached = true),
                    prepend = LoadState.NotLoading(endOfPaginationReached = true),
                ),
            )
        )
        justRun { navigator.navigate(direction = any()) }

        setContent {
            MoviesPotTheme {
                SearchMovies(navigator = navigator, viewModel = viewModel)
            }
        }

        onNodeWithTag(
            testTag = MoviesPaginatedGridDefaults.MoviesPaginatedGridTestTag,
            useUnmergedTree = true,
        ).assertExists(errorMessageOnFail = "No Content component found.")
            .assertIsDisplayed()
            .onChildren()
            .onFirst()
            .assertHasClickAction()
            .performClick()

        verify(exactly = 1) { navigator.navigate(direction = any()) }
    }

    @Test
    fun contentErrorTest(): Unit = with(composeRule) {
        val error = Exception("Loading failed.")

        every { viewModel.error } returns null
        every { viewModel.search } returns flowOf(value = "")
        every { viewModel.movies } returns flowOf(
            value = PagingData.empty(
                sourceLoadStates = LoadStates(
                    refresh = LoadState.Error(error = error),
                    append = LoadState.NotLoading(endOfPaginationReached = true),
                    prepend = LoadState.NotLoading(endOfPaginationReached = true),
                )
            )
        )
        justRun { viewModel.onError(error = any()) }

        setContent {
            MoviesPotTheme {
                SearchMovies(navigator = navigator, viewModel = viewModel)
            }
        }

        verify(exactly = 1) { viewModel.onError(error = error) }
    }

    @Test
    fun errorTest(): Unit = with(composeRule) {
        every { viewModel.error } returns Exception("Loading failed.")
        every { viewModel.search } returns flowOf(value = "")
        every { viewModel.movies } returns flowOf(
            value = PagingData.empty(
                sourceLoadStates = LoadStates(
                    refresh = LoadState.NotLoading(endOfPaginationReached = true),
                    append = LoadState.NotLoading(endOfPaginationReached = true),
                    prepend = LoadState.NotLoading(endOfPaginationReached = true),
                )
            )
        )
        justRun { viewModel.onErrorConsumed() }

        setContent {
            MoviesPotTheme {
                SearchMovies(navigator = navigator, viewModel = viewModel)
            }
        }

        onNodeWithTag(
            testTag = SearchMoviesDefaults.ContentErrorTestTag,
            useUnmergedTree = true
        ).assertExists(errorMessageOnFail = "No Error component found.")
            .assertIsDisplayed()
        onNodeWithTag(
            testTag = MoviesPaginatedGridDefaults.MoviesPaginatedGridTestTag,
            useUnmergedTree = true,
        ).assertExists(errorMessageOnFail = "No Content component found.")
            .assertIsDisplayed()
            .onChildren()
            .assertCountEquals(expectedSize = 1)
            .onFirst()
            .assertIsNotDisplayed()
        onNodeWithTag(
            testTag = SearchMoviesDefaults.ContentLoadingTestTag,
            useUnmergedTree = true
        ).assertDoesNotExist()

        mainClock.advanceTimeBy(milliseconds = 4_000)

        verify(exactly = 1) { viewModel.onErrorConsumed() }
    }

    @Test
    fun scrollToTopTest(): Unit = with(composeRule) {
        val largeList = mutableListOf<Movie>()

        repeat(times = 5) { largeList += TestMovies }

        every { viewModel.error } returns null
        every { viewModel.search } returns flowOf(value = "")
        every { viewModel.movies } returns flowOf(
            value = PagingData.from(
                data = largeList,
                sourceLoadStates = LoadStates(
                    refresh = LoadState.NotLoading(endOfPaginationReached = true),
                    append = LoadState.NotLoading(endOfPaginationReached = true),
                    prepend = LoadState.NotLoading(endOfPaginationReached = true),
                ),
            )
        )

        setContent {
            MoviesPotTheme {
                SearchMovies(navigator = navigator, viewModel = viewModel)
            }
        }

        onNodeWithTag(
            testTag = MoviesPaginatedGridDefaults.MoviesPaginatedGridTestTag,
            useUnmergedTree = true,
        ).assertExists(errorMessageOnFail = "No Content component found.")
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
