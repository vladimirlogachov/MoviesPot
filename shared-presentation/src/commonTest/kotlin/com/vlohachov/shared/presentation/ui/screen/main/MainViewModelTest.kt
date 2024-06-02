package com.vlohachov.shared.presentation.ui.screen.main

import app.cash.turbine.test
import com.vlohachov.shared.domain.model.movie.Movie
import com.vlohachov.shared.domain.repository.MovieRepository
import com.vlohachov.shared.domain.usecase.movie.LoadMoviesByCategory
import com.vlohachov.shared.presentation.TestPaginatedData
import com.vlohachov.shared.presentation.core.ViewState
import dev.mokkery.answering.returns
import dev.mokkery.every
import dev.mokkery.matcher.any
import dev.mokkery.mock
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import kotlin.js.JsName
import kotlin.test.Test
import kotlin.test.assertIs
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class MainViewModelTest {

    private val repository = mock<MovieRepository>()

    private val loadMoviesByCategory = LoadMoviesByCategory(repository = repository)

    @Test
    @JsName(name = "movies loading")
    fun `movies loading`() = runTest {
        every {
            repository.getMoviesByCategory(
                category = any(),
                page = any(),
                language = any(),
                region = any()
            )
        } returns flowOf()

        MainViewModel(loadMoviesByCategory = loadMoviesByCategory).uiState.test {
            awaitItem().validateLoading()
        }
    }

    @Test
    @JsName(name = "movies_loading_success")
    fun `movies loading success`() = runTest {
        every {
            repository.getMoviesByCategory(
                category = any(),
                page = any(),
                language = any(),
                region = any()
            )
        } returns flowOf(value = TestPaginatedData)

        MainViewModel(loadMoviesByCategory = loadMoviesByCategory).uiState.test {
            skipItems(count = 4)
            awaitItem().validateSuccess()
        }
    }

    @Test
    @JsName(name = "movies_loading_error")
    fun `movies loading error`() = runTest {
        every {
            repository.getMoviesByCategory(
                category = any(),
                page = any(),
                language = any(),
                region = any()
            )
        } returns flow { error(message = "Error") }

        MainViewModel(loadMoviesByCategory = loadMoviesByCategory).uiState.test {
            skipItems(count = 4)
            awaitItem().validateError()
        }
    }

    @Test
    @JsName(name = "error_is_set_and_consumed")
    fun `error is set and consumed`() = runTest {
        every {
            repository.getMoviesByCategory(
                category = any(),
                page = any(),
                language = any(),
                region = any()
            )
        } returns flowOf()

        MainViewModel(loadMoviesByCategory = loadMoviesByCategory).run {
            error.test {
                assertNull(actual = awaitItem())
                onError(error = Exception())
                assertNotNull(actual = awaitItem())
                onErrorConsumed()
                assertNull(actual = awaitItem())
            }
        }
    }

    private fun MainViewState.validateLoading() {
        assertIs<ViewState.Loading>(
            value = upcomingViewState,
            message = "UpcomingState"
        )
        assertIs<ViewState.Loading>(
            value = nowPlayingViewState,
            message = "NowPlayingState"
        )
        assertIs<ViewState.Loading>(
            value = popularViewState,
            message = "PopularState"
        )
        assertIs<ViewState.Loading>(
            value = topRatedViewState,
            message = "TopRatedState"
        )
        assertNull(actual = error)
    }

    private fun MainViewState.validateSuccess() {
        assertIs<ViewState.Success<List<Movie>>>(
            value = upcomingViewState,
            message = "UpcomingState"
        )
        assertIs<ViewState.Success<List<Movie>>>(
            value = nowPlayingViewState,
            message = "NowPlayingState"
        )
        assertIs<ViewState.Success<List<Movie>>>(
            value = popularViewState,
            message = "PopularState"
        )
        assertIs<ViewState.Success<List<Movie>>>(
            value = topRatedViewState,
            message = "TopRatedState"
        )
        assertNull(actual = error)
    }

    private fun MainViewState.validateError() {
        assertIs<ViewState.Error>(
            value = upcomingViewState,
            message = "UpcomingState"
        )
        assertIs<ViewState.Error>(
            value = nowPlayingViewState,
            message = "NowPlayingState"
        )
        assertIs<ViewState.Error>(
            value = popularViewState,
            message = "PopularState"
        )
        assertIs<ViewState.Error>(
            value = topRatedViewState,
            message = "TopRatedState"
        )
        assertNotNull(actual = error)
    }

}
