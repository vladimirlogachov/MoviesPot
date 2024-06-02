package com.vlohachov.shared.ui.component.movie

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertAll
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.onChildren
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onLast
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.runComposeUiTest
import androidx.paging.LoadState
import androidx.paging.LoadStates
import androidx.paging.PagingData
import com.vlohachov.shared.TestMovies
import com.vlohachov.shared.core.collectAsLazyPagingItems
import com.vlohachov.shared.domain.model.movie.Movie
import com.vlohachov.shared.ui.component.PosterDefaults
import com.vlohachov.shared.ui.theme.MoviesPotTheme
import kotlinx.coroutines.flow.flowOf
import kotlin.js.JsName
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalTestApi::class)
class MoviesPaginatedGridTest {

    @Test
    @JsName(name = "check_empty")
    fun `check empty`() = runComposeUiTest {
        val dataFlow = flowOf(value = PagingData.empty<Movie>())

        setContent {
            MoviesPotTheme {
                MoviesPaginatedGrid(
                    modifier = Modifier.fillMaxWidth(),
                    columns = GridCells.Fixed(count = 3),
                    movies = dataFlow.collectAsLazyPagingItems(),
                    onError = { },
                    progress = null,
                )
            }
        }

        onNodeWithTag(testTag = MoviesPaginatedGridDefaults.MoviesPaginatedGridTestTag)
            .assertExists(errorMessageOnFail = "No MoviesPaginatedGrid component found.")
            .assertIsDisplayed()
            .onChildren()
            .assertCountEquals(expectedSize = 0)
    }

    @Test
    @JsName(name = "check_refresh")
    fun `check refresh`() = runComposeUiTest {
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
                    onError = { },
                )
            }
        }

        onNodeWithTag(testTag = MoviesPaginatedGridDefaults.MoviesPaginatedGridTestTag)
            .assertExists(errorMessageOnFail = "No MoviesPaginatedGrid component found.")
            .assertIsDisplayed()
            .onChildren()
            .assertCountEquals(expectedSize = 1)
            .onFirst()
            .assert(matcher = hasTestTag(testTag = MoviesPaginatedGridDefaults.LoadingTestTag))
    }

    @Test
    @JsName(name = "check_error")
    fun `check error`() = runComposeUiTest {
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
                    onError = { e -> error = e },
                    progress = null,
                )
            }
        }

        onNodeWithTag(testTag = MoviesPaginatedGridDefaults.MoviesPaginatedGridTestTag)
            .assertExists(errorMessageOnFail = "No MoviesPaginatedGrid component found.")
            .assertIsDisplayed()
            .onChildren()
            .assertCountEquals(expectedSize = 0)

        assertEquals(expected = exception, actual = error)
    }

    @Test
    @JsName(name = "check_not_empty")
    fun `check not empty`() = runComposeUiTest {
        val dataFlow = flowOf(value = PagingData.from(data = TestMovies))

        setContent {
            MoviesPotTheme {
                MoviesPaginatedGrid(
                    modifier = Modifier.fillMaxWidth(),
                    columns = GridCells.Fixed(count = 3),
                    movies = dataFlow.collectAsLazyPagingItems(),
                    onError = { },
                    progress = null,
                )
            }
        }

        onNodeWithTag(testTag = MoviesPaginatedGridDefaults.MoviesPaginatedGridTestTag)
            .assertExists(errorMessageOnFail = "No MoviesPaginatedGrid component found.")
            .assertIsDisplayed()
            .onChildren()
            .assertCountEquals(expectedSize = TestMovies.size)
            .assertAll(matcher = hasTestTag(testTag = PosterDefaults.PosterTestTag))
    }

    @Test
    @JsName(name = "check_append")
    fun `check append`() = runComposeUiTest {
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
                    progress = null,
                )
            }
        }

        onNodeWithTag(testTag = MoviesPaginatedGridDefaults.MoviesPaginatedGridTestTag)
            .assertExists(errorMessageOnFail = "No MoviesPaginatedGrid component found.")
            .assertIsDisplayed()
            .onChildren()
            .assertCountEquals(expectedSize = TestMovies.size + 1)
            .onLast()
            .assert(matcher = hasTestTag(testTag = MoviesPaginatedGridDefaults.LoadingTestTag))
    }

    @Test
    @JsName(name = "check_on_click")
    fun `check on click`() = runComposeUiTest {
        val dataFlow = flowOf(value = PagingData.from(data = TestMovies))
        var movie by mutableStateOf<Movie?>(value = null)

        setContent {
            MoviesPotTheme {
                MoviesPaginatedGrid(
                    modifier = Modifier.fillMaxWidth(),
                    columns = GridCells.Fixed(count = 3),
                    movies = dataFlow.collectAsLazyPagingItems(),
                    onError = { },
                    onClick = { m -> movie = m },
                    progress = null,
                )
            }
        }

        onNodeWithTag(testTag = MoviesPaginatedGridDefaults.MoviesPaginatedGridTestTag)
            .assertExists(errorMessageOnFail = "No MoviesPaginatedGrid component found.")
            .assertIsDisplayed()
            .onChildren()
            .assertCountEquals(expectedSize = TestMovies.size)
            .assertAll(matcher = hasTestTag(testTag = PosterDefaults.PosterTestTag))
            .onFirst()
            .performClick()

        assertEquals(expected = TestMovies.first(), actual = movie)
    }

}
