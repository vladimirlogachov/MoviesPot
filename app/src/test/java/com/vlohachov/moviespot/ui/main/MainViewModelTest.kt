package com.vlohachov.moviespot.ui.main

import app.cash.turbine.test
import com.google.common.truth.Truth
import com.vlohachov.domain.repository.MoviesRepository
import com.vlohachov.domain.usecase.movie.list.NowPlayingUseCase
import com.vlohachov.domain.usecase.movie.list.PopularUseCase
import com.vlohachov.domain.usecase.movie.list.TopRatedUseCase
import com.vlohachov.domain.usecase.movie.list.UpcomingUseCase
import com.vlohachov.moviespot.core.ViewState
import com.vlohachov.moviespot.data.TestPaginatedData
import com.vlohachov.moviespot.util.TestDispatcherRule
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class MainViewModelTest {

    @get:Rule
    val dispatcherRule = TestDispatcherRule()

//    private val upcomingFlow = MutableSharedFlow<Result<PaginatedData<Movie>>>(
//        replay = 1,
//        onBufferOverflow = BufferOverflow.DROP_OLDEST,
//    )
//
//    private val nowPlayingFlow = MutableSharedFlow<Result<PaginatedData<Movie>>>(
//        replay = 1,
//        onBufferOverflow = BufferOverflow.DROP_OLDEST,
//    )
//
//    private val popularFlow = MutableSharedFlow<Result<PaginatedData<Movie>>>(
//        replay = 1,
//        onBufferOverflow = BufferOverflow.DROP_OLDEST,
//    )
//
//    private val topRatedFlow = MutableSharedFlow<Result<PaginatedData<Movie>>>(
//        replay = 1,
//        onBufferOverflow = BufferOverflow.DROP_OLDEST,
//    )

//    private val upcoming = mockk<UpcomingUseCase>(relaxed = true)
//    private val nowPlaying = mockk<NowPlayingUseCase>(relaxed = true)
//    private val popular = mockk<PopularUseCase>(relaxed = true)
//    private val topRated = mockk<TopRatedUseCase>(relaxed = true)

    private val repository = mockk<MoviesRepository>(relaxed = true)

    private val upcoming = UpcomingUseCase(
        coroutineContext = Dispatchers.IO,
        repository = repository,
    )
    private val nowPlaying = NowPlayingUseCase(
        coroutineContext = Dispatchers.IO,
        repository = repository,
    )
    private val popular = PopularUseCase(
        coroutineContext = Dispatchers.IO,
        repository = repository,
    )
    private val topRated = TopRatedUseCase(
        coroutineContext = Dispatchers.IO,
        repository = repository,
    )

    private val viewModel = MainViewModel(
        upcoming = upcoming,
        nowPlaying = nowPlaying,
        popular = popular,
        topRated = topRated,
    )

    @Before
    fun setUp() {
        every {
            repository.getUpcomingMovies(
                page = any(),
                language = any(),
                region = any(),
            )
        } returns flowOf(TestPaginatedData)
        every {
            repository.getNowPlayingMovies(
                page = any(),
                language = any(),
                region = any(),
            )
        } returns flowOf(TestPaginatedData)
        every {
            repository.getPopularMovies(
                page = any(),
                language = any(),
                region = any(),
            )
        } returns flowOf(TestPaginatedData)
        every {
            repository.getTopRatedMovies(
                page = any(),
                language = any(),
                region = any(),
            )
        } returns flowOf(TestPaginatedData)
    }

    @Test
    fun `uiState initial Loading`() = runTest {
        viewModel.uiState.test {
            val state = awaitItem()

            Truth.assertThat(state.upcomingViewState).isEqualTo(ViewState.Loading)
            Truth.assertThat(state.nowPlayingViewState).isEqualTo(ViewState.Loading)
            Truth.assertThat(state.popularViewState).isEqualTo(ViewState.Loading)
            Truth.assertThat(state.topRatedViewState).isEqualTo(ViewState.Loading)
            Truth.assertThat(state.error).isNull()

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `uiState nested view states Success`() = runTest {
        val vm = viewModel
        vm.uiState.test {
            val loading = awaitItem()
            Truth.assertThat(loading.popularViewState is ViewState.Loading).isTrue()

            val state = awaitItem()
            Truth.assertThat(state.popularViewState is ViewState.Success).isTrue()
        }
    }
}