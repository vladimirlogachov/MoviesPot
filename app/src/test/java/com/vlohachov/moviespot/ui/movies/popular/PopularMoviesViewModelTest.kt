package com.vlohachov.moviespot.ui.movies.popular

import androidx.paging.LoadState
import androidx.paging.PagingData
import app.cash.turbine.test
import com.google.common.truth.Truth
import com.vlohachov.domain.model.movie.Movie
import com.vlohachov.moviespot.data.PagingDataCollector
import com.vlohachov.moviespot.data.TestMovies
import com.vlohachov.moviespot.data.collector
import com.vlohachov.moviespot.util.TestDispatcherRule
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class PopularMoviesViewModelTest {

    @get:Rule
    val dispatcherRule = TestDispatcherRule(dispatcher = UnconfinedTestDispatcher())

    private val pager = mockk<PopularMoviesPager>()

    private val moviesFlow = MutableStateFlow<PagingData<Movie>>(
        value = PagingData.empty(sourceLoadStates = PagingDataCollector.InitialLoadStates)
    )

    private val viewModel by lazy {
        every { pager.pagingDataFlow } returns moviesFlow

        PopularMoviesViewModel(pager = pager)
    }

    @Test
    fun `movies loading`() = runTest {
        viewModel.movies.test {
            val collector = awaitItem().collector()

            Truth.assertThat(collector.snapshotList.items).isEmpty()
            Truth.assertThat(collector.loadStates.refresh).isEqualTo(LoadState.Loading)
        }
    }

    @Test
    fun `movies loading success`() = runTest {
        viewModel.movies.test {
            skipItems(count = 1)

            moviesFlow.tryEmit(
                value = PagingData.from(
                    data = TestMovies,
                    sourceLoadStates = PagingDataCollector.InitialLoadStates.copy(
                        refresh = LoadState.NotLoading(endOfPaginationReached = true)
                    )
                )
            )

            val collector = awaitItem().collector()

            Truth.assertThat(collector.snapshotList.items).isEqualTo(TestMovies)
            Truth.assertThat(collector.loadStates.refresh)
                .isEqualTo(LoadState.NotLoading(endOfPaginationReached = true))
        }
    }

    @Test
    fun `movies loading error`() = runTest {
        val error = Exception()

        viewModel.movies.test {
            skipItems(count = 1)

            moviesFlow.tryEmit(
                value = PagingData.empty(
                    sourceLoadStates = PagingDataCollector.InitialLoadStates.copy(
                        refresh = LoadState.Error(error = error)
                    )
                )
            )

            val collector = awaitItem().collector()

            Truth.assertThat(collector.snapshotList.items).isEmpty()
            Truth.assertThat(collector.loadStates.refresh).isEqualTo(LoadState.Error(error = error))
        }
    }

    @Test
    fun `error is set and consumed`() = runTest {
        viewModel.onError(error = Exception())

        Truth.assertThat(viewModel.error).isNotNull()

        viewModel.onErrorConsumed()

        Truth.assertThat(viewModel.error).isNull()
    }
}
