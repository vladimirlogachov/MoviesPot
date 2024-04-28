package com.vlohachov.shared.domain.repository

import com.vlohachov.shared.domain.model.PaginatedData
import com.vlohachov.shared.domain.model.movie.Movie
import kotlinx.coroutines.flow.Flow

public interface DiscoverRepository {

    public fun discoverMovies(
        page: Int,
        year: Int?,
        genres: String?,
        keywords: String?,
        language: String?,
    ): Flow<PaginatedData<Movie>>

}
