package com.vlohachov.data.remote.schema.movie

import com.google.gson.annotations.SerializedName
import com.vlohachov.data.remote.TmdbConfig
import com.vlohachov.domain.model.movie.Movie

data class MovieSchema(
    @SerializedName("id")
    val id: Long,
    @SerializedName("title")
    val title: String,
    @SerializedName("original_title")
    val originalTitle: String,
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

internal fun List<MovieSchema>.toDomain(): List<Movie> =
    map(MovieSchema::toDomain)

internal fun MovieSchema.toDomain(): Movie =
    Movie(
        id = id,
        title = title,
        originalTitle = originalTitle,
        overview = overview,
        releaseDate = releaseDate,
        posterPath = TmdbConfig.BASE_IMAGE_URL + "w342" + posterPath,
        genreIds = genreIds,
        isAdult = isAdult,
        voteCount = voteCount,
        voteAverage = voteAverage,
    )
