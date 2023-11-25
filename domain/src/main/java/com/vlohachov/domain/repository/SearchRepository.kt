package com.vlohachov.domain.repository

import com.vlohachov.domain.model.PaginatedData
import com.vlohachov.domain.model.movie.Movie
import kotlinx.coroutines.flow.Flow

interface SearchRepository {

    fun searchMovies(
        query: String,
        page: Int,
        language: String?,
    ): Flow<PaginatedData<Movie>>

}
