package com.vlohachov.domain.model.movie

import com.vlohachov.domain.model.Company
import com.vlohachov.domain.model.Country
import com.vlohachov.domain.model.Language
import com.vlohachov.domain.model.genre.Genre

data class MovieDetails(
    val id: Int,
    val title: String,
    val originalTitle: String,
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
