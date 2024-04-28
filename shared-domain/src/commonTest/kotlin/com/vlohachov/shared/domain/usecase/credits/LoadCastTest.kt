package com.vlohachov.shared.domain.usecase.credits

import app.cash.turbine.test
import com.vlohachov.shared.domain.Result
import com.vlohachov.shared.domain.data.TestMovieCredits
import com.vlohachov.shared.domain.model.movie.credit.CastMember
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

class LoadCastTest {

    private companion object {
        val TestParam = LoadCast.Param(id = 0)
    }

    private val repository = mock<MovieRepository>()

    private val useCase = LoadCast(repository = repository)

    @Test
    @JsName("result_flow_emits_Loading")
    fun `result flow emits Loading`() = runTest {
        every {
            repository.getMovieCredits(id = any(), language = any())
        } returns flowOf(TestMovieCredits)

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
            repository.getMovieCredits(id = any(), language = any())
        } returns flowOf(TestMovieCredits)

        useCase(param = TestParam).test {
            assertIs<Result.Success<List<CastMember>>>(value = expectMostRecentItem())
            awaitComplete()
        }
    }

    @Test
    @JsName("result_flow_emits_Error")
    fun `result flow emits Error`() = runTest {
        every {
            repository.getMovieCredits(id = any(), language = any())
        } returns flow { throw NullPointerException() }

        useCase(param = TestParam).test {
            assertIs<Result.Error>(value = expectMostRecentItem())
            awaitComplete()
        }
    }

}
