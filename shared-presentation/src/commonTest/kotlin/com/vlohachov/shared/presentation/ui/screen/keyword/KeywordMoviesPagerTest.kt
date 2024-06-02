package com.vlohachov.shared.presentation.ui.screen.keyword

import app.cash.turbine.test
import com.vlohachov.shared.domain.repository.DiscoverRepository
import com.vlohachov.shared.domain.usecase.DiscoverMovies
import com.vlohachov.shared.presentation.TestPaginatedData
import dev.mokkery.answering.returns
import dev.mokkery.every
import dev.mokkery.matcher.any
import dev.mokkery.mock
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import kotlin.js.JsName
import kotlin.test.Test
import kotlin.test.assertNotNull

class KeywordMoviesPagerTest {

    private val repository = mock<DiscoverRepository> {
        every {
            discoverMovies(
                page = any(),
                year = any(),
                genres = any(),
                keywords = any(),
                language = any()
            )
        } returns flowOf(value = TestPaginatedData)
    }

    private val pager = KeywordMoviesPager(
        keywordId = 1,
        useCase = DiscoverMovies(repository = repository),
    )

    @Test
    @JsName(name = "pager_data_flow_emits_values")
    fun `pager data flow emits values`() = runTest {
        pager.pagingDataFlow.test {
            assertNotNull(actual = awaitItem())
        }
    }

}
