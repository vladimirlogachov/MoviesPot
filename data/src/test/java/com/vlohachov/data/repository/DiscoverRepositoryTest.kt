package com.vlohachov.data.repository

import com.google.common.truth.Truth.assertThat
import com.vlohachov.data.data.MoviesTestPaginatedSchema
import com.vlohachov.data.remote.api.TmdbDiscoverApi
import com.vlohachov.data.remote.schema.movie.toDomain
import com.vlohachov.domain.repository.DiscoverRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class DiscoverRepositoryTest {

    private val api = mockk<TmdbDiscoverApi>()

    private val repository: DiscoverRepository = DiscoverRepositoryImpl(remote = api)

    @Test
    fun `discover movies success`() = runTest {
        val expected = MoviesTestPaginatedSchema.toDomain()

        coEvery {
            api.discoverMovies(
                page = any(),
                year = any(),
                genres = any(),
                keywords = any(),
                language = any(),
            )
        } returns MoviesTestPaginatedSchema

        val actual = repository.discoverMovies(
            page = 1,
            year = null,
            genres = null,
            keywords = null,
            language = null,
        ).first()

        assertThat(actual).isEqualTo(expected)
    }

    @Test(expected = Exception::class)
    fun `discover movies failure`() = runTest {
        coEvery {
            api.discoverMovies(
                page = any(),
                year = any(),
                genres = any(),
                keywords = any(),
                language = any(),
            )
        } throws Exception()

        repository.discoverMovies(
            page = 1,
            year = null,
            genres = null,
            keywords = null,
            language = null,
        ).first()
    }

}
