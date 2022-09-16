package com.vlohachov.moviespot.ui.keyword

import app.cash.turbine.test
import com.google.common.truth.Truth
import com.vlohachov.domain.usecase.DiscoverMoviesUseCase
import com.vlohachov.moviespot.util.TestDispatcherRule
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class KeywordMoviesPagerTest {

    @get:Rule
    val dispatcherRule = TestDispatcherRule()

    private val useCase = mockk<DiscoverMoviesUseCase>()

    private val pager by lazy {
        every { useCase.resultFlow(param = any()) } returns flowOf()

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