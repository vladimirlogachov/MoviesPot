package com.vlohachov.data.remote.schema.movie

import com.google.gson.annotations.SerializedName
import com.vlohachov.domain.model.PaginatedData
import com.vlohachov.domain.model.movie.Movie

data class MoviesPaginatedSchema(
    @SerializedName("page")
    val page: Int,
    @SerializedName("results")
    val results: List<MovieSchema>,
    @SerializedName("total_results")
    val totalResults: Int,
    @SerializedName("total_pages")
    val totalPages: Int,
)

internal fun MoviesPaginatedSchema.toDomain(): PaginatedData<Movie> =
    PaginatedData(
        page = page,
        data = results.toDomain(),
        totalResults = totalResults,
        totalPages = totalPages,
    )
