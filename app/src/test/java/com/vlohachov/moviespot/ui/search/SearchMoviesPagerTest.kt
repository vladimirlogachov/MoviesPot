package com.vlohachov.moviespot.ui.search

import app.cash.turbine.test
import com.google.common.truth.Truth
import com.vlohachov.domain.usecase.SearchMoviesUseCase
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SearchMoviesPagerTest {

    private val useCase = mockk<SearchMoviesUseCase>()

    private val pager by lazy {
        every { useCase.resultFlow(param = any()) } returns flowOf()

        SearchMoviesPager(useCase = useCase)
    }

    @Test
    fun `pager data flow emits values`() = runTest {
        pager.pagingDataFlow.test {
            val actual = awaitItem()

            Truth.assertThat(actual).isNotNull()
        }
    }

    @Test
    fun `pager data flow emits values on query update`() = runTest {
        pager.pagingDataFlow.test {
            skipItems(count = 1)

            pager.onQuery(query = "query")

            val actual = awaitItem()

            Truth.assertThat(actual).isNotNull()
        }
    }
}
