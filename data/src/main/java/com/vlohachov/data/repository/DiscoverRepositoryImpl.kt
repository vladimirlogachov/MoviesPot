package com.vlohachov.data.repository

import com.vlohachov.data.remote.api.TmdbDiscoverApi
import com.vlohachov.data.remote.schema.movie.MoviesPaginatedSchema
import com.vlohachov.data.remote.schema.movie.toDomain
import com.vlohachov.shared.domain.model.PaginatedData
import com.vlohachov.shared.domain.model.movie.Movie
import com.vlohachov.shared.domain.repository.DiscoverRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class DiscoverRepositoryImpl(private val remote: TmdbDiscoverApi) : DiscoverRepository {

    override fun discoverMovies(
        page: Int,
        year: Int?,
        genres: String?,
        keywords: String?,
        language: String?
    ): Flow<PaginatedData<Movie>> = flow {
        remote.discoverMovies(
            page = page, year = year, genres = genres,
            keywords = keywords, language = language,
        ).also { response -> emit(value = response) }
    }.map(MoviesPaginatedSchema::toDomain)

}
