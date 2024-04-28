package com.vlohachov.data.repository

import com.google.common.truth.Truth
import com.vlohachov.data.data.MovieCreditsTestSchema
import com.vlohachov.data.data.MovieDetailsTestSchema
import com.vlohachov.data.data.MovieKeywordsTestSchema
import com.vlohachov.data.data.MoviesTestPaginatedSchema
import com.vlohachov.data.remote.api.TmdbMovieApi
import com.vlohachov.data.remote.schema.movie.credit.toDomain
import com.vlohachov.data.remote.schema.movie.keyword.toDomain
import com.vlohachov.data.remote.schema.movie.toDomain
import com.vlohachov.shared.domain.repository.MovieRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class MovieRepositoryTest {

    private val api = mockk<TmdbMovieApi>()

    private val repository: MovieRepository = MovieRepositoryImpl(remote = api)

    @Test
    fun `get movies success`() = runTest {
        val expected = MoviesTestPaginatedSchema.toDomain()

        coEvery {
            api.getMoviesByCategory(
                category = any(),
                page = any(),
                language = any(),
                region = any(),
            )
        } returns MoviesTestPaginatedSchema

        val actual = repository.getMoviesByCategory(
            category = "category",
            page = 1,
            language = null,
            region = null,
        ).first()

        Truth.assertThat(actual).isEqualTo(expected)
    }

    @Test(expected = Exception::class)
    fun `get movies failure`() = runTest {
        coEvery {
            api.getMoviesByCategory(
                category = any(),
                page = any(),
                language = any(),
                region = any(),
            )
        } throws Exception()

        repository.getMoviesByCategory(
            category = "category",
            page = 1,
            language = null,
            region = null,
        ).first()
    }

    @Test
    fun `get movie details success`() = runTest {
        val expected = MovieDetailsTestSchema.toDomain()

        coEvery {
            api.getMovieDetails(
                id = any(),
                language = any(),
            )
        } returns MovieDetailsTestSchema

        val actual = repository.getMovieDetails(
            id = 1,
            language = null,
        ).first()

        Truth.assertThat(actual).isEqualTo(expected)
    }

    @Test(expected = Exception::class)
    fun `get movie details failure`() = runTest {
        coEvery {
            api.getMovieDetails(
                id = any(),
                language = any(),
            )
        } throws Exception()

        repository.getMovieDetails(
            id = 1,
            language = null,
        ).first()
    }

    @Test
    fun `get movie credits success`() = runTest {
        val expected = MovieCreditsTestSchema.toDomain()

        coEvery {
            api.getMovieCredits(
                id = any(),
                language = any(),
            )
        } returns MovieCreditsTestSchema

        val actual = repository.getMovieCredits(
            id = 1,
            language = null,
        ).first()

        Truth.assertThat(actual).isEqualTo(expected)
    }

    @Test(expected = Exception::class)
    fun `get movie credits failure`() = runTest {
        coEvery {
            api.getMovieCredits(
                id = any(),
                language = any(),
            )
        } throws Exception()

        repository.getMovieCredits(
            id = 1,
            language = null,
        ).first()
    }

    @Test
    fun `get movie recommendations success`() = runTest {
        val expected = MoviesTestPaginatedSchema.toDomain()

        coEvery {
            api.getMovieRecommendations(
                id = any(),
                page = any(),
                language = any(),
            )
        } returns MoviesTestPaginatedSchema

        val actual = repository.getMovieRecommendations(
            id = 1,
            page = 1,
            language = null,
        ).first()

        Truth.assertThat(actual).isEqualTo(expected)
    }

    @Test(expected = Exception::class)
    fun `get movie recommendations failure`() = runTest {
        coEvery {
            api.getMovieRecommendations(
                id = any(),
                page = any(),
                language = any(),
            )
        } throws Exception()

        repository.getMovieRecommendations(
            id = 1,
            page = 1,
            language = null,
        ).first()
    }

    @Test
    fun `get movie keywords success`() = runTest {
        val expected = MovieKeywordsTestSchema.toDomain()

        coEvery { api.getMovieKeywords(id = any()) } returns MovieKeywordsTestSchema

        val actual = repository.getMovieKeywords(id = 1).first()

        Truth.assertThat(actual).isEqualTo(expected)
    }

    @Test(expected = Exception::class)
    fun `get movie keywords failure`() = runTest {
        coEvery { api.getMovieKeywords(id = any()) } throws Exception()

        repository.getMovieKeywords(id = 1).first()
    }

}
