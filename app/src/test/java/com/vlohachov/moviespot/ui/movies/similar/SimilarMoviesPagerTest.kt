package com.vlohachov.moviespot.ui.movies.similar

import app.cash.turbine.test
import com.google.common.truth.Truth
import com.vlohachov.shared.domain.usecase.movie.LoadRecommendations
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Test

class SimilarMoviesPagerTest {

    private val useCase = mockk<LoadRecommendations>()

    private val pager by lazy {
        every { useCase(param = any()) } returns flowOf()

        SimilarMoviesPager(movieId = 1, useCase = useCase)
    }

    @Test
    fun `pager data flow emits values`() = runTest {
        pager.pagingDataFlow.test {
            val actual = awaitItem()

            Truth.assertThat(actual).isNotNull()
        }
    }
}
