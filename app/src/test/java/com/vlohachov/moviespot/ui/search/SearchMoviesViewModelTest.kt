package com.vlohachov.moviespot.ui.search

import androidx.paging.LoadState
import androidx.paging.PagingData
import app.cash.turbine.test
import com.google.common.truth.Truth
import com.vlohachov.moviespot.data.PagingDataCollector
import com.vlohachov.moviespot.data.TestMovies
import com.vlohachov.moviespot.data.collector
import com.vlohachov.moviespot.util.TestDispatcherRule
import com.vlohachov.shared.domain.model.movie.Movie
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SearchMoviesViewModelTest {

    @get:Rule
    val dispatcherRule = TestDispatcherRule(dispatcher = UnconfinedTestDispatcher())

    private val pager = mockk<SearchMoviesPager>()

    private val moviesFlow = MutableStateFlow<PagingData<Movie>>(
        value = PagingData.empty(sourceLoadStates = PagingDataCollector.InitialLoadStates)
    )

    private val viewModel by lazy {
        every { pager.onQuery(query = any()) } just Runs
        every { pager.pagingDataFlow } returns moviesFlow.onEach { data ->
            val appendState = data.collector().loadStates.refresh

            if (appendState is LoadState.Error && appendState.error is IllegalStateException) {
                throw appendState.error
            }
        }

        SearchMoviesViewModel(pager = pager)
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
    fun `search field initial empty`() = runTest {
        viewModel.search.test {
            val actual = awaitItem()

            Truth.assertThat(actual).isEmpty()
        }
    }

    @Test
    fun `search field updated`() = runTest {
        viewModel.search.test {
            skipItems(count = 1)

            val expected = "search"

            viewModel.onSearch(search = expected)

            val actual = awaitItem()

            Truth.assertThat(actual).isEqualTo(expected)

            verify { pager.onQuery(query = any()) }
        }
    }

    @Test
    fun `search field cleared`() = runTest {
        viewModel.search.test {
            skipItems(count = 1)

            viewModel.onSearch(search = "query")

            Truth.assertThat(awaitItem()).isNotEmpty()

            viewModel.onClear()

            Truth.assertThat(awaitItem()).isEmpty()
        }
    }


    @Test
    fun `error is caught from paging data flow`() = runTest {
        val error = IllegalStateException()

        viewModel.movies.test {
            skipItems(count = 1)

            moviesFlow.tryEmit(
                value = PagingData.empty(
                    sourceLoadStates = PagingDataCollector.InitialLoadStates.copy(
                        refresh = LoadState.Error(error = error)
                    )
                )
            )

            Truth.assertThat(viewModel.error).isEqualTo(error)
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
