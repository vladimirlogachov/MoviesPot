package com.vlohachov.shared.data.repository

import com.vlohachov.shared.data.TmdbConfig
import com.vlohachov.shared.data.extensions.getFlow
import com.vlohachov.shared.data.scheme.genre.GenresScheme
import com.vlohachov.shared.data.scheme.genre.toDomain
import com.vlohachov.shared.domain.model.genre.Genre
import com.vlohachov.shared.domain.repository.GenreRepository
import io.ktor.client.HttpClient
import io.ktor.client.request.parameter
import io.ktor.client.request.url
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

public class RemoteGenreRepository(private val client: HttpClient) : GenreRepository {

    override fun getGenres(language: String?): Flow<List<Genre>> = client.getFlow<GenresScheme> {
        url(host = TmdbConfig.HOST, path = "/3/genre/movie/list") {
            parameter(key = "api_key", value = TmdbConfig.API_KEY)
            parameter(key = "language", value = language)
        }
    }.map(GenresScheme::toDomain)

}
