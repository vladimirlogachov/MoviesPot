package com.vlohachov.shared.data.repository

import com.vlohachov.shared.data.TmdbConfig
import com.vlohachov.shared.data.extensions.getFlow
import com.vlohachov.shared.data.scheme.movie.MoviesPaginatedScheme
import com.vlohachov.shared.data.scheme.movie.toDomain
import com.vlohachov.shared.domain.model.PaginatedData
import com.vlohachov.shared.domain.model.movie.Movie
import com.vlohachov.shared.domain.repository.DiscoverRepository
import io.ktor.client.HttpClient
import io.ktor.client.request.parameter
import io.ktor.client.request.url
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

public class RemoteDiscoverRepository(private val client: HttpClient) : DiscoverRepository {

    override fun discoverMovies(
        page: Int,
        year: Int?,
        genres: String?,
        keywords: String?,
        language: String?
    ): Flow<PaginatedData<Movie>> = client.getFlow<MoviesPaginatedScheme> {
        url(host = TmdbConfig.HOST, path = "/3/discover/movie") {
            parameter(key = "api_key", value = TmdbConfig.API_KEY)
            parameter(key = "page", value = page)
            parameter(key = "year", value = year)
            parameter(key = "with_genres", value = genres)
            parameter(key = "with_keywords", value = keywords)
            parameter(key = "language", value = language)
        }
    }.map(MoviesPaginatedScheme::toDomain)

}
