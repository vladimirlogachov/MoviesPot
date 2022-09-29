package com.vlohachov.moviespot.ui.discover

import app.cash.turbine.test
import com.google.common.truth.Truth
import com.vlohachov.domain.Result
import com.vlohachov.domain.model.genre.Genre
import com.vlohachov.domain.usecase.GenresUseCase
import com.vlohachov.moviespot.core.ViewState
import com.vlohachov.moviespot.data.TestGenre
import com.vlohachov.moviespot.data.TestGenres
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
class DiscoverViewModelTest {

    @get:Rule
    val dispatcherRule = TestDispatcherRule(dispatcher = UnconfinedTestDispatcher())

    private val useCase = mockk<GenresUseCase>()

    private val genresFlow = MutableStateFlow<Result<List<Genre>>>(value = Result.Loading)

    private val viewModel by lazy {
        every { useCase.resultFlow(param = any()) } returns genresFlow

        DiscoverViewModel(useCase = useCase)
    }

    @Test
    fun `uiState initial`() = runTest {
        viewModel.uiState.test {
            val state = awaitItem()

            Truth.assertThat(state.year).isEmpty()
            Truth.assertThat(state.selectedGenres).isEmpty()
            Truth.assertThat(state.genresViewState).isEqualTo(ViewState.Loading)
            Truth.assertThat(state.error).isNull()
        }
    }

    @Test
    fun `uiState year updated`() = runTest {
        viewModel.uiState.test {
            skipItems(count = 1)

            val excepted = "2022"

            viewModel.onYear(year = excepted)

            val actual = awaitItem().year

            Truth.assertThat(actual).isEqualTo(excepted)
        }
    }

    @Test
    fun `uiState year trimmed to valid length`() = runTest {
        viewModel.uiState.test {
            skipItems(count = 1)

            val excepted = 4

            viewModel.onYear(year = "20222")

            val actual = awaitItem().year

            Truth.assertThat(actual.length).isEqualTo(excepted)
        }
    }

    @Test
    fun `uiState genres loading success`() = runTest {
        viewModel.uiState.test {
            skipItems(count = 1)

            genresFlow.tryEmit(value = Result.Success(value = TestGenres))

            val actual = awaitItem().genresViewState

            Truth.assertThat(actual).isEqualTo(ViewState.Success(data = TestGenres))
        }
    }

    @Test
    fun `uiState genres loading error`() = runTest {
        val error = Exception()
        viewModel.uiState.test {
            skipItems(count = 1)

            genresFlow.tryEmit(value = Result.Error(exception = error))

            val actual = awaitItem().genresViewState

            Truth.assertThat(actual).isEqualTo(ViewState.Error(error = error))
        }
    }

    @Test
    fun `uiState genre selected`() = runTest {
        viewModel.uiState.test {
            skipItems(count = 1)

            viewModel.onSelect(genre = TestGenre)

            val actual = awaitItem().selectedGenres

            Truth.assertThat(actual).hasSize(1)
            Truth.assertThat(actual).contains(TestGenre)
        }
    }

    @Test
    fun `uiState genre removed`() = runTest {
        val genre = TestGenre.copy(id = 12)

        viewModel.uiState.test {
            viewModel.onSelect(genre = genre)
            viewModel.onSelect(genre = TestGenre)
            viewModel.onClearSelection(genre = genre)

            skipItems(count = 3)

            val actual = awaitItem().selectedGenres

            Truth.assertThat(actual).hasSize(1)
            Truth.assertThat(actual).contains(TestGenre)
        }
    }

    @Test
    fun `uiState error is set and consumed`() = runTest {
        viewModel.uiState.test {
            skipItems(count = 1)

            viewModel.onError(error = Exception())

            Truth.assertThat(awaitItem().error).isNotNull()

            viewModel.onErrorConsumed()

            Truth.assertThat(awaitItem().error).isNull()
        }
    }
}
