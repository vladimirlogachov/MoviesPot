package com.vlohachov.data.remote.api

import com.vlohachov.data.remote.TmdbConfig
import com.vlohachov.data.remote.schema.movie.MoviesPaginatedSchema
import retrofit2.http.GET
import retrofit2.http.Query

interface TmdbDiscoverApi {

    @GET("/3/discover/movie")
    suspend fun discoverMovies(
        @Query("page") page: Int,
        @Query("year") year: Int?,
        @Query("with_genres") genres: String?,
        @Query("with_keywords") keywords: String?,
        @Query("language") language: String?,
        @Query("api_key") apiKey: String = TmdbConfig.API_KEY,
    ): MoviesPaginatedSchema

}
