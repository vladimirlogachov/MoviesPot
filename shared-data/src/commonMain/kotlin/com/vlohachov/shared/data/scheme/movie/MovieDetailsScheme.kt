package com.vlohachov.shared.data.scheme.movie

import com.vlohachov.shared.data.TmdbConfig
import com.vlohachov.shared.data.scheme.CompanyScheme
import com.vlohachov.shared.data.scheme.CountryScheme
import com.vlohachov.shared.data.scheme.LanguageScheme
import com.vlohachov.shared.data.scheme.genre.GenreScheme
import com.vlohachov.shared.data.scheme.genre.toDomain
import com.vlohachov.shared.data.scheme.toDomain
import com.vlohachov.shared.domain.model.movie.MovieDetails
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class MovieDetailsScheme(
    @SerialName("id")
    val id: Int,
    @SerialName("title")
    val title: String,
    @SerialName("original_title")
    val originalTitle: String,
    @SerialName("tagline")
    val tagline: String,
    @SerialName("overview")
    val overview: String,
    @SerialName("poster_path")
    val posterPath: String,
    @SerialName("runtime")
    val runtime: Int,
    @SerialName("budget")
    val budget: Int,
    @SerialName("release_date")
    val releaseDate: String,
    @SerialName("status")
    val status: String,
    @SerialName("vote_average")
    val voteAverage: Float,
    @SerialName("vote_count")
    val voteCount: Int,
    @SerialName("genres")
    val genres: List<GenreScheme>,
    @SerialName("adult")
    val isAdult: Boolean,
    @SerialName("homepage")
    val homepage: String,
    @SerialName("original_language")
    val originalLanguage: String,
    @SerialName("spoken_languages")
    val spokenLanguages: List<LanguageScheme>,
    @SerialName("production_countries")
    val productionCountries: List<CountryScheme>,
    @SerialName("production_companies")
    val productionCompanies: List<CompanyScheme>,
)

internal fun MovieDetailsScheme.toDomain(): MovieDetails =
    MovieDetails(
        id = id,
        title = title,
        originalTitle = originalTitle,
        tagline = tagline,
        overview = overview,
        posterPath = TmdbConfig.BASE_IMAGE_URL + "original" + posterPath,
        runtime = runtime,
        budget = budget,
        releaseDate = releaseDate,
        status = status,
        voteAverage = voteAverage,
        voteCount = voteCount,
        genres = genres.map(GenreScheme::toDomain),
        isAdult = isAdult,
        homepage = homepage,
        originalLanguage = originalLanguage,
        spokenLanguages = spokenLanguages.map(LanguageScheme::toDomain),
        productionCountries = productionCountries.map(CountryScheme::toDomain),
        productionCompanies = productionCompanies.map(CompanyScheme::toDomain),
    )
