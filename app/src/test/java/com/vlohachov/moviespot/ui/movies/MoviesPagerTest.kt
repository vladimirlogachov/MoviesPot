package com.vlohachov.moviespot.ui.movies

import app.cash.turbine.test
import com.google.common.truth.Truth
import com.vlohachov.domain.model.movie.MovieCategory
import com.vlohachov.domain.usecase.movie.LoadMoviesByCategory
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Test

class MoviesPagerTest {

    private val useCase = mockk<LoadMoviesByCategory>()

    private val pager by lazy {
        every { useCase(param = any()) } returns flowOf()

        MoviesPager(useCase = useCase)
    }

    @Test
    fun `pager data flow emits values`() = runTest {
        pager.pagingDataFlow(category = MovieCategory.UPCOMING).test {
            val actual = awaitItem()

            Truth.assertThat(actual).isNotNull()
        }
    }

}
