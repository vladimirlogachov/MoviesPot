package com.vlohachov.data.repository

import com.vlohachov.data.remote.api.TmdbGenreApi
import com.vlohachov.data.remote.schema.genre.GenresSchema
import com.vlohachov.data.remote.schema.genre.toDomain
import com.vlohachov.shared.domain.model.genre.Genre
import com.vlohachov.shared.domain.repository.GenreRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class GenreRepositoryImpl(private val remote: TmdbGenreApi) : GenreRepository {

    override fun getGenres(language: String?): Flow<List<Genre>> = flow {
        remote.getGenres(language = language)
            .also { response -> emit(value = response) }
    }.map(GenresSchema::toDomain)

}
