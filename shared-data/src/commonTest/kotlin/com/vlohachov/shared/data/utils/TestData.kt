package com.vlohachov.shared.data.utils

import com.vlohachov.shared.data.scheme.CompanyScheme
import com.vlohachov.shared.data.scheme.CountryScheme
import com.vlohachov.shared.data.scheme.LanguageScheme
import com.vlohachov.shared.data.scheme.genre.GenreScheme
import com.vlohachov.shared.data.scheme.genre.GenresScheme
import com.vlohachov.shared.data.scheme.movie.MovieDetailsScheme
import com.vlohachov.shared.data.scheme.movie.MovieScheme
import com.vlohachov.shared.data.scheme.movie.MoviesPaginatedScheme
import com.vlohachov.shared.data.scheme.movie.credit.CastMemberScheme
import com.vlohachov.shared.data.scheme.movie.credit.CrewMemberScheme
import com.vlohachov.shared.data.scheme.movie.credit.MovieCreditsScheme
import com.vlohachov.shared.data.scheme.movie.keyword.KeywordScheme
import com.vlohachov.shared.data.scheme.movie.keyword.MovieKeywordsScheme
import com.vlohachov.shared.domain.model.settings.Settings

internal val TestSettings = Settings(
    dynamicTheme = true,
    supportsDynamicTheme = true,
)

internal val GenreTestScheme = GenreScheme(
    id = 0,
    name = "Genre",
)

internal val GenresTestScheme = GenresScheme(
    genres = listOf(
        GenreTestScheme,
        GenreTestScheme,
        GenreTestScheme,
    )
)

internal val MovieTestScheme = MovieScheme(
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

internal val MoviesTestPaginatedScheme = MoviesPaginatedScheme(
    page = 1,
    results = listOf(
        MovieTestScheme,
        MovieTestScheme,
        MovieTestScheme,
    ),
    totalResults = 3,
    totalPages = 1,
)

internal val LanguageTestScheme = LanguageScheme(
    name = "name",
    englishName = "english_name",
    iso = "iso",
)

internal val CountryTestScheme = CountryScheme(
    name = "name",
    iso = "iso",
)

internal val CompanyTestScheme = CompanyScheme(
    id = 0,
    name = "name",
    logoPath = "path",
    originCountry = "country",
)

internal val MovieDetailsTestScheme = MovieDetailsScheme(
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
    genres = GenresTestScheme.genres,
    isAdult = false,
    homepage = "homepage",
    originalLanguage = "language",
    spokenLanguages = listOf(LanguageTestScheme),
    productionCountries = listOf(CountryTestScheme),
    productionCompanies = listOf(CompanyTestScheme),
)

internal val CastMemberTestScheme = CastMemberScheme(
    id = 1,
    name = "name",
    character = "character",
    profilePath = "path",
)

internal val CrewMemberTestScheme = CrewMemberScheme(
    id = 1,
    name = "name",
    job = "job",
    profilePath = "path",
)

internal val MovieCreditsTestScheme = MovieCreditsScheme(
    id = 1,
    cast = listOf(CastMemberTestScheme),
    crew = listOf(CrewMemberTestScheme),
)

internal val KeywordTestScheme = KeywordScheme(
    id = 1,
    name = "name"
)

internal val MovieKeywordsTestScheme = MovieKeywordsScheme(
    id = 1,
    keywords = listOf(
        KeywordTestScheme,
        KeywordTestScheme,
    ),
)
