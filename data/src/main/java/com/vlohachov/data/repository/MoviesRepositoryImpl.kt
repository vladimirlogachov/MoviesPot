package com.vlohachov.data.repository

import com.vlohachov.data.remote.TmdbApi
import com.vlohachov.data.remote.schema.genre.GenresSchema
import com.vlohachov.data.remote.schema.genre.toDomain
import com.vlohachov.data.remote.schema.movie.MovieDetailsSchema
import com.vlohachov.data.remote.schema.movie.MoviesPaginatedSchema
import com.vlohachov.data.remote.schema.movie.credit.MovieCreditsSchema
import com.vlohachov.data.remote.schema.movie.credit.toDomain
import com.vlohachov.data.remote.schema.movie.keyword.MovieKeywordsSchema
import com.vlohachov.data.remote.schema.movie.keyword.toDomain
import com.vlohachov.data.remote.schema.movie.toDomain
import com.vlohachov.domain.model.PaginatedData
import com.vlohachov.domain.model.genre.Genre
import com.vlohachov.domain.model.movie.Movie
import com.vlohachov.domain.model.movie.MovieCredits
import com.vlohachov.domain.model.movie.MovieDetails
import com.vlohachov.domain.model.movie.keyword.Keyword
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

    override fun getMovieDetails(id: Long, language: String?): Flow<MovieDetails> {
        val flow = flow {
            val response = remote.getMovieDetails(
                id = id,
                language = language,
            )
            emit(value = response)
        }
        return flow.map(MovieDetailsSchema::toDomain).flowOn(context = Dispatchers.IO)
    }

    override fun getMovieCredits(id: Long, language: String?): Flow<MovieCredits> {
        val flow = flow {
            val response = remote.getMovieCredits(
                id = id,
                language = language,
            )
            emit(value = response)
        }
        return flow.map(MovieCreditsSchema::toDomain).flowOn(context = Dispatchers.IO)
    }

    override fun getMovieRecommendations(
        id: Long,
        page: Int,
        language: String?,
    ): Flow<PaginatedData<Movie>> {
        val flow = flow {
            val response = remote.getMovieRecommendations(
                id = id,
                page = page,
                language = language,
            )
            emit(value = response)
        }
        return flow.map(MoviesPaginatedSchema::toDomain).flowOn(context = Dispatchers.IO)
    }

    override fun getMovieKeywords(id: Long): Flow<List<Keyword>> {
        val flow = flow {
            val response = remote.getMovieKeywords(id = id)
            emit(value = response)
        }
        return flow.map(MovieKeywordsSchema::toDomain).flowOn(context = Dispatchers.IO)
    }

    override fun searchMovies(
        query: String,
        page: Int,
        language: String?
    ): Flow<PaginatedData<Movie>> {
        val flow = flow {
            val response = remote.searchMovies(
                query = query,
                page = page,
                language = language,
            )
            emit(value = response)
        }
        return flow.map(MoviesPaginatedSchema::toDomain).flowOn(context = Dispatchers.IO)
    }

    override fun discoverMovies(
        page: Int,
        year: Int?,
        genres: String?,
        keywords: String?,
        language: String?
    ): Flow<PaginatedData<Movie>> {
        val flow = flow {
            val response = remote.discoverMovies(
                page = page,
                year = year,
                genres = genres,
                keywords = keywords,
                language = language,
            )
            emit(value = response)
        }
        return flow.map(MoviesPaginatedSchema::toDomain).flowOn(context = Dispatchers.IO)
    }
}