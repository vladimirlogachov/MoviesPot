package com.vlohachov.moviespot.ui.movies.now

import app.cash.turbine.test
import com.google.common.truth.Truth
import com.vlohachov.domain.usecase.movie.list.NowPlayingUseCase
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Test

class NowPlayingMoviesPagerTest {

    private val useCase = mockk<NowPlayingUseCase>()

    private val pager by lazy {
        every { useCase.resultFlow(param = any()) } returns flowOf()

        NowPlayingMoviesPager(useCase = useCase)
    }

    @Test
    fun `pager data flow emits values`() = runTest {
        pager.pagingDataFlow.test {
            val actual = awaitItem()

            Truth.assertThat(actual).isNotNull()
        }
    }
}
