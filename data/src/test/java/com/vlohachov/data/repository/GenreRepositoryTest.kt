package com.vlohachov.data.repository

import com.google.common.truth.Truth.assertThat
import com.vlohachov.data.data.GenresTestSchema
import com.vlohachov.data.remote.api.TmdbGenreApi
import com.vlohachov.data.remote.schema.genre.toDomain
import com.vlohachov.domain.repository.GenreRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class GenreRepositoryTest {

    private val api = mockk<TmdbGenreApi>()

    private val repository: GenreRepository = GenreRepositoryImpl(remote = api)

    @Test
    fun `get genres success`() = runTest {
        val expected = GenresTestSchema.toDomain()

        coEvery { api.getGenres(language = any()) } returns GenresTestSchema

        val actual = repository.getGenres(language = null).first()

        assertThat(actual).isNotEmpty()
        assertThat(actual).isEqualTo(expected)
    }

    @Test(expected = Exception::class)
    fun `get genres failure`() = runTest {
        coEvery { api.getGenres(language = any()) } throws Exception()

        repository.getGenres(language = null).first()
    }

}
