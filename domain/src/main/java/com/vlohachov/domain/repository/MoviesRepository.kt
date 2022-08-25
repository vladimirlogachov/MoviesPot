package com.vlohachov.domain.repository

import com.vlohachov.domain.model.Genre
import com.vlohachov.domain.model.Movie
import kotlinx.coroutines.flow.Flow

interface MoviesRepository {

    fun getGenres(language: String?): Flow<List<Genre>>

    fun getTopRatedMovies(
        page: Int,
        language: String?,
        region: String?,
    ): Flow<List<Movie>>
}