package com.vlohachov.data.repository

import com.vlohachov.data.remote.api.TmdbSearchApi
import com.vlohachov.data.remote.schema.movie.MoviesPaginatedSchema
import com.vlohachov.data.remote.schema.movie.toDomain
import com.vlohachov.domain.model.PaginatedData
import com.vlohachov.domain.model.movie.Movie
import com.vlohachov.domain.repository.SearchRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class SearchRepositoryImpl(private val remote: TmdbSearchApi) : SearchRepository {

    override fun searchMovies(
        query: String,
        page: Int,
        language: String?
    ): Flow<PaginatedData<Movie>> = flow {
        remote.searchMovies(query = query, page = page, language = language)
            .also { response -> emit(value = response) }
    }.map(MoviesPaginatedSchema::toDomain)

}
