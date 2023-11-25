package com.vlohachov.domain.repository

import com.vlohachov.domain.model.PaginatedData
import com.vlohachov.domain.model.movie.Movie
import com.vlohachov.domain.model.movie.MovieCredits
import com.vlohachov.domain.model.movie.MovieDetails
import com.vlohachov.domain.model.movie.keyword.Keyword
import kotlinx.coroutines.flow.Flow

interface MovieRepository {

    fun getMoviesByCategory(
        category: String,
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

    fun getMovieRecommendations(
        id: Long,
        page: Int,
        language: String?,
    ): Flow<PaginatedData<Movie>>

    fun getMovieKeywords(id: Long): Flow<List<Keyword>>

}
