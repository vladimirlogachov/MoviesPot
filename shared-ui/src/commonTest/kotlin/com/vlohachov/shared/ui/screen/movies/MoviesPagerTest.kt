package com.vlohachov.shared.ui.screen.movies

import app.cash.turbine.test
import com.vlohachov.shared.TestPaginatedData
import com.vlohachov.shared.domain.model.movie.MovieCategory
import com.vlohachov.shared.domain.repository.MovieRepository
import com.vlohachov.shared.domain.usecase.movie.LoadMoviesByCategory
import dev.mokkery.answering.returns
import dev.mokkery.every
import dev.mokkery.matcher.any
import dev.mokkery.mock
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import kotlin.js.JsName
import kotlin.test.Test
import kotlin.test.assertNotNull

class MoviesPagerTest {

    private val repository = mock<MovieRepository> {
        every {
            getMoviesByCategory(category = any(), page = any(), language = any(), region = any())
        } returns flowOf(value = TestPaginatedData)
    }

    private val pager = MoviesPager(useCase = LoadMoviesByCategory(repository = repository))

    @Test
    @JsName(name = "pager_data_flow_emits_values")
    fun `pager data flow emits values`() = runTest {
        pager.pagingDataFlow(category = MovieCategory.UPCOMING).test {
            assertNotNull(actual = awaitItem())
        }
    }

}
