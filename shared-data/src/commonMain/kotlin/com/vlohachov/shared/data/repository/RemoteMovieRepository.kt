package com.vlohachov.shared.data.repository

import com.vlohachov.shared.data.TmdbConfig
import com.vlohachov.shared.data.getFlow
import com.vlohachov.shared.data.remote.scheme.movie.MovieDetailsSchema
import com.vlohachov.shared.data.remote.scheme.movie.MoviesPaginatedSchema
import com.vlohachov.shared.data.remote.scheme.movie.credit.MovieCreditsSchema
import com.vlohachov.shared.data.remote.scheme.movie.credit.toDomain
import com.vlohachov.shared.data.remote.scheme.movie.keyword.MovieKeywordsSchema
import com.vlohachov.shared.data.remote.scheme.movie.keyword.toDomain
import com.vlohachov.shared.data.remote.scheme.movie.toDomain
import com.vlohachov.shared.domain.model.PaginatedData
import com.vlohachov.shared.domain.model.movie.Movie
import com.vlohachov.shared.domain.model.movie.MovieCredits
import com.vlohachov.shared.domain.model.movie.MovieDetails
import com.vlohachov.shared.domain.model.movie.keyword.Keyword
import com.vlohachov.shared.domain.repository.MovieRepository
import io.ktor.client.HttpClient
import io.ktor.client.request.parameter
import io.ktor.client.request.url
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

public class RemoteMovieRepository(private val client: HttpClient) : MovieRepository {

    override fun getMoviesByCategory(
        category: String,
        page: Int,
        language: String?,
        region: String?
    ): Flow<PaginatedData<Movie>> = client.getFlow<MoviesPaginatedSchema> {
        url(host = TmdbConfig.BASE_URL, path = "/3/movie/$category") {
            parameter(key = "api_key", value = TmdbConfig.API_KEY)
            parameter(key = "page", value = page)
            parameter(key = "language", value = language)
            parameter(key = "region", value = region)
        }
    }.map(MoviesPaginatedSchema::toDomain)

    override fun getMovieDetails(id: Long, language: String?): Flow<MovieDetails> =
        client.getFlow<MovieDetailsSchema> {
            url(host = TmdbConfig.BASE_URL, path = "/3/movie/$id") {
                parameter(key = "api_key", value = TmdbConfig.API_KEY)
                parameter(key = "language", value = language)
            }
        }.map(MovieDetailsSchema::toDomain)

    override fun getMovieCredits(id: Long, language: String?): Flow<MovieCredits> =
        client.getFlow<MovieCreditsSchema> {
            url(host = TmdbConfig.BASE_URL, path = "/3/movie/$id/credits") {
                parameter(key = "api_key", value = TmdbConfig.API_KEY)
                parameter(key = "language", value = language)
            }
        }.map(MovieCreditsSchema::toDomain)

    override fun getMovieRecommendations(
        id: Long,
        page: Int,
        language: String?,
    ): Flow<PaginatedData<Movie>> = client.getFlow<MoviesPaginatedSchema> {
        url(host = TmdbConfig.BASE_URL, path = "/3/movie/$id/recommendations") {
            parameter(key = "api_key", value = TmdbConfig.API_KEY)
            parameter(key = "page", value = page)
            parameter(key = "language", value = language)
        }
    }.map(MoviesPaginatedSchema::toDomain)

    override fun getMovieKeywords(id: Long): Flow<List<Keyword>> =
        client.getFlow<MovieKeywordsSchema> {
            url(host = TmdbConfig.BASE_URL, path = "/3/movie/$id/keywords") {
                parameter(key = "api_key", value = TmdbConfig.API_KEY)
            }
        }.map(MovieKeywordsSchema::toDomain)

}
