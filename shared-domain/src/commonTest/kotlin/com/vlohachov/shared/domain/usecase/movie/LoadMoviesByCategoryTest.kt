package com.vlohachov.shared.domain.usecase.movie

import app.cash.turbine.test
import com.vlohachov.shared.domain.Result
import com.vlohachov.shared.domain.data.TestPaginatedData
import com.vlohachov.shared.domain.model.PaginatedData
import com.vlohachov.shared.domain.model.movie.Movie
import com.vlohachov.shared.domain.model.movie.MovieCategory
import com.vlohachov.shared.domain.repository.MovieRepository
import dev.mokkery.answering.returns
import dev.mokkery.every
import dev.mokkery.matcher.any
import dev.mokkery.mock
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import kotlin.js.JsName
import kotlin.test.Test
import kotlin.test.assertIs

class LoadMoviesByCategoryTest {

    private companion object {
        val TestParam = LoadMoviesByCategory.Param(category = MovieCategory.UPCOMING)
    }

    private val repository = mock<MovieRepository>()

    private val useCase = LoadMoviesByCategory(repository = repository)

    @Test
    @JsName("result_flow_emits_Loading")
    fun `result flow emits Loading`() = runTest {
        every {
            repository.getMoviesByCategory(
                category = any(), page = any(),
                language = any(), region = any(),
            )
        } returns flowOf(TestPaginatedData)

        useCase(TestParam).test {
            assertIs<Result.Loading>(value = awaitItem())
            skipItems(count = 1)
            awaitComplete()
        }
    }

    @Test
    @JsName("result_flow_emits_Success")
    fun `result flow emits Success`() = runTest {
        every {
            repository.getMoviesByCategory(
                category = any(), page = any(),
                language = any(), region = any(),
            )
        } returns flowOf(TestPaginatedData)

        useCase(TestParam).test {
            assertIs<Result.Success<PaginatedData<Movie>>>(value = expectMostRecentItem())
            awaitComplete()
        }
    }

    @Test
    @JsName("result_flow_emits_Error")
    fun `result flow emits Error`() = runTest {
        every {
            repository.getMoviesByCategory(
                category = any(), page = any(),
                language = any(), region = any(),
            )
        } returns flow { throw NullPointerException() }

        useCase(param = TestParam).test {
            assertIs<Result.Error>(value = expectMostRecentItem())
            awaitComplete()
        }
    }

}
