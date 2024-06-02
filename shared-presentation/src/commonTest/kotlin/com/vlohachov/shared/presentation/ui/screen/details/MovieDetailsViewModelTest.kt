package com.vlohachov.shared.presentation.ui.screen.details

import app.cash.turbine.test
import com.vlohachov.shared.domain.model.movie.Movie
import com.vlohachov.shared.domain.model.movie.MovieDetails
import com.vlohachov.shared.domain.model.movie.keyword.Keyword
import com.vlohachov.shared.domain.repository.MovieRepository
import com.vlohachov.shared.domain.usecase.credits.LoadDirector
import com.vlohachov.shared.domain.usecase.movie.LoadDetails
import com.vlohachov.shared.domain.usecase.movie.LoadKeywords
import com.vlohachov.shared.domain.usecase.movie.LoadRecommendations
import com.vlohachov.shared.presentation.TestKeywords
import com.vlohachov.shared.presentation.TestMovieCredits
import com.vlohachov.shared.presentation.TestMovieDetails
import com.vlohachov.shared.presentation.TestPaginatedData
import com.vlohachov.shared.presentation.core.ViewState
import dev.mokkery.answering.returns
import dev.mokkery.every
import dev.mokkery.matcher.any
import dev.mokkery.mock
import dev.mokkery.resetAnswers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import kotlin.js.JsName
import kotlin.test.Test
import kotlin.test.assertIs
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class MovieDetailsViewModelTest {

    private val repository = mock<MovieRepository> {
        every {
            getMovieDetails(id = any(), language = any())
        } returns flowOf(value = TestMovieDetails)
        every {
            getMovieCredits(id = any(), language = any())
        } returns flowOf(value = TestMovieCredits)
        every {
            getMovieKeywords(id = any())
        } returns flowOf(value = TestKeywords)
        every {
            getMovieRecommendations(id = any(), page = any(), language = any())
        } returns flowOf(value = TestPaginatedData)
    }

    private val viewModel = MovieDetailsViewModel(
        movieId = 1,
        loadDetails = LoadDetails(repository = repository),
        loadDirector = LoadDirector(repository = repository),
        loadKeywords = LoadKeywords(repository = repository),
        loadRecommendations = LoadRecommendations(repository = repository)
    )

    @Test
    @JsName(name = "content_loading")
    fun `content loading`() = runTest {
        viewModel.uiState.test {
            awaitItem().validateLoading()
        }
    }

    @Test
    @JsName(name = "content_loading_success")
    fun `content loading success`() = runTest {
        viewModel.uiState.test {
            skipItems(count = 4)
            awaitItem().validateSuccess()
        }
    }

    @Test
    @JsName(name = "content_loading_error")
    fun `content loading error`() = runTest {
        with(receiver = repository) {
            resetAnswers(this)
            every {
                getMovieDetails(id = any(), language = any())
            } returns flow { error("Error") }
            every {
                getMovieCredits(id = any(), language = any())
            } returns flow { error("Error") }
            every {
                getMovieKeywords(id = any())
            } returns flow { error("Error") }
            every {
                getMovieRecommendations(id = any(), page = any(), language = any())
            } returns flow { error("Error") }
        }

        MovieDetailsViewModel(
            movieId = 1,
            loadDetails = LoadDetails(repository = repository),
            loadDirector = LoadDirector(repository = repository),
            loadKeywords = LoadKeywords(repository = repository),
            loadRecommendations = LoadRecommendations(repository = repository)
        ).uiState.test {
            skipItems(count = 4)
            awaitItem().validateError()
        }
    }

    @Test
    @JsName(name = "error_is_set_and_consumed")
    fun `error is set and consumed`() = runTest {
        viewModel.uiState.test {
            skipItems(count = 5)
            viewModel.onError(error = Exception())
            assertNotNull(actual = awaitItem().error)
            viewModel.onErrorConsumed()
            assertNull(actual = awaitItem().error)
        }
    }

    private fun MovieDetailsViewState.validateLoading() {
        assertIs<ViewState.Loading>(
            value = detailsViewState,
            message = "DetailsState"
        )
        assertIs<ViewState.Loading>(
            value = directorViewState,
            message = "DirectorState"
        )
        assertIs<ViewState.Loading>(
            value = keywordsViewState,
            message = "KeywordsState"
        )
        assertIs<ViewState.Loading>(
            value = recommendationsViewState,
            message = "RecommendationsState"
        )
        assertNull(actual = error)
    }

    private fun MovieDetailsViewState.validateSuccess() {
        assertIs<ViewState.Success<MovieDetails>>(
            value = detailsViewState,
            message = "DetailsState"
        )
        assertIs<ViewState.Success<String>>(
            value = directorViewState,
            message = "DirectorState"
        )
        assertIs<ViewState.Success<List<Keyword>>>(
            value = keywordsViewState,
            message = "KeywordsState"
        )
        assertIs<ViewState.Success<List<Movie>>>(
            value = recommendationsViewState,
            message = "RecommendationsState"
        )
        assertNull(actual = error)
    }

    private fun MovieDetailsViewState.validateError() {
        assertIs<ViewState.Error>(
            value = detailsViewState,
            message = "DetailsState"
        )
        assertIs<ViewState.Error>(
            value = directorViewState,
            message = "DirectorState"
        )
        assertIs<ViewState.Error>(
            value = keywordsViewState,
            message = "KeywordsState"
        )
        assertIs<ViewState.Error>(
            value = recommendationsViewState,
            message = "RecommendationsState"
        )
        assertNull(actual = error)
    }

}
