package com.vlohachov.shared.domain.usecase

import app.cash.turbine.test
import com.vlohachov.shared.domain.Result
import com.vlohachov.shared.domain.data.TestGenres
import com.vlohachov.shared.domain.model.genre.Genre
import com.vlohachov.shared.domain.repository.GenreRepository
import dev.mokkery.answering.returns
import dev.mokkery.every
import dev.mokkery.matcher.any
import dev.mokkery.mock
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import kotlin.js.JsName
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class LoadGenresTest {

    private companion object {
        val TestParam = LoadGenres.Param()
    }

    private val repository = mock<GenreRepository>()

    private val useCase = LoadGenres(repository = repository)

    @Test
    @JsName("result_flow_emits_Loading")
    fun `result flow emits Loading`() = runTest {
        every { repository.getGenres(language = any()) } returns flowOf(TestGenres)

        useCase(param = TestParam).test {
            assertIs<Result.Loading>(value = awaitItem())
            skipItems(count = 1)
            awaitComplete()
        }
    }

    @Test
    @JsName("result_flow_emits_Success_with_all_genres")
    fun `result flow emits Success with all genres`() = runTest {
        every { repository.getGenres(language = any()) } returns flowOf(TestGenres)

        useCase(param = TestParam).test {
            assertIs<Result.Success<List<Genre>>>(value = expectMostRecentItem())
            awaitComplete()
        }
    }

    @Test
    @JsName("result_flow_emits_Success_with_n_genres")
    fun `result flow emits Success with n genres`() = runTest {
        val genresToTake = 1

        every { repository.getGenres(language = any()) } returns flowOf(TestGenres)

        useCase(param = TestParam.copy(count = genresToTake)).test {
            with(expectMostRecentItem()) {
                assertIs<Result.Success<List<Genre>>>(value = this)
                assertEquals(genresToTake, value.size)
            }
            awaitComplete()
        }
    }

    @Test
    @JsName("result_flow_emits_Error")
    fun `result flow emits Error`() = runTest {
        every { repository.getGenres(language = any()) } returns flow { throw NullPointerException() }

        useCase(param = TestParam).test {
            assertIs<Result.Error>(value = expectMostRecentItem())
            awaitComplete()
        }
    }

}
