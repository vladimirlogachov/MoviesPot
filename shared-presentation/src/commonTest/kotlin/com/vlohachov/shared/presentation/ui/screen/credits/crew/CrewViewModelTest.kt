package com.vlohachov.shared.presentation.ui.screen.credits.crew

import app.cash.turbine.test
import com.vlohachov.shared.domain.model.movie.credit.CrewMember
import com.vlohachov.shared.domain.repository.MovieRepository
import com.vlohachov.shared.domain.usecase.credits.LoadCast
import com.vlohachov.shared.domain.usecase.credits.LoadCrew
import com.vlohachov.shared.presentation.TestMovieCredits
import com.vlohachov.shared.presentation.core.ViewState
import com.vlohachov.shared.presentation.ui.screen.credits.cast.CastViewModel
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

class CrewViewModelTest {

    private val repository = mock<MovieRepository> {
        every {
            getMovieCredits(id = any(), language = any())
        } returns flowOf(value = TestMovieCredits)
    }

    private val viewModel = CrewViewModel(
        movieId = 1L,
        loadCrew = LoadCrew(repository = repository)
    )

    @Test
    @JsName(name = "crew_loading")
    fun `crew loading`() = runTest {
        viewModel.crew.test {
            assertIs<ViewState.Loading>(value = awaitItem())
        }
    }

    @Test
    @JsName(name = "crew_loading_success")
    fun `crew loading success`() = runTest {
        viewModel.crew.test {
            skipItems(count = 1)
            with(receiver = awaitItem()) {
                assertIs<ViewState.Success<List<CrewMember>>>(value = this)
                assertEquals(expected = TestMovieCredits.crew, actual = data)
            }
        }
    }

    @Test
    @JsName(name = "crew_loading_error")
    fun `crew loading error`() = runTest {
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
