package com.vlohachov.data.remote

import com.vlohachov.data.remote.schema.MoviesPaginatedSchema
import retrofit2.http.GET
import retrofit2.http.Query

interface TmdbApi {
    @GET("/movie/top_rated")
    suspend fun getTopRatedMovies(
        @Query("page") page: Int,
        @Query("language") language: String?,
        @Query("region") region: String?,
        @Query("api_key") apiKey: String = TmdbConfig.API_KEY,
    ): MoviesPaginatedSchema
}