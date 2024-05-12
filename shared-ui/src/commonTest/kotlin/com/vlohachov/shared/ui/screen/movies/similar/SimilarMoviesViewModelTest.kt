package com.vlohachov.shared.ui.screen.movies.similar

import androidx.paging.testing.asSnapshot
import app.cash.turbine.test
import com.vlohachov.shared.TestPaginatedData
import com.vlohachov.shared.domain.repository.MovieRepository
import com.vlohachov.shared.domain.usecase.movie.LoadRecommendations
import dev.mokkery.answering.returns
import dev.mokkery.answering.throwsErrorWith
import dev.mokkery.every
import dev.mokkery.matcher.any
import dev.mokkery.mock
import dev.mokkery.resetAnswers
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import kotlin.js.JsName
import kotlin.test.Test
import kotlin.test.assertFails
import kotlin.test.assertIs
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.expect

class SimilarMoviesViewModelTest {

    private val repository = mock<MovieRepository> {
        every {
            getMovieRecommendations(id = any(), page = any(), language = any())
        } returns flowOf(value = TestPaginatedData)
    }

    private val pager = SimilarMoviesPager(
        movieId = 1,
        useCase = LoadRecommendations(repository = repository)
    )
    private val viewModel = SimilarMoviesViewModel(pager = pager)

    @Test
    @JsName(name = "on_success_returns_non_empty_data")
    fun `on success returns non empty data`() = runTest {
        viewModel.movies.test {
            expect(expected = TestPaginatedData.data.size) {
                flowOf(awaitItem()).asSnapshot().size
            }
        }
    }

    @Test
    fun `on failure throws error`() = runTest {
        resetAnswers(repository)
        every {
            repository.getMovieRecommendations(id = any(), page = any(), language = any())
        } throwsErrorWith "Error"

        viewModel.movies.test {
            assertIs<IllegalStateException>(
                value = assertFails {
                    flowOf(value = awaitItem()).asSnapshot()
                }
            )
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
