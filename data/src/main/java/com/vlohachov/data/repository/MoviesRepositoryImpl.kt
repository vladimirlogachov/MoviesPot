package com.vlohachov.data.repository

import com.vlohachov.data.remote.TmdbApi
import com.vlohachov.data.remote.schema.genre.GenresSchema
import com.vlohachov.data.remote.schema.genre.toDomain
import com.vlohachov.data.remote.schema.movie.MoviesPaginatedSchema
import com.vlohachov.data.remote.schema.movie.toDomain
import com.vlohachov.domain.model.Genre
import com.vlohachov.domain.model.Movie
import com.vlohachov.domain.model.PaginatedData
import com.vlohachov.domain.repository.MoviesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map

class MoviesRepositoryImpl(private val remote: TmdbApi) : MoviesRepository {

    override fun getGenres(language: String?): Flow<List<Genre>> {
        val flow = flow {
            val response = remote.getGenres(language = language)
            emit(value = response)
        }
        return flow.map(GenresSchema::toDomain).flowOn(context = Dispatchers.IO)
    }

    override fun getUpcomingMovies(
        page: Int,
        language: String?,
        region: String?
    ): Flow<PaginatedData<Movie>> {
        val flow = flow {
            val response = remote.getUpcomingMovies(
                page = page,
                language = language,
                region = region,
            )
            emit(value = response)
        }
        return flow.map(MoviesPaginatedSchema::toDomain).flowOn(context = Dispatchers.IO)
    }

    override fun getNowPlayingMovies(
        page: Int,
        language: String?,
        region: String?
    ): Flow<PaginatedData<Movie>> {
        val flow = flow {
            val response = remote.getNowPlayingMovies(
                page = page,
                language = language,
                region = region,
            )
            emit(value = response)
        }
        return flow.map(MoviesPaginatedSchema::toDomain).flowOn(context = Dispatchers.IO)
    }

    override fun getPopularMovies(
        page: Int,
        language: String?,
        region: String?
    ): Flow<PaginatedData<Movie>> {
        val flow = flow {
            val response = remote.getPopularMovies(
                page = page,
                language = language,
                region = region,
            )
            emit(value = response)
        }
        return flow.map(MoviesPaginatedSchema::toDomain).flowOn(context = Dispatchers.IO)
    }

    override fun getTopRatedMovies(
        page: Int,
        language: String?,
        region: String?,
    ): Flow<PaginatedData<Movie>> {
        val flow = flow {
            val response = remote.getTopRatedMovies(
                page = page,
                language = language,
                region = region,
            )
            emit(value = response)
        }
        return flow.map(MoviesPaginatedSchema::toDomain).flowOn(context = Dispatchers.IO)
    }
}