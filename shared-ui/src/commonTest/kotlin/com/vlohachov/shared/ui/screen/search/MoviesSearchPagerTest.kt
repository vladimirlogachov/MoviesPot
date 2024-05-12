package com.vlohachov.shared.ui.screen.search

import app.cash.turbine.test
import com.vlohachov.shared.TestPaginatedData
import com.vlohachov.shared.domain.repository.SearchRepository
import com.vlohachov.shared.domain.usecase.SearchMovies
import dev.mokkery.answering.returns
import dev.mokkery.every
import dev.mokkery.matcher.any
import dev.mokkery.mock
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import kotlin.js.JsName
import kotlin.test.Test
import kotlin.test.assertNotNull

class MoviesSearchPagerTest {

    private val repository = mock<SearchRepository> {
        every {
            searchMovies(query = any(), page = any(), language = any())
        } returns flowOf(value = TestPaginatedData)
    }

    private val pager = MoviesSearchPager(useCase = SearchMovies(repository = repository))

    @Test
    @JsName(name = "pager_data_flow_emits_values")
    fun `pager data flow emits values`() = runTest {
        pager.pagingDataFlow.test {
            assertNotNull(actual = awaitItem())
        }
    }

    @Test
    @JsName(name = "pager_data_flow_emits_values_on_query_update")
    fun `pager data flow emits values on query update`() = runTest {
        pager.pagingDataFlow.test {
            skipItems(count = 1)
            pager.onQuery(query = "query")
            assertNotNull(actual = awaitItem())
        }
    }

}
