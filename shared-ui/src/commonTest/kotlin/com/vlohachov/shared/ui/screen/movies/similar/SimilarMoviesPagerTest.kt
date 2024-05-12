package com.vlohachov.shared.ui.screen.movies.similar

import app.cash.turbine.test
import com.vlohachov.shared.TestPaginatedData
import com.vlohachov.shared.domain.repository.MovieRepository
import com.vlohachov.shared.domain.usecase.movie.LoadRecommendations
import dev.mokkery.answering.returns
import dev.mokkery.every
import dev.mokkery.matcher.any
import dev.mokkery.mock
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import kotlin.js.JsName
import kotlin.test.Test
import kotlin.test.assertNotNull

class SimilarMoviesPagerTest {

    private val repository = mock<MovieRepository> {
        every {
            getMovieRecommendations(id = any(), page = any(), language = any())
        } returns flowOf(value = TestPaginatedData)
    }

    private val pager = SimilarMoviesPager(
        movieId = 1L,
        useCase = LoadRecommendations(repository = repository)
    )

    @Test
    @JsName(name = "pager_data_flow_emits_values")
    fun `pager data flow emits values`() = runTest {
        pager.pagingDataFlow.test {
            assertNotNull(actual = awaitItem())
        }
    }

}
