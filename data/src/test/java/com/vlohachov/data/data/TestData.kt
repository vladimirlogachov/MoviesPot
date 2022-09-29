package com.vlohachov.data.data

import com.vlohachov.data.remote.schema.CompanySchema
import com.vlohachov.data.remote.schema.CountrySchema
import com.vlohachov.data.remote.schema.LanguageSchema
import com.vlohachov.data.remote.schema.genre.GenreSchema
import com.vlohachov.data.remote.schema.genre.GenresSchema
import com.vlohachov.data.remote.schema.movie.MovieDetailsSchema
import com.vlohachov.data.remote.schema.movie.MovieSchema
import com.vlohachov.data.remote.schema.movie.MoviesPaginatedSchema
import com.vlohachov.data.remote.schema.movie.credit.CastMemberSchema
import com.vlohachov.data.remote.schema.movie.credit.CrewMemberSchema
import com.vlohachov.data.remote.schema.movie.credit.MovieCreditsSchema
import com.vlohachov.data.remote.schema.movie.keyword.KeywordSchema
import com.vlohachov.data.remote.schema.movie.keyword.MovieKeywordsSchema

val GenreTestSchema = GenreSchema(
    id = 0,
    name = "Genre",
)

val GenresTestSchema = GenresSchema(
    genres = listOf(
        GenreTestSchema,
        GenreTestSchema,
        GenreTestSchema,
    )
)

val MovieTestSchema = MovieSchema(
    id = 0,
    title = "title",
    originalTitle = "original_title",
    overview = "overview",
    releaseDate = "2022-08-18",
    posterPath = "path",
    genreIds = listOf(10, 11),
    isAdult = false,
    voteCount = 234,
    voteAverage = 6.7f,
)

val MoviesTestPaginatedSchema = MoviesPaginatedSchema(
    page = 1,
    results = listOf(
        MovieTestSchema,
        MovieTestSchema,
        MovieTestSchema,
    ),
    totalResults = 3,
    totalPages = 1,
)

val LanguageTestSchema = LanguageSchema(
    name = "name",
    englishName = "english_name",
    iso = "iso",
)

val CountryTestSchema = CountrySchema(
    name = "name",
    iso = "iso",
)

val CompanyTestSchema = CompanySchema(
    id = 0,
    name = "name",
    logoPath = "path",
    originCountry = "country",
)

val MovieDetailsTestSchema = MovieDetailsSchema(
    id = 0,
    title = "title",
    originalTitle = "original_title",
    tagline = "tagline",
    overview = "overview",
    posterPath = "poster",
    runtime = 96,
    budget = 12345678,
    releaseDate = "2022-08-18",
    status = "status",
    voteAverage = 6.7f,
    voteCount = 1234,
    genres = GenresTestSchema.genres,
    isAdult = false,
    homepage = "homepage",
    originalLanguage = "language",
    spokenLanguages = listOf(LanguageTestSchema),
    productionCountries = listOf(CountryTestSchema),
    productionCompanies = listOf(CompanyTestSchema),
)

val CastMemberTestSchema = CastMemberSchema(
    id = 1,
    name = "name",
    character = "character",
    profilePath = "path",
)

val CrewMemberTestSchema = CrewMemberSchema(
    id = 1,
    name = "name",
    job = "job",
    profilePath = "path",
)

val MovieCreditsTestSchema = MovieCreditsSchema(
    id = 1,
    cast = listOf(CastMemberTestSchema),
    crew = listOf(CrewMemberTestSchema),
)

val KeywordTestSchema = KeywordSchema(
    id = 1,
    name = "name"
)

val MovieKeywordsTestSchema = MovieKeywordsSchema(
    id = 1,
    keywords = listOf(
        KeywordTestSchema,
        KeywordTestSchema,
    ),
)
