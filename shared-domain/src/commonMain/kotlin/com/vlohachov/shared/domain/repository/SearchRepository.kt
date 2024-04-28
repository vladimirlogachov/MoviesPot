package com.vlohachov.shared.domain.repository

import com.vlohachov.shared.domain.model.PaginatedData
import com.vlohachov.shared.domain.model.movie.Movie
import kotlinx.coroutines.flow.Flow

public interface SearchRepository {

    public fun searchMovies(
        query: String,
        page: Int,
        language: String?,
    ): Flow<PaginatedData<Movie>>

}
