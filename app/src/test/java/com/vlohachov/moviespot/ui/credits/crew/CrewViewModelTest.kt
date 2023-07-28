package com.vlohachov.moviespot.ui.credits.crew

import app.cash.turbine.test
import com.google.common.truth.Truth
import com.vlohachov.domain.Result
import com.vlohachov.domain.model.movie.credit.CrewMember
import com.vlohachov.domain.usecase.credits.CrewUseCase
import com.vlohachov.moviespot.core.ViewState
import com.vlohachov.moviespot.data.TestMovieCredits
import com.vlohachov.moviespot.util.TestDispatcherRule
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test

class CrewViewModelTest {

    @get:Rule
    val dispatcherRule = TestDispatcherRule()

    private val crew = mockk<CrewUseCase>()

    private val crewFlow = MutableStateFlow<Result<List<CrewMember>>>(value = Result.Loading)

    private val viewModel by lazy {
        every { crew.resultFlow(param = any()) } returns crewFlow

        CrewViewModel(movieId = 0, crew = crew)
    }

    @Test
    fun `uiState content loading`() = runTest {
        viewModel.uiState.test {
            with(awaitItem()) {
                Truth.assertThat(viewState).isEqualTo(ViewState.Loading)
                Truth.assertThat(error).isNull()
            }
        }
    }

    @Test
    fun `uiState content loading success`() = runTest {
        viewModel.uiState.test {
            skipItems(count = 1)

            crewFlow.tryEmit(value = Result.Success(value = TestMovieCredits.crew))

            with(awaitItem()) {
                Truth.assertThat(viewState is ViewState.Success).isTrue()
                Truth.assertThat(error).isNull()
            }
        }
    }

    @Test
    fun `uiState content loading error`() = runTest {
        viewModel.uiState.test {
            skipItems(count = 1)

            crewFlow.tryEmit(value = Result.Error(exception = Exception()))

            with(awaitItem()) {
                Truth.assertThat(viewState is ViewState.Error).isTrue()
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
