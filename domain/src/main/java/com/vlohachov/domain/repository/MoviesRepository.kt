package com.vlohachov.domain.repository

import com.vlohachov.domain.model.PaginatedData
import com.vlohachov.domain.model.genre.Genre
import com.vlohachov.domain.model.movie.Movie
import com.vlohachov.domain.model.movie.MovieCredits
import com.vlohachov.domain.model.movie.MovieDetails
import kotlinx.coroutines.flow.Flow

interface MoviesRepository {

    fun getGenres(language: String?): Flow<List<Genre>>

    fun getUpcomingMovies(
        page: Int,
        language: String?,
        region: String?,
    ): Flow<PaginatedData<Movie>>

    fun getNowPlayingMovies(
        page: Int,
        language: String?,
        region: String?,
    ): Flow<PaginatedData<Movie>>

    fun getPopularMovies(
        page: Int,
        language: String?,
        region: String?,
    ): Flow<PaginatedData<Movie>>

    fun getTopRatedMovies(
        page: Int,
        language: String?,
        region: String?,
    ): Flow<PaginatedData<Movie>>

    fun getMovieDetails(
        id: Long,
        language: String?,
    ): Flow<MovieDetails>

    fun getMovieCredits(
        id: Long,
        language: String?,
    ): Flow<MovieCredits>
}