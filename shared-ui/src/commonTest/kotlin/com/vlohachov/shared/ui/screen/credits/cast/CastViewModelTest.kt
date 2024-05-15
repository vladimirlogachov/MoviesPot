package com.vlohachov.shared.ui.screen.credits.cast

import app.cash.turbine.test
import com.vlohachov.shared.TestMovieCredits
import com.vlohachov.shared.core.ViewState
import com.vlohachov.shared.domain.model.movie.credit.CastMember
import com.vlohachov.shared.domain.repository.MovieRepository
import com.vlohachov.shared.domain.usecase.credits.LoadCast
import dev.mokkery.answering.returns
import dev.mokkery.every
import dev.mokkery.matcher.any
import dev.mokkery.mock
import dev.mokkery.resetAnswers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import kotlin.js.JsName
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class CastViewModelTest {

    private val repository = mock<MovieRepository> {
        every {
            getMovieCredits(id = any(), language = any())
        } returns flowOf(value = TestMovieCredits)
    }

    private val viewModel = CastViewModel(
        movieId = 1L,
        loadCast = LoadCast(repository = repository)
    )

    @Test
    @JsName(name = "cast_loading")
    fun `cast loading`() = runTest {
        viewModel.cast.test {
            assertIs<ViewState.Loading>(value = awaitItem())
        }
    }

    @Test
    @JsName(name = "cast_loading_success")
    fun `cast loading success`() = runTest {
        viewModel.cast.test {
            skipItems(count = 1)
            with(receiver = awaitItem()) {
                assertIs<ViewState.Success<List<CastMember>>>(value = this)
                assertEquals(expected = TestMovieCredits.cast, actual = data)
            }
        }
    }

    @Test
    @JsName(name = "on_failure_throws_error")
    fun `on failure throws error`() = runTest {
        resetAnswers(repository)
        every {
            repository.getMovieCredits(id = any(), language = any())
        } returns flow { error(message = "Error") }

        CastViewModel(movieId = 1L, loadCast = LoadCast(repository = repository)).cast.test {
            skipItems(count = 1)
            assertIs<ViewState.Error>(value = awaitItem())
        }
    }

    @Test
    @JsName(name = "error_is_set_and_consumed")
    fun `error is set and consumed`() = runTest {
        viewModel.error.test {
            assertNull(actual = awaitItem())
            viewModel.onError(error = Exception())
            assertNotNull(actual = awaitItem())
            viewModel.onErrorConsumed()
            assertNull(actual = awaitItem())
        }
    }

}
