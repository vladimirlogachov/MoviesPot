package com.vlohachov.shared.domain.model.movie

import com.vlohachov.shared.domain.model.Company
import com.vlohachov.shared.domain.model.Country
import com.vlohachov.shared.domain.model.Language
import com.vlohachov.shared.domain.model.genre.Genre

public data class MovieDetails(
    val id: Int,
    val title: String,
    val originalTitle: String,
    val tagline: String,
    val overview: String,
    val posterPath: String,
    val runtime: Int,
    val budget: Int,
    val releaseDate: String,
    val status: String,
    val voteAverage: Float,
    val voteCount: Int,
    val genres: List<Genre>,
    val isAdult: Boolean,
    val homepage: String,
    val originalLanguage: String,
    val spokenLanguages: List<Language>,
    val productionCountries: List<Country>,
    val productionCompanies: List<Company>,
)
