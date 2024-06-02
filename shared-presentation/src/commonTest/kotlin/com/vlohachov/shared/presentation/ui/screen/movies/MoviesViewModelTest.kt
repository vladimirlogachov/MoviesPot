package com.vlohachov.shared.presentation.ui.screen.movies

import androidx.paging.testing.asSnapshot
import app.cash.turbine.test
import com.vlohachov.shared.domain.model.movie.MovieCategory
import com.vlohachov.shared.domain.repository.MovieRepository
import com.vlohachov.shared.domain.usecase.movie.LoadMoviesByCategory
import com.vlohachov.shared.presentation.TestPaginatedData
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

class MoviesViewModelTest {

    private val repository = mock<MovieRepository> {
        every {
            getMoviesByCategory(category = any(), page = any(), language = any(), region = any())
        } returns flowOf(value = TestPaginatedData)
    }

    private val pager = MoviesPager(useCase = LoadMoviesByCategory(repository = repository))
    private val viewModel = MoviesViewModel(category = MovieCategory.POPULAR, pager = pager)

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
    @JsName(name = "on_failure_throws_error")
    fun `on failure throws error`() = runTest {
        resetAnswers(repository)
        every {
            repository.getMoviesByCategory(
                category = any(),
                page = any(),
                language = any(),
                region = any()
            )
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
