package com.vlohachov.shared.data.repository

import app.cash.turbine.test
import com.vlohachov.shared.data.scheme.movie.credit.toDomain
import com.vlohachov.shared.data.scheme.movie.keyword.toDomain
import com.vlohachov.shared.data.scheme.movie.toDomain
import com.vlohachov.shared.data.utils.MovieCreditsTestScheme
import com.vlohachov.shared.data.utils.MovieDetailsTestScheme
import com.vlohachov.shared.data.utils.MovieKeywordsTestScheme
import com.vlohachov.shared.data.utils.MoviesTestPaginatedScheme
import com.vlohachov.shared.data.utils.mockClientFailure
import com.vlohachov.shared.data.utils.mockClientSuccess
import kotlinx.coroutines.test.runTest
import kotlin.js.JsName
import kotlin.test.Test
import kotlin.test.assertEquals

class RemoteMovieRepositoryTest {

    @Test
    @JsName(name = "get_movies_success")
    fun `get movies success`() = runTest {
        RemoteMovieRepository(client = mockClientSuccess(data = MoviesTestPaginatedScheme))
            .getMoviesByCategory(category = "category", page = 1, language = null, region = null)
            .test {
                assertEquals(MoviesTestPaginatedScheme.toDomain(), awaitItem())
                awaitComplete()
            }
    }

    @Test
    @JsName(name = "get_movies_failure")
    fun `get movies failure`() = runTest {
        RemoteMovieRepository(client = mockClientFailure())
            .getMoviesByCategory(category = "category", page = 1, language = null, region = null)
            .test {
                awaitError()
            }
    }

    @Test
    @JsName(name = "get_movie_details_success")
    fun `get movie details success`() = runTest {
        RemoteMovieRepository(client = mockClientSuccess(data = MovieDetailsTestScheme))
            .getMovieDetails(id = 1, language = null)
            .test {
                assertEquals(MovieDetailsTestScheme.toDomain(), awaitItem())
                awaitComplete()
            }
    }

    @Test
    @JsName(name = "get_movie_details_failure")
    fun `get movie details failure`() = runTest {
        RemoteMovieRepository(client = mockClientFailure())
            .getMovieDetails(id = 1, language = null)
            .test {
                awaitError()
            }
    }

    @Test
    @JsName(name = "get_movie_credits_success")
    fun `get movie credits success`() = runTest {
        RemoteMovieRepository(client = mockClientSuccess(data = MovieCreditsTestScheme))
            .getMovieCredits(id = 1, language = null)
            .test {
                assertEquals(MovieCreditsTestScheme.toDomain(), awaitItem())
                awaitComplete()
            }
    }

    @Test
    @JsName(name = "get_movie_credits_failure")
    fun `get movie credits failure`() = runTest {
        RemoteMovieRepository(client = mockClientFailure())
            .getMovieCredits(id = 1, language = null)
            .test {
                awaitError()
            }
    }

    @Test
    @JsName(name = "get_movie_recommendations_success")
    fun `get movie recommendations success`() = runTest {
        RemoteMovieRepository(client = mockClientSuccess(data = MoviesTestPaginatedScheme))
            .getMovieRecommendations(id = 1, page = 1, language = null)
            .test {
                assertEquals(MoviesTestPaginatedScheme.toDomain(), awaitItem())
                awaitComplete()
            }
    }

    @Test
    @JsName(name = "get_movie_recommendations_failure")
    fun `get movie recommendations failure`() = runTest {
        RemoteMovieRepository(client = mockClientFailure())
            .getMovieRecommendations(id = 1, page = 1, language = null)
            .test {
                awaitError()
            }
    }

    @Test
    @JsName(name = "get_movie_keywords_success")
    fun `get movie keywords success`() = runTest {
        RemoteMovieRepository(client = mockClientSuccess(data = MovieKeywordsTestScheme))
            .getMovieKeywords(id = 1)
            .test {
                assertEquals(MovieKeywordsTestScheme.toDomain(), awaitItem())
                awaitComplete()
            }
    }

    @Test
    @JsName(name = "get_movie_keywords_failure")
    fun `get movie keywords failure`() = runTest {
        RemoteMovieRepository(client = mockClientFailure())
            .getMovieKeywords(id = 1)
            .test {
                awaitError()
            }
    }

}
