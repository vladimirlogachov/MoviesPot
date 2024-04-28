package com.vlohachov.shared.data.remote.scheme.movie

import com.vlohachov.shared.domain.model.PaginatedData
import com.vlohachov.shared.domain.model.movie.Movie
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class MoviesPaginatedSchema(
    @SerialName("page")
    val page: Int,
    @SerialName("results")
    val results: List<MovieSchema>,
    @SerialName("total_results")
    val totalResults: Int,
    @SerialName("total_pages")
    val totalPages: Int,
)

internal fun MoviesPaginatedSchema.toDomain(): PaginatedData<Movie> =
    PaginatedData(
        page = page,
        data = results.toDomain(),
        totalResults = totalResults,
        totalPages = totalPages,
    )
