package com.vlohachov.domain.repository

import com.vlohachov.domain.model.PaginatedData
import com.vlohachov.domain.model.movie.Movie
import kotlinx.coroutines.flow.Flow

interface DiscoverRepository {

    fun discoverMovies(
        page: Int,
        year: Int?,
        genres: String?,
        keywords: String?,
        language: String?,
    ): Flow<PaginatedData<Movie>>

}
