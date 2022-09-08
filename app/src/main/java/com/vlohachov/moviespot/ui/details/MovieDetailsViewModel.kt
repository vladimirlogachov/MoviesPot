package com.vlohachov.moviespot.ui.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
import com.vlohachov.moviespot.core.WhileUiSubscribed
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class MovieDetailsViewModel(
    movieId: Long,
    movieDetails: MovieDetailsUseCase,
    director: DirectorUseCase,
    keywords: MovieKeywordsUseCase,
    movieRecommendations: MovieRecommendationsUseCase,
) : ViewModel() {

    private val error = MutableStateFlow<Throwable?>(value = null)

    private val detailsResult: Flow<Result<MovieDetails>> =
        movieDetails.resultFlow(param = MovieDetailsUseCase.Param(id = movieId))
    private val directorResult: Flow<Result<String>> =
        director.resultFlow(param = DirectorUseCase.Param(id = movieId))
    private val keywordsResult: Flow<Result<List<Keyword>>> =
        keywords.resultFlow(param = MovieKeywordsUseCase.Param(id = movieId))
    private val recommendationsResult: Flow<Result<PaginatedData<Movie>>> =
        movieRecommendations.resultFlow(param = MovieRecommendationsUseCase.Param(id = movieId))

    val uiState: StateFlow<MovieDetailsViewState> = combine(
        detailsResult,
        directorResult,
        keywordsResult,
        recommendationsResult,
        error,
    ) { detailsResult, directorResult, keywordsResult, recommendationsResult, error ->
        val detailsViewState = when (detailsResult) {
            Result.Loading -> ViewState.Loading
            is Result.Error -> ViewState.Error(error = detailsResult.exception)
            is Result.Success -> ViewState.Success(data = detailsResult.value)
        }

        val directorViewState = when (directorResult) {
            Result.Loading -> ViewState.Loading
            is Result.Error -> ViewState.Error(error = directorResult.exception)
            is Result.Success -> ViewState.Success(data = directorResult.value)
        }

        val keywordsViewState = when (keywordsResult) {
            Result.Loading -> ViewState.Loading
            is Result.Error -> ViewState.Error(error = keywordsResult.exception)
            is Result.Success -> ViewState.Success(data = keywordsResult.value)
        }

        val recommendationsViewState = when (recommendationsResult) {
            Result.Loading -> ViewState.Loading
            is Result.Error -> ViewState.Error(error = recommendationsResult.exception)
            is Result.Success -> ViewState.Success(data = recommendationsResult.value.data)
        }

        MovieDetailsViewState(
            detailsViewState = detailsViewState,
            directorViewState = directorViewState,
            keywordsViewState = keywordsViewState,
            recommendationsViewState = recommendationsViewState,
            error = error,
        )
    }.stateIn(
        scope = viewModelScope,
        started = WhileUiSubscribed,
        initialValue = MovieDetailsViewState(),
    )

    fun onError(error: Throwable) {
        viewModelScope.launch { this@MovieDetailsViewModel.error.emit(value = error) }
    }

    fun onErrorConsumed() {
        viewModelScope.launch { this@MovieDetailsViewModel.error.emit(value = null) }
    }
}