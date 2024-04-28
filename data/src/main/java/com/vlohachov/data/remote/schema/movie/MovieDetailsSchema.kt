package com.vlohachov.data.remote.schema.movie

import com.google.gson.annotations.SerializedName
import com.vlohachov.data.remote.TmdbConfig
import com.vlohachov.data.remote.schema.CompanySchema
import com.vlohachov.data.remote.schema.CountrySchema
import com.vlohachov.data.remote.schema.LanguageSchema
import com.vlohachov.data.remote.schema.genre.GenreSchema
import com.vlohachov.data.remote.schema.genre.toDomain
import com.vlohachov.data.remote.schema.toDomain
import com.vlohachov.shared.domain.model.movie.MovieDetails

data class MovieDetailsSchema(
    @SerializedName("id")
    val id: Int,
    @SerializedName("title")
    val title: String,
    @SerializedName("original_title")
    val originalTitle: String,
    @SerializedName("tagline")
    val tagline: String,
    @SerializedName("overview")
    val overview: String,
    @SerializedName("poster_path")
    val posterPath: String,
    @SerializedName("runtime")
    val runtime: Int,
    @SerializedName("budget")
    val budget: Int,
    @SerializedName("release_date")
    val releaseDate: String,
    @SerializedName("status")
    val status: String,
    @SerializedName("vote_average")
    val voteAverage: Float,
    @SerializedName("vote_count")
    val voteCount: Int,
    @SerializedName("genres")
    val genres: List<GenreSchema>,
    @SerializedName("adult")
    val isAdult: Boolean,
    @SerializedName("homepage")
    val homepage: String,
    @SerializedName("original_language")
    val originalLanguage: String,
    @SerializedName("spoken_languages")
    val spokenLanguages: List<LanguageSchema>,
    @SerializedName("production_countries")
    val productionCountries: List<CountrySchema>,
    @SerializedName("production_companies")
    val productionCompanies: List<CompanySchema>,
)

internal fun MovieDetailsSchema.toDomain(): MovieDetails =
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
        genres = genres.map(GenreSchema::toDomain),
        isAdult = isAdult,
        homepage = homepage,
        originalLanguage = originalLanguage,
        spokenLanguages = spokenLanguages.map(LanguageSchema::toDomain),
        productionCountries = productionCountries.map(CountrySchema::toDomain),
        productionCompanies = productionCompanies.map(CompanySchema::toDomain),
    )
