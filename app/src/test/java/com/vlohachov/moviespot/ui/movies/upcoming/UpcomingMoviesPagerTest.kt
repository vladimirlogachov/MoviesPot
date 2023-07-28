package com.vlohachov.moviespot.ui.movies.upcoming

import app.cash.turbine.test
import com.google.common.truth.Truth
import com.vlohachov.domain.usecase.movie.list.UpcomingUseCase
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Test

class UpcomingMoviesPagerTest {

    private val useCase = mockk<UpcomingUseCase>()

    private val pager by lazy {
        every { useCase.resultFlow(param = any()) } returns flowOf()

        UpcomingMoviesPager(useCase = useCase)
    }

    @Test
    fun `pager data flow emits values`() = runTest {
        pager.pagingDataFlow.test {
            val actual = awaitItem()

            Truth.assertThat(actual).isNotNull()
        }
    }
}
