package com.vlohachov.moviespot.ui.main

import app.cash.turbine.test
import com.google.common.truth.Truth
import com.vlohachov.domain.Result
import com.vlohachov.domain.model.PaginatedData
import com.vlohachov.domain.model.movie.Movie
import com.vlohachov.domain.usecase.movie.list.NowPlayingUseCase
import com.vlohachov.domain.usecase.movie.list.PopularUseCase
import com.vlohachov.domain.usecase.movie.list.TopRatedUseCase
import com.vlohachov.domain.usecase.movie.list.UpcomingUseCase
import com.vlohachov.moviespot.core.ViewState
import com.vlohachov.moviespot.data.TestPaginatedData
import com.vlohachov.moviespot.util.TestDispatcherRule
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class MainViewModelTest {

    @get:Rule
    val dispatcherRule = TestDispatcherRule()

    private val upcoming = mockk<UpcomingUseCase>()
    private val nowPlaying = mockk<NowPlayingUseCase>()
    private val popular = mockk<PopularUseCase>()
    private val topRated = mockk<TopRatedUseCase>()

    private val testFlow = MutableStateFlow<Result<PaginatedData<Movie>>>(value = Result.Loading)

    private val viewModel by lazy {
        every { upcoming.resultFlow(param = any()) } returns testFlow
        every { nowPlaying.resultFlow(param = any()) } returns testFlow
        every { popular.resultFlow(param = any()) } returns testFlow
        every { topRated.resultFlow(param = any()) } returns testFlow

        MainViewModel(
            upcoming = upcoming,
            nowPlaying = nowPlaying,
            popular = popular,
            topRated = topRated,
        )
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
