package com.vlohachov.moviespot.ui.movies.popular

import app.cash.turbine.test
import com.google.common.truth.Truth
import com.vlohachov.domain.usecase.movie.list.PopularUseCase
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Test

class PopularMoviesPagerTest {

    private val useCase = mockk<PopularUseCase>()

    private val pager by lazy {
        every { useCase.resultFlow(param = any()) } returns flowOf()

        PopularMoviesPager(useCase = useCase)
    }

    @Test
    fun `pager data flow emits values`() = runTest {
        pager.pagingDataFlow.test {
            val actual = awaitItem()

            Truth.assertThat(actual).isNotNull()
        }
    }
}
