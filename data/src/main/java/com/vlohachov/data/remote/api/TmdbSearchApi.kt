package com.vlohachov.data.remote.api

import com.vlohachov.data.remote.TmdbConfig
import com.vlohachov.data.remote.schema.movie.MoviesPaginatedSchema
import retrofit2.http.GET
import retrofit2.http.Query

interface TmdbSearchApi {

    @GET("/3/search/movie")
    suspend fun searchMovies(
        @Query("query") query: String,
        @Query("page") page: Int,
        @Query("language") language: String?,
        @Query("api_key") apiKey: String = TmdbConfig.API_KEY,
    ): MoviesPaginatedSchema

}
