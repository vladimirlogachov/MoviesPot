package com.vlohachov.shared.data.repository

import app.cash.turbine.test
import com.vlohachov.shared.data.scheme.movie.toDomain
import com.vlohachov.shared.data.utils.MoviesTestPaginatedScheme
import com.vlohachov.shared.data.utils.mockClientFailure
import com.vlohachov.shared.data.utils.mockClientSuccess
import kotlinx.coroutines.test.runTest
import kotlin.js.JsName
import kotlin.test.Test
import kotlin.test.assertEquals

class RemoteSearchRepositoryTest {

    @Test
    @JsName("search_movies_success")
    fun `search movies success`() = runTest {
        RemoteSearchRepository(client = mockClientSuccess(data = MoviesTestPaginatedScheme))
            .searchMovies(page = 1, query = "", language = null)
            .test {
                assertEquals(MoviesTestPaginatedScheme.toDomain(), awaitItem())
                awaitComplete()
            }
    }

    @Test
    @JsName("search_movies_failure")
    fun `search movies failure`() = runTest {
        RemoteSearchRepository(client = mockClientFailure())
            .searchMovies(page = 1, query = "", language = null)
            .test {
                awaitError()
            }
    }

}
