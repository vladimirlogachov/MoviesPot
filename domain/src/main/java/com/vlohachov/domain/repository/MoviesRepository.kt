package com.vlohachov.domain.repository

import com.vlohachov.domain.model.Genre
import com.vlohachov.domain.model.Movie
import com.vlohachov.domain.model.PaginatedData
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
}