package com.vlohachov.domain.model.movie

data class Movie(
    val id: Long,
    val title: String,
    val originalTitle: String,
    val overview: String,
    val releaseDate: String?,
    val posterPath: String?,
    val genreIds: List<Long>,
    val isAdult: Boolean,
    val voteCount: Long,
    val voteAverage: Float,
)
