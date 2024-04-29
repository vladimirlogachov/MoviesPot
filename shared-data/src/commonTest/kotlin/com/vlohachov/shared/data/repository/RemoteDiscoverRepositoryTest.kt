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

class RemoteDiscoverRepositoryTest {

    @Test
    @JsName("discover_movies_success")
    fun `discover movies success`() = runTest {
        RemoteDiscoverRepository(client = mockClientSuccess(data = MoviesTestPaginatedScheme))
            .discoverMovies(
                page = 1, year = null, genres = null,
                keywords = null, language = null
            ).test {
                assertEquals(MoviesTestPaginatedScheme.toDomain(), awaitItem())
                awaitComplete()
            }
    }

    @Test
    @JsName("discover_movies_failure")
    fun `discover movies failure`() = runTest {
        RemoteDiscoverRepository(client = mockClientFailure())
            .discoverMovies(
                page = 1, year = null, genres = null,
                keywords = null, language = null
            ).test {
                awaitError()
            }
    }

}
