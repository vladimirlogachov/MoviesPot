package com.vlohachov.data.remote.schema

import com.google.gson.annotations.SerializedName
import com.vlohachov.data.remote.TmdbConfig
import com.vlohachov.domain.model.Movie

data class MovieSchema(
    @SerializedName("id")
    val id: Long,
    @SerializedName("title")
    val title: String,
    @SerializedName("overview")
    val overview: String,
    @SerializedName("release_date")
    val releaseDate: String,
    @SerializedName("poster_path")
    val posterPath: String?,
    @SerializedName("genre_ids")
    val genreIds: List<Long>,
    @SerializedName("adult")
    val isAdult: Boolean,
    @SerializedName("vote_count")
    val voteCount: Long,
    @SerializedName("vote_average")
    val voteAverage: Float,
)

internal fun MovieSchema.toDomain(): Movie =
    Movie(
        id = id,
        title = title,
        overview = overview,
        releaseDate = releaseDate,
        posterPath = TmdbConfig.BASE_IMAGE_URL + "w185" + posterPath,
        genreIds = genreIds,
        isAdult = isAdult,
        voteCount = voteCount,
        voteAverage = voteAverage,
    )
