package com.vlohachov.shared.data.repository

import com.vlohachov.shared.data.TmdbConfig
import com.vlohachov.shared.data.extensions.getFlow
import com.vlohachov.shared.data.scheme.movie.MoviesPaginatedScheme
import com.vlohachov.shared.data.scheme.movie.toDomain
import com.vlohachov.shared.domain.model.PaginatedData
import com.vlohachov.shared.domain.model.movie.Movie
import com.vlohachov.shared.domain.repository.SearchRepository
import io.ktor.client.HttpClient
import io.ktor.client.request.parameter
import io.ktor.client.request.url
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

public class RemoteSearchRepository(private val client: HttpClient) : SearchRepository {

    override fun searchMovies(
        query: String,
        page: Int,
        language: String?
    ): Flow<PaginatedData<Movie>> = client.getFlow<MoviesPaginatedScheme> {
        url(host = TmdbConfig.BASE_URL, path = "/3/search/movie") {
            parameter(key = "query", value = query)
            parameter(key = "page", value = page)
            parameter(key = "language", value = language)
            parameter(key = "api_key", value = TmdbConfig.API_KEY)
        }
    }.map(MoviesPaginatedScheme::toDomain)

}
