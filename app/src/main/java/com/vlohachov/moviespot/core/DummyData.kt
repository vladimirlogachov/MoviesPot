package com.vlohachov.moviespot.core

import com.vlohachov.domain.model.movie.Movie

const val LoremIpsum = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod" +
        " tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nos" +
        "trud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute i" +
        "rure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pari" +
        "atur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deseru" +
        "nt mollit anim id est laborum."

val DummyMovie = Movie(
    id = 10,
    title = "Title",
    originalTitle = "Original title",
    overview = "",
    releaseDate = "",
    posterPath = null,
    genreIds = listOf(),
    isAdult = false,
    voteCount = 10,
    voteAverage = 10.0f,
)

val DummyMovies = listOf(
    Movie(
        id = 10,
        title = "Movie 1",
        originalTitle = "Original title 1",
        overview = "",
        releaseDate = "",
        posterPath = null,
        genreIds = listOf(),
        isAdult = false,
        voteCount = 10,
        voteAverage = 10.0f,
    ),
    Movie(
        id = 10,
        title = "Movie 2",
        originalTitle = "Original title 2",
        overview = "",
        releaseDate = "",
        posterPath = null,
        genreIds = listOf(),
        isAdult = false,
        voteCount = 10,
        voteAverage = 10.0f,
    ),
    Movie(
        id = 10,
        title = "Movie 3",
        originalTitle = "Original title 3",
        overview = "",
        releaseDate = "",
        posterPath = null,
        genreIds = listOf(),
        isAdult = false,
        voteCount = 10,
        voteAverage = 10.0f,
    ),
    Movie(
        id = 10,
        title = "Movie 4",
        originalTitle = "Original title 4",
        overview = "",
        releaseDate = "",
        posterPath = null,
        genreIds = listOf(),
        isAdult = false,
        voteCount = 10,
        voteAverage = 10.0f,
    )
)