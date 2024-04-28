package com.vlohachov.moviespot.ui.movies.similar

import android.content.Context
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onChildren
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollToIndex
import androidx.paging.LoadState
import androidx.paging.LoadStates
import androidx.paging.PagingData
import androidx.test.core.app.ApplicationProvider
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.vlohachov.moviespot.R
import com.vlohachov.moviespot.data.TestMovies
import com.vlohachov.moviespot.ui.components.button.ScrollToTopDefaults
import com.vlohachov.moviespot.ui.components.movie.MoviesPaginatedGridDefaults
import com.vlohachov.shared.domain.model.movie.Movie
import com.vlohachov.shared.ui.theme.MoviesPotTheme
import io.mockk.every
import io.mockk.justRun
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.flowOf
import org.junit.Rule
import org.junit.Test

class SimilarMoviesTest {

    @get:Rule
    val composeRule = createComposeRule()

    private val navigator = mockk<DestinationsNavigator>()
    private val viewModel = mockk<SimilarMoviesViewModel>()

    private val context = ApplicationProvider.getApplicationContext<Context>()

    @Test
    fun titleTest(): Unit = with(composeRule) {
        val title = "Title"

        every { viewModel.error } returns null
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
                SimilarMovies(
                    navigator = navigator,
                    movieId = 0,
                    movieTitle = title,
                    viewModel = viewModel,
                )
            }
        }

        val titles = onAllNodesWithText(
            text = context.getString(R.string.similar_to, title),
            useUnmergedTree = true,
        )

        titles[0]
            .assertExists(errorMessageOnFail = "No Title small component found.")
            .assertIsDisplayed()

        titles[1]
            .assertExists(errorMessageOnFail = "No Title large component found.")
            .assertIsDisplayed()
    }

    @Test
    fun navigateUpTest(): Unit = with(composeRule) {
        every { viewModel.error } returns null
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
                SimilarMovies(
                    navigator = navigator,
                    movieId = 0,
                    movieTitle = "",
                    viewModel = viewModel,
                )
            }
        }

        onNode(matcher = hasClickAction(), useUnmergedTree = true)
            .assertExists(errorMessageOnFail = "No Button back component found.")
            .assertIsDisplayed()
            .performClick()

        verify(exactly = 1) { navigator.navigateUp() }
    }

    @Test
    fun contentLoadingTest(): Unit = with(composeRule) {
        every { viewModel.error } returns null
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
                SimilarMovies(
                    navigator = navigator,
                    movieId = 0,
                    movieTitle = "",
                    viewModel = viewModel,
                )
            }
        }

        onNodeWithTag(
            testTag = SimilarMoviesDefaults.ContentErrorTestTag,
            useUnmergedTree = true
        ).assertExists(errorMessageOnFail = "No Error component found.")
            .assertIsNotDisplayed()
        onNodeWithTag(
            testTag = MoviesPaginatedGridDefaults.MoviesPaginatedGridTestTag,
            useUnmergedTree = true,
        ).assertExists(errorMessageOnFail = "No Content component found.")
            .assertIsDisplayed()
            .onChildren()
            .assertCountEquals(expectedSize = 0)
        onNodeWithTag(
            testTag = SimilarMoviesDefaults.ContentLoadingTestTag,
            useUnmergedTree = true
        ).assertExists(errorMessageOnFail = "No Progress component found.")
            .assertIsDisplayed()
            .assert(matcher = hasContentDescription(value = true.toString()))
    }

    @Test
    fun contentLoadedTest(): Unit = with(composeRule) {
        every { viewModel.error } returns null
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
                SimilarMovies(
                    navigator = navigator,
                    movieId = 0,
                    movieTitle = "",
                    viewModel = viewModel,
                )
            }
        }

        onNodeWithTag(
            testTag = SimilarMoviesDefaults.ContentErrorTestTag,
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
            testTag = SimilarMoviesDefaults.ContentLoadingTestTag,
            useUnmergedTree = true
        ).assertExists(errorMessageOnFail = "No Progress component found.")
            .assertIsDisplayed()
            .assert(matcher = hasContentDescription(value = false.toString()))
    }

    @Test
    fun contentItemClickTest(): Unit = with(composeRule) {
        every { viewModel.error } returns null
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
                SimilarMovies(
                    navigator = navigator,
                    movieId = 0,
                    movieTitle = "",
                    viewModel = viewModel,
                )
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
                SimilarMovies(
                    navigator = navigator,
                    movieId = 0,
                    movieTitle = "",
                    viewModel = viewModel,
                )
            }
        }

        verify(exactly = 1) { viewModel.onError(error = error) }
    }

    @Test
    fun errorTest(): Unit = with(composeRule) {
        every { viewModel.error } returns Exception("Loading failed.")
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
                SimilarMovies(
                    navigator = navigator,
                    movieId = 0,
                    movieTitle = "",
                    viewModel = viewModel,
                )
            }
        }

        onNodeWithTag(
            testTag = SimilarMoviesDefaults.ContentErrorTestTag,
            useUnmergedTree = true
        ).assertExists(errorMessageOnFail = "No Error component found.")
            .assertIsDisplayed()
        onNodeWithTag(
            testTag = MoviesPaginatedGridDefaults.MoviesPaginatedGridTestTag,
            useUnmergedTree = true,
        ).assertExists(errorMessageOnFail = "No Content component found.")
            .assertIsDisplayed()
            .onChildren()
            .assertCountEquals(expectedSize = 0)
        onNodeWithTag(
            testTag = SimilarMoviesDefaults.ContentLoadingTestTag,
            useUnmergedTree = true
        ).assertExists(errorMessageOnFail = "No Progress component found.")
            .assertIsDisplayed()
            .assert(matcher = hasContentDescription(value = false.toString()))

        mainClock.advanceTimeBy(milliseconds = 4_000)

        verify(exactly = 1) { viewModel.onErrorConsumed() }
    }

    @Test
    fun scrollToTopTest(): Unit = with(composeRule) {
        val largeList = mutableListOf<Movie>()

        repeat(times = 5) { largeList += TestMovies }

        every { viewModel.error } returns null
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
                SimilarMovies(
                    navigator = navigator,
                    movieId = 0,
                    movieTitle = "",
                    viewModel = viewModel,
                )
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
