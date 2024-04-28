package com.vlohachov.data.repository

import com.vlohachov.data.remote.api.TmdbMovieApi
import com.vlohachov.data.remote.schema.movie.MovieDetailsSchema
import com.vlohachov.data.remote.schema.movie.MoviesPaginatedSchema
import com.vlohachov.data.remote.schema.movie.credit.MovieCreditsSchema
import com.vlohachov.data.remote.schema.movie.credit.toDomain
import com.vlohachov.data.remote.schema.movie.keyword.MovieKeywordsSchema
import com.vlohachov.data.remote.schema.movie.keyword.toDomain
import com.vlohachov.data.remote.schema.movie.toDomain
import com.vlohachov.shared.domain.model.PaginatedData
import com.vlohachov.shared.domain.model.movie.Movie
import com.vlohachov.shared.domain.model.movie.MovieCredits
import com.vlohachov.shared.domain.model.movie.MovieDetails
import com.vlohachov.shared.domain.model.movie.keyword.Keyword
import com.vlohachov.shared.domain.repository.MovieRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class MovieRepositoryImpl(private val remote: TmdbMovieApi) : MovieRepository {

    override fun getMoviesByCategory(
        category: String,
        page: Int,
        language: String?,
        region: String?
    ): Flow<PaginatedData<Movie>> = flow {
        remote.getMoviesByCategory(
            category = category,
            page = page,
            language = language,
            region = region
        ).also { response -> emit(value = response) }
    }.map(MoviesPaginatedSchema::toDomain)

    override fun getMovieDetails(id: Long, language: String?): Flow<MovieDetails> = flow {
        remote.getMovieDetails(id = id, language = language)
            .also { response -> emit(value = response) }
    }.map(MovieDetailsSchema::toDomain)

    override fun getMovieCredits(id: Long, language: String?): Flow<MovieCredits> = flow {
        remote.getMovieCredits(id = id, language = language)
            .also { response -> emit(value = response) }
    }.map(MovieCreditsSchema::toDomain)


    override fun getMovieRecommendations(
        id: Long,
        page: Int,
        language: String?,
    ): Flow<PaginatedData<Movie>> = flow {
        remote.getMovieRecommendations(id = id, page = page, language = language)
            .also { response -> emit(value = response) }
    }.map(MoviesPaginatedSchema::toDomain)

    override fun getMovieKeywords(id: Long): Flow<List<Keyword>> = flow {
        remote.getMovieKeywords(id = id)
            .also { response -> emit(value = response) }
    }.map(MovieKeywordsSchema::toDomain)

}
