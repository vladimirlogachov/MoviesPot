package com.vlohachov.shared.data.repository

import app.cash.turbine.test
import com.vlohachov.shared.data.scheme.genre.toDomain
import com.vlohachov.shared.data.utils.GenresTestScheme
import com.vlohachov.shared.data.utils.mockClientFailure
import com.vlohachov.shared.data.utils.mockClientSuccess
import kotlinx.coroutines.test.runTest
import kotlin.js.JsName
import kotlin.test.Test
import kotlin.test.assertEquals

class RemoteGenreRepositoryTest {

    @Test
    @JsName(name = "get_genre_success")
    fun `get genres success`() = runTest {
        RemoteGenreRepository(client = mockClientSuccess(data = GenresTestScheme))
            .getGenres(language = null)
            .test {
                assertEquals(GenresTestScheme.toDomain(), awaitItem())
                awaitComplete()
            }
    }

    @Test
    @JsName(name = "get_genre_failure")
    fun `get genres failure`() = runTest {
        RemoteGenreRepository(client = mockClientFailure())
            .getGenres(language = null)
            .test {
                awaitError()
            }
    }

}
