package com.vlohachov.shared.domain.usecase

import app.cash.turbine.test
import com.vlohachov.shared.domain.Result
import com.vlohachov.shared.domain.data.TestPaginatedData
import com.vlohachov.shared.domain.model.PaginatedData
import com.vlohachov.shared.domain.model.movie.Movie
import com.vlohachov.shared.domain.repository.SearchRepository
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

class SearchMoviesTest {

    private companion object {
        val TestParam = SearchMovies.Param(query = "query", page = 1)
    }

    private val repository = mock<SearchRepository>()

    private val useCase = SearchMovies(repository = repository)

    @Test
    @JsName("result_flow_emits_Loading")
    fun `result flow emits Loading`() = runTest {
        every {
            repository.searchMovies(query = any(), page = any(), language = any())
        } returns flowOf(TestPaginatedData)

        useCase(param = TestParam).test {
            assertIs<Result.Loading>(value = awaitItem())
            skipItems(count = 1)
            awaitComplete()
        }
    }

    @Test
    @JsName("result_flow_emits_Success")
    fun `result flow emits Success`() = runTest {
        every {
            repository.searchMovies(query = any(), page = any(), language = any())
        } returns flowOf(TestPaginatedData)

        useCase(param = TestParam).test {
            assertIs<Result.Success<PaginatedData<Movie>>>(value = expectMostRecentItem())
            awaitComplete()

        }
    }

    @Test
    @JsName("result_flow_emits_Error")
    fun `result flow emits Error`() = runTest {
        every {
            repository.searchMovies(query = any(), page = any(), language = any())
        } returns flow { throw NullPointerException() }

        useCase(param = TestParam).test {
            assertIs<Result.Error>(value = expectMostRecentItem())
            awaitComplete()
        }
    }

}
