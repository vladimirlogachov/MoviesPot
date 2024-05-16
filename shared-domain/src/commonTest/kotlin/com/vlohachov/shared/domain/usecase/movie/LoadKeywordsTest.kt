package com.vlohachov.shared.domain.usecase.movie

import app.cash.turbine.test
import com.vlohachov.shared.domain.Result
import com.vlohachov.shared.domain.data.TestKeywords
import com.vlohachov.shared.domain.model.movie.keyword.Keyword
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

class LoadKeywordsTest {

    private companion object {
        val TestParam = LoadKeywords.Param(id = 0)
    }

    private val repository = mock<MovieRepository>()

    private val useCase = LoadKeywords(repository = repository)

    @Test
    @JsName(name = "result_flow_emits_Loading")
    fun `result flow emits Loading`() = runTest {
        every { repository.getMovieKeywords(id = any()) } returns flowOf(TestKeywords)

        useCase(param = TestParam).test {
            assertIs<Result.Loading>(value = awaitItem())
            skipItems(count = 1)
            awaitComplete()
        }
    }

    @Test
    @JsName(name = "result_flow_emits_Success")
    fun `result flow emits Success`() = runTest {
        every { repository.getMovieKeywords(id = any()) } returns flowOf(TestKeywords)

        useCase(param = TestParam).test {
            assertIs<Result.Success<List<Keyword>>>(value = expectMostRecentItem())
            awaitComplete()
        }
    }

    @Test
    @JsName(name = "result_flow_emits_Error")
    fun `result flow emits Error`() = runTest {
        every { repository.getMovieKeywords(id = any()) } returns flow { throw NullPointerException() }

        useCase(param = TestParam).test {
            assertIs<Result.Error>(value = expectMostRecentItem())
            awaitComplete()
        }
    }

}
