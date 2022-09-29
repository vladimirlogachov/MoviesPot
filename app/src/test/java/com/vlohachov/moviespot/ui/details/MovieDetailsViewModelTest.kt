package com.vlohachov.moviespot.ui.details

import app.cash.turbine.test
import com.google.common.truth.Truth
import com.vlohachov.domain.Result
import com.vlohachov.domain.model.PaginatedData
import com.vlohachov.domain.model.movie.Movie
import com.vlohachov.domain.model.movie.MovieDetails
import com.vlohachov.domain.model.movie.keyword.Keyword
import com.vlohachov.domain.usecase.credits.DirectorUseCase
import com.vlohachov.domain.usecase.movie.MovieDetailsUseCase
import com.vlohachov.domain.usecase.movie.MovieKeywordsUseCase
import com.vlohachov.domain.usecase.movie.MovieRecommendationsUseCase
import com.vlohachov.moviespot.core.ViewState
import com.vlohachov.moviespot.data.TestKeywords
import com.vlohachov.moviespot.data.TestMovieDetails
import com.vlohachov.moviespot.data.TestPaginatedData
import com.vlohachov.moviespot.util.TestDispatcherRule
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class MovieDetailsViewModelTest {

    @get:Rule
    val dispatcherRule = TestDispatcherRule()

    private val movieDetails = mockk<MovieDetailsUseCase>()
    private val director = mockk<DirectorUseCase>()
    private val keywords = mockk<MovieKeywordsUseCase>()
    private val movieRecommendations = mockk<MovieRecommendationsUseCase>()

    private val detailsFlow =
        MutableStateFlow<Result<MovieDetails>>(value = Result.Loading)
    private val directorFlow =
        MutableStateFlow<Result<String>>(value = Result.Loading)
    private val keywordsFlow =
        MutableStateFlow<Result<List<Keyword>>>(value = Result.Loading)
    private val movieRecommendationsFlow =
        MutableStateFlow<Result<PaginatedData<Movie>>>(value = Result.Loading)

    private val viewModel by lazy {
        every { movieDetails.resultFlow(param = any()) } returns detailsFlow
        every { director.resultFlow(param = any()) } returns directorFlow
        every { keywords.resultFlow(param = any()) } returns keywordsFlow
        every { movieRecommendations.resultFlow(param = any()) } returns movieRecommendationsFlow

        MovieDetailsViewModel(
            movieId = 0,
            movieDetails = movieDetails,
            director = director,
            keywords = keywords,
            movieRecommendations = movieRecommendations,
        )
    }

    @Test
    fun `uiState content loading`() = runTest {
        viewModel.uiState.test {
            with(awaitItem()) {
                Truth.assertThat(detailsViewState).isEqualTo(ViewState.Loading)
                Truth.assertThat(directorViewState).isEqualTo(ViewState.Loading)
                Truth.assertThat(keywordsViewState).isEqualTo(ViewState.Loading)
                Truth.assertThat(recommendationsViewState).isEqualTo(ViewState.Loading)
                Truth.assertThat(error).isNull()
            }
        }
    }

    @Test
    fun `uiState content loading success`() = runTest {
        viewModel.uiState.test {
            skipItems(count = 1)

            detailsFlow.tryEmit(value = Result.Success(value = TestMovieDetails))
            directorFlow.tryEmit(value = Result.Success(value = ""))
            keywordsFlow.tryEmit(value = Result.Success(value = TestKeywords))
            movieRecommendationsFlow.tryEmit(value = Result.Success(value = TestPaginatedData))

            with(awaitItem()) {
                Truth.assertThat(detailsViewState is ViewState.Success).isTrue()
                Truth.assertThat(directorViewState is ViewState.Success).isTrue()
                Truth.assertThat(keywordsViewState is ViewState.Success).isTrue()
                Truth.assertThat(recommendationsViewState is ViewState.Success).isTrue()
                Truth.assertThat(error).isNull()
            }
        }
    }

    @Test
    fun `uiState content loading error`() = runTest {
        viewModel.uiState.test {
            skipItems(count = 1)

            detailsFlow.tryEmit(value = Result.Error(exception = Exception()))
            directorFlow.tryEmit(value = Result.Error(exception = Exception()))
            keywordsFlow.tryEmit(value = Result.Error(exception = Exception()))
            movieRecommendationsFlow.tryEmit(value = Result.Error(exception = Exception()))

            with(awaitItem()) {
                Truth.assertThat(detailsViewState is ViewState.Error).isTrue()
                Truth.assertThat(directorViewState is ViewState.Error).isTrue()
                Truth.assertThat(keywordsViewState is ViewState.Error).isTrue()
                Truth.assertThat(recommendationsViewState is ViewState.Error).isTrue()
                Truth.assertThat(error).isNull()
            }
        }
    }

    @Test
    fun `uiState common error consumed`() = runTest {
        viewModel.uiState.test {
            skipItems(count = 1)

            viewModel.onError(error = Exception())

            Truth.assertThat(awaitItem().error).isNotNull()

            viewModel.onErrorConsumed()

            Truth.assertThat(awaitItem().error).isNull()
        }
    }
}
