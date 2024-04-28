package com.vlohachov.shared.data.remote.scheme.movie

import com.vlohachov.shared.data.TmdbConfig
import com.vlohachov.shared.domain.model.movie.Movie
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class MovieSchema(
    @SerialName("id")
    val id: Long,
    @SerialName("title")
    val title: String,
    @SerialName("original_title")
    val originalTitle: String,
    @SerialName("overview")
    val overview: String,
    @SerialName("release_date")
    val releaseDate: String,
    @SerialName("poster_path")
    val posterPath: String?,
    @SerialName("genre_ids")
    val genreIds: List<Long>,
    @SerialName("adult")
    val isAdult: Boolean,
    @SerialName("vote_count")
    val voteCount: Long,
    @SerialName("vote_average")
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
