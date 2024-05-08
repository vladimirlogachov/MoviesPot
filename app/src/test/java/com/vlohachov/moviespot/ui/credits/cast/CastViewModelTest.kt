package com.vlohachov.moviespot.ui.credits.cast

import app.cash.turbine.test
import com.google.common.truth.Truth
import com.vlohachov.moviespot.data.TestMovieCredits
import com.vlohachov.moviespot.util.TestDispatcherRule
import com.vlohachov.shared.core.ViewState
import com.vlohachov.shared.domain.Result
import com.vlohachov.shared.domain.model.movie.credit.CastMember
import com.vlohachov.shared.domain.usecase.credits.LoadCast
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test

class CastViewModelTest {

    @get:Rule
    val dispatcherRule = TestDispatcherRule()

    private val cast = mockk<LoadCast>()

    private val castFlow = MutableStateFlow<Result<List<CastMember>>>(value = Result.Loading)

    private val viewModel by lazy {
        every { cast(param = any()) } returns castFlow

        CastViewModel(movieId = 0, loadCast = cast)
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

            castFlow.tryEmit(value = Result.Success(value = TestMovieCredits.cast))

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

            castFlow.tryEmit(value = Result.Error(exception = Exception()))

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
