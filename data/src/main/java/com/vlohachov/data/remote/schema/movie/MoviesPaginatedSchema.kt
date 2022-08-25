package com.vlohachov.data.remote.schema.movie

import com.google.gson.annotations.SerializedName

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
