package com.vlohachov.data.remote.api

import com.vlohachov.data.remote.TmdbConfig
import com.vlohachov.data.remote.schema.genre.GenresSchema
import retrofit2.http.GET
import retrofit2.http.Query

interface TmdbGenreApi {

    @GET("/3/genre/movie/list")
    suspend fun getGenres(
        @Query("language") language: String?,
        @Query("api_key") apiKey: String = TmdbConfig.API_KEY,
    ): GenresSchema

}
