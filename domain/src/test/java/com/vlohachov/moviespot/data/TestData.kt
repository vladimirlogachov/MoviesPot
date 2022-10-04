package com.vlohachov.moviespot.data

import com.vlohachov.domain.model.Company
import com.vlohachov.domain.model.Country
import com.vlohachov.domain.model.Language
import com.vlohachov.domain.model.PaginatedData
import com.vlohachov.domain.model.genre.Genre
import com.vlohachov.domain.model.movie.Movie
import com.vlohachov.domain.model.movie.MovieCredits
import com.vlohachov.domain.model.movie.MovieDetails
import com.vlohachov.domain.model.movie.credit.CastMember
import com.vlohachov.domain.model.movie.credit.CrewMember
import com.vlohachov.domain.model.movie.keyword.Keyword
import com.vlohachov.domain.model.settings.Settings

val TestSettings = Settings(
    dynamicTheme = true
)

val TestGenre = Genre(
    id = 1,
    name = "name",
)

val TestGenres = listOf(
    TestGenre,
    TestGenre,
    TestGenre,
)

val TestMovie = Movie(
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

val TestMovies = listOf(
    TestMovie,
    TestMovie,
    TestMovie,
    TestMovie,
)

val TestPaginatedData = PaginatedData(
    page = 1,
    data = TestMovies,
    totalResults = TestMovies.size,
    totalPages = 1,
)

val TestCastMember = CastMember(
    id = 1,
    name = "name",
    character = "character",
    profilePath = "path",
)

val TestCastMembers = listOf(
    TestCastMember,
    TestCastMember,
    TestCastMember,
)

val TestCrewMember = CrewMember(
    id = 1,
    name = "name",
    job = "job",
    profilePath = "path",
)

val TestCrewMembers = listOf(
    TestCrewMember,
    TestCrewMember,
    TestCrewMember,
)

val TestMovieCredits = MovieCredits(
    id = 0,
    cast = TestCastMembers,
    crew = TestCrewMembers,
)

val TestLanguage = Language(
    name = "name",
    englishName = "english_name",
    iso = "iso",
)

val TestCountry = Country(
    name = "name",
    iso = "iso",
)

val TestCompany = Company(
    id = 0,
    name = "name",
    logoPath = "path",
    originCountry = "country",
)

val TestMovieDetails = MovieDetails(
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
    genres = TestGenres,
    isAdult = false,
    homepage = "homepage",
    originalLanguage = "language",
    spokenLanguages = listOf(TestLanguage),
    productionCountries = listOf(TestCountry),
    productionCompanies = listOf(TestCompany),
)

val TestKeyword = Keyword(
    id = 1,
    name = "name"
)

val TestKeywords = listOf(
    TestKeyword,
    TestKeyword,
    TestKeyword,
)
