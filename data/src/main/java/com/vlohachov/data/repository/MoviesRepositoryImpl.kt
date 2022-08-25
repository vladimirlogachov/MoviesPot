package com.vlohachov.data.repository

import com.vlohachov.data.remote.TmdbApi
import com.vlohachov.data.remote.schema.MovieSchema
import com.vlohachov.data.remote.schema.toDomain
import com.vlohachov.domain.model.Movie
import com.vlohachov.domain.repository.MoviesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map

class MoviesRepositoryImpl(
    private val remote: TmdbApi,
) : MoviesRepository {

    override fun getTopRatedMovies(
        page: Int,
        language: String?,
        region: String?,
    ): Flow<List<Movie>> {
        return flow {
            val response = remote.getTopRatedMovies(
                page = page,
                language = language,
                region = region,
            )
            emit(value = response)
        }.map { response ->
            response.results.map(MovieSchema::toDomain)
        }.flowOn(context = Dispatchers.IO)
    }
}