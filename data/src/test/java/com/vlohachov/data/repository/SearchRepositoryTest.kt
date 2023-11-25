package com.vlohachov.data.repository

import com.google.common.truth.Truth.assertThat
import com.vlohachov.data.data.MoviesTestPaginatedSchema
import com.vlohachov.data.remote.api.TmdbSearchApi
import com.vlohachov.data.remote.schema.movie.toDomain
import com.vlohachov.domain.repository.SearchRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class SearchRepositoryTest {

    private val api = mockk<TmdbSearchApi>()

    private val repository: SearchRepository = SearchRepositoryImpl(remote = api)

    @Test
    fun `Search movies success`() = runTest {
        val expected = MoviesTestPaginatedSchema.toDomain()

        coEvery {
            api.searchMovies(
                page = any(),
                query = any(),
                language = any(),
            )
        } returns MoviesTestPaginatedSchema

        val actual = repository.searchMovies(
            page = 1,
            query = "",
            language = null,
        ).first()

        assertThat(actual).isEqualTo(expected)
    }

    @Test(expected = Exception::class)
    fun `Search movies failure`() = runTest {
        coEvery {
            api.searchMovies(
                page = any(),
                query = any(),
                language = any(),
            )
        } throws Exception()

        repository.searchMovies(
            page = 1,
            query = "",
            language = null,
        ).first()
    }

}
