package com.vlohachov.moviespot.ui.main

import app.cash.turbine.test
import com.google.common.truth.Truth
import com.vlohachov.moviespot.data.TestPaginatedData
import com.vlohachov.moviespot.util.TestDispatcherRule
import com.vlohachov.shared.domain.Result
import com.vlohachov.shared.domain.model.PaginatedData
import com.vlohachov.shared.domain.model.movie.Movie
import com.vlohachov.shared.domain.usecase.movie.LoadMoviesByCategory
import com.vlohachov.shared.core.ViewState
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test

class MainViewModelTest {

    @get:Rule
    val dispatcherRule = TestDispatcherRule()

    private val loadMoviesByCategory = mockk<LoadMoviesByCategory>()

    private val testFlow = MutableStateFlow<Result<PaginatedData<Movie>>>(value = Result.Loading)

    private val viewModel by lazy {
        every { loadMoviesByCategory(param = any()) } returns testFlow

        MainViewModel(loadMoviesByCategory)
    }

    @Test
    fun `uiState content loading`() = runTest {
        viewModel.uiState.test {
            with(awaitItem()) {
                Truth.assertThat(upcomingViewState).isEqualTo(ViewState.Loading)
                Truth.assertThat(nowPlayingViewState).isEqualTo(ViewState.Loading)
                Truth.assertThat(popularViewState).isEqualTo(ViewState.Loading)
                Truth.assertThat(topRatedViewState).isEqualTo(ViewState.Loading)
                Truth.assertThat(error).isNull()
            }
        }
    }

    @Test
    fun `uiState content loading success`() = runTest {
        viewModel.uiState.test {
            skipItems(count = 1)

            testFlow.tryEmit(value = Result.Success(value = TestPaginatedData))

            with(awaitItem()) {
                Truth.assertThat(upcomingViewState is ViewState.Success).isTrue()
                Truth.assertThat(nowPlayingViewState is ViewState.Success).isTrue()
                Truth.assertThat(popularViewState is ViewState.Success).isTrue()
                Truth.assertThat(topRatedViewState is ViewState.Success).isTrue()
                Truth.assertThat(error).isNull()
            }
        }
    }

    @Test
    fun `uiState content loading error`() = runTest {
        viewModel.uiState.test {
            skipItems(count = 1)

            testFlow.tryEmit(value = Result.Error(exception = Exception()))

            with(awaitItem()) {
                Truth.assertThat(upcomingViewState is ViewState.Error).isTrue()
                Truth.assertThat(nowPlayingViewState is ViewState.Error).isTrue()
                Truth.assertThat(popularViewState is ViewState.Error).isTrue()
                Truth.assertThat(topRatedViewState is ViewState.Error).isTrue()
                Truth.assertThat(error).isNull()
            }
        }
    }

    @Test
    fun `uiState common error consumed`() = runTest {
        viewModel.uiState.test {
            skipItems(count = 1)

            viewModel.onError(error = Exception())

            Truth.assertThat(awaitItem().error).isNotNull()

            viewModel.onErrorConsumed()

            Truth.assertThat(awaitItem().error).isNull()
        }
    }
}
