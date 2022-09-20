package com.vlohachov.moviespot.ui.movies.similar

import app.cash.turbine.test
import com.google.common.truth.Truth
import com.vlohachov.domain.usecase.movie.MovieRecommendationsUseCase
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SimilarMoviesPagerTest {

    private val useCase = mockk<MovieRecommendationsUseCase>()

    private val pager by lazy {
        every { useCase.resultFlow(param = any()) } returns flowOf()

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