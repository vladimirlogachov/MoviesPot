package com.vlohachov.moviespot.ui.components.movie

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertAll
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onChildren
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onLast
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.paging.LoadState
import androidx.paging.LoadStates
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import com.google.common.truth.Truth
import com.vlohachov.moviespot.data.TestMovies
import com.vlohachov.shared.domain.model.movie.Movie
import com.vlohachov.shared.ui.component.PosterDefaults
import com.vlohachov.shared.ui.theme.MoviesPotTheme
import kotlinx.coroutines.flow.flowOf
import org.junit.Rule
import org.junit.Test

class MoviesPaginatedGridTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun emptyTest(): Unit = with(composeRule) {
        val dataFlow = flowOf(value = PagingData.empty<Movie>())

        setContent {
            MoviesPotTheme {
                MoviesPaginatedGrid(
                    modifier = Modifier.fillMaxWidth(),
                    columns = GridCells.Fixed(count = 3),
                    movies = dataFlow.collectAsLazyPagingItems(),
                    onError = { },
                )
            }
        }

        onNodeWithTag(
            testTag = MoviesPaginatedGridDefaults.MoviesPaginatedGridTestTag,
            useUnmergedTree = true,
        ).assertExists(errorMessageOnFail = "No MoviesPaginatedGrid component found.")
            .assertIsDisplayed()
            .onChildren()
            .assertCountEquals(expectedSize = 0)
    }

    @Test
    fun refreshTest(): Unit = with(composeRule) {
        val dataFlow = flowOf(
            value = PagingData.empty<Movie>(
                sourceLoadStates = LoadStates(
                    refresh = LoadState.Loading,
                    prepend = LoadState.NotLoading(endOfPaginationReached = true),
                    append = LoadState.NotLoading(endOfPaginationReached = true),
                )
            )
        )

        setContent {
            MoviesPotTheme {
                MoviesPaginatedGrid(
                    modifier = Modifier.fillMaxWidth(),
                    columns = GridCells.Fixed(count = 3),
                    movies = dataFlow.collectAsLazyPagingItems(),
                    progress = {
                        item(span = { GridItemSpan(currentLineSpan = 3) }) {
                            CircularProgressIndicator(
                                modifier = Modifier.semantics {
                                    testTag =
                                        MoviesPaginatedGridDefaults.RefreshProgressTestTag
                                }
                            )
                        }
                    },
                    onError = { },
                )
            }
        }

        onNodeWithTag(
            testTag = MoviesPaginatedGridDefaults.MoviesPaginatedGridTestTag,
            useUnmergedTree = true,
        ).assertExists(errorMessageOnFail = "No MoviesPaginatedGrid component found.")
            .assertIsDisplayed()
            .onChildren()
            .assertCountEquals(expectedSize = 1)
            .onFirst()
            .assert(hasTestTag(testTag = MoviesPaginatedGridDefaults.RefreshProgressTestTag))
    }

    @Test
    fun errorTest(): Unit = with(composeRule) {
        val exception = Exception()
        val dataFlow = flowOf(
            value = PagingData.empty<Movie>(
                sourceLoadStates = LoadStates(
                    refresh = LoadState.Error(error = exception),
                    prepend = LoadState.NotLoading(endOfPaginationReached = true),
                    append = LoadState.NotLoading(endOfPaginationReached = true),
                )
            )
        )
        var error by mutableStateOf<Throwable?>(value = null)

        setContent {
            MoviesPotTheme {
                MoviesPaginatedGrid(
                    modifier = Modifier.fillMaxWidth(),
                    columns = GridCells.Fixed(count = 3),
                    movies = dataFlow.collectAsLazyPagingItems(),
                    progress = {
                        item(span = { GridItemSpan(currentLineSpan = 3) }) {
                            CircularProgressIndicator(
                                modifier = Modifier.semantics {
                                    testTag =
                                        MoviesPaginatedGridDefaults.RefreshProgressTestTag
                                }
                            )
                        }
                    },
                    onError = { e -> error = e },
                )
            }
        }

        onNodeWithTag(
            testTag = MoviesPaginatedGridDefaults.MoviesPaginatedGridTestTag,
            useUnmergedTree = true,
        ).assertExists(errorMessageOnFail = "No MoviesPaginatedGrid component found.")
            .assertIsDisplayed()
            .onChildren()
            .assertCountEquals(expectedSize = 0)

        Truth.assertThat(error).isEqualTo(exception)
    }

    @Test
    fun nonEmptyTest(): Unit = with(composeRule) {
        val dataFlow = flowOf(value = PagingData.from(data = TestMovies))

        setContent {
            MoviesPotTheme {
                MoviesPaginatedGrid(
                    modifier = Modifier.fillMaxWidth(),
                    columns = GridCells.Fixed(count = 3),
                    movies = dataFlow.collectAsLazyPagingItems(),
                    onError = { },
                )
            }
        }

        onNodeWithTag(
            testTag = MoviesPaginatedGridDefaults.MoviesPaginatedGridTestTag,
            useUnmergedTree = true,
        ).assertExists(errorMessageOnFail = "No MoviesPaginatedGrid component found.")
            .assertIsDisplayed()
            .onChildren()
            .assertCountEquals(expectedSize = TestMovies.size)
            .assertAll(hasTestTag(testTag = PosterDefaults.PosterTestTag))
    }

    @Test
    fun appendTest(): Unit = with(composeRule) {
        val dataFlow = flowOf(
            value = PagingData.from(
                data = TestMovies,
                sourceLoadStates = LoadStates(
                    refresh = LoadState.NotLoading(endOfPaginationReached = false),
                    append = LoadState.Loading,
                    prepend = LoadState.NotLoading(endOfPaginationReached = true),
                ),
            )
        )

        setContent {
            MoviesPotTheme {
                MoviesPaginatedGrid(
                    modifier = Modifier.fillMaxWidth(),
                    columns = GridCells.Fixed(count = 3),
                    movies = dataFlow.collectAsLazyPagingItems(),
                    onError = { },
                )
            }
        }

        onNodeWithTag(
            testTag = MoviesPaginatedGridDefaults.MoviesPaginatedGridTestTag,
            useUnmergedTree = true,
        ).assertExists(errorMessageOnFail = "No MoviesPaginatedGrid component found.")
            .assertIsDisplayed()
            .onChildren()
            .assertCountEquals(expectedSize = TestMovies.size + 1)
            .onLast()
            .assert(hasTestTag(testTag = MoviesPaginatedGridDefaults.AppendProgressTestTag))
    }

    @Test
    fun clickableTest(): Unit = with(composeRule) {
        val dataFlow = flowOf(value = PagingData.from(data = TestMovies))
        var movie by mutableStateOf<Movie?>(value = null)

        setContent {
            MoviesPotTheme {
                MoviesPaginatedGrid(
                    modifier = Modifier.fillMaxWidth(),
                    columns = GridCells.Fixed(count = 3),
                    movies = dataFlow.collectAsLazyPagingItems(),
                    onError = { },
                    onClick = { m -> movie = m }
                )
            }
        }

        onNodeWithTag(
            testTag = MoviesPaginatedGridDefaults.MoviesPaginatedGridTestTag,
            useUnmergedTree = true,
        ).assertExists(errorMessageOnFail = "No MoviesPaginatedGrid component found.")
            .assertIsDisplayed()
            .onChildren()
            .assertCountEquals(expectedSize = TestMovies.size)
            .assertAll(hasTestTag(testTag = PosterDefaults.PosterTestTag))
            .onFirst()
            .performClick()

        Truth.assertThat(movie).isEqualTo(TestMovies.first())
    }
}
