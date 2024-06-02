package com.vlohachov.shared.presentation

import com.vlohachov.shared.domain.model.Company
import com.vlohachov.shared.domain.model.Country
import com.vlohachov.shared.domain.model.Language
import com.vlohachov.shared.domain.model.PaginatedData
import com.vlohachov.shared.domain.model.genre.Genre
import com.vlohachov.shared.domain.model.movie.Movie
import com.vlohachov.shared.domain.model.movie.MovieCredits
import com.vlohachov.shared.domain.model.movie.MovieDetails
import com.vlohachov.shared.domain.model.movie.credit.CastMember
import com.vlohachov.shared.domain.model.movie.credit.CrewMember
import com.vlohachov.shared.domain.model.movie.keyword.Keyword
import com.vlohachov.shared.domain.model.settings.Settings

val TestSettings = Settings(dynamicTheme = true)

val TestGenre = testGenre()

val TestGenres = buildList {
    repeat(times = 3) { id -> add(testGenre(id = id)) }
}

fun testGenre(id: Int = 0) = Genre(id = id, name = "name")

val TestMovie = testMovie()

val TestMovies = buildList {
    repeat(times = 5) { id -> add(testMovie(id = id.toLong())) }
}

fun testMovie(id: Long = 0) = Movie(
    id = id,
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

val TestPaginatedData = PaginatedData(
    page = 1,
    data = TestMovies,
    totalResults = TestMovies.size,
    totalPages = 1,
)

val TestCastMember = testCastMember()

val TestCastMembers = buildList {
    repeat(times = 3) { id -> add(testCastMember(id = id.toLong())) }
}

fun testCastMember(id: Long = 0) = CastMember(
    id = id,
    name = "name",
    character = "character$id",
    profilePath = "path",
)

val TestCrewMember = testCrewMember()

val TestDirector = CrewMember(
    id = 1,
    name = "Director Name",
    job = "Director",
    profilePath = "path",
)

val TestCrewMembers = buildList {
    repeat(times = 3) { id -> add(testCrewMember(id = id.toLong())) }
}

fun testCrewMember(id: Long = 0) = CrewMember(
    id = id,
    name = "name",
    job = "job$id",
    profilePath = "path",
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

val TestCountry = Country(name = "name", iso = "iso")

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

val TestKeyword = testKeyword()

val TestKeywords = buildList {
    repeat(times = 3) { id -> add(testKeyword(id = id)) }
}

fun testKeyword(id: Int = 0) = Keyword(id = id, name = "name")
