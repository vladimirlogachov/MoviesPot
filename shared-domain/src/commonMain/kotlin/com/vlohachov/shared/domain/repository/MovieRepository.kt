package com.vlohachov.shared.domain.repository

import com.vlohachov.shared.domain.model.PaginatedData
import com.vlohachov.shared.domain.model.movie.Movie
import com.vlohachov.shared.domain.model.movie.MovieCredits
import com.vlohachov.shared.domain.model.movie.MovieDetails
import com.vlohachov.shared.domain.model.movie.keyword.Keyword
import kotlinx.coroutines.flow.Flow

public interface MovieRepository {

    public fun getMoviesByCategory(
        category: String,
        page: Int,
        language: String?,
        region: String?,
    ): Flow<PaginatedData<Movie>>

    public fun getMovieDetails(
        id: Long,
        language: String?,
    ): Flow<MovieDetails>

    public fun getMovieCredits(
        id: Long,
        language: String?,
    ): Flow<MovieCredits>

    public fun getMovieRecommendations(
        id: Long,
        page: Int,
        language: String?,
    ): Flow<PaginatedData<Movie>>

    public fun getMovieKeywords(id: Long): Flow<List<Keyword>>

}
