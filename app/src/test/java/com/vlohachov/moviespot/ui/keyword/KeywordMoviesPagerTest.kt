package com.vlohachov.moviespot.ui.keyword

import app.cash.turbine.test
import com.google.common.truth.Truth
import com.vlohachov.shared.domain.usecase.DiscoverMovies
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Test

class KeywordMoviesPagerTest {

    private val useCase = mockk<DiscoverMovies>()

    private val pager by lazy {
        every { useCase(param = any()) } returns flowOf()

        KeywordMoviesPager(keywordId = 0, useCase = useCase)
    }

    @Test
    fun `pager data flow emits values`() = runTest {
        pager.pagingDataFlow.test {
            val actual = awaitItem()

            Truth.assertThat(actual).isNotNull()
        }
    }
}
