package com.vlohachov.moviespot.ui.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vlohachov.moviespot.core.WhileUiSubscribed
import com.vlohachov.moviespot.core.toViewState
import com.vlohachov.moviespot.core.toViewStatePaginated
import com.vlohachov.shared.domain.usecase.credits.LoadDirector
import com.vlohachov.shared.domain.usecase.movie.LoadDetails
import com.vlohachov.shared.domain.usecase.movie.LoadKeywords
import com.vlohachov.shared.domain.usecase.movie.LoadRecommendations
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MovieDetailsViewModel(
    movieId: Long,
    loadDetails: LoadDetails,
    loadDirector: LoadDirector,
    loadKeywords: LoadKeywords,
    loadRecommendations: LoadRecommendations,
) : ViewModel() {

    private val error = MutableStateFlow<Throwable?>(value = null)

    val uiState: StateFlow<MovieDetailsViewState> = combine(
        loadDetails(param = LoadDetails.Param(id = movieId)),
        loadDirector(param = LoadDirector.Param(id = movieId)),
        loadKeywords(param = LoadKeywords.Param(id = movieId)),
        loadRecommendations(param = LoadRecommendations.Param(id = movieId)),
        error,
    ) { details, director, keywords, recommendations, error ->
        MovieDetailsViewState(
            detailsViewState = details.toViewState(),
            directorViewState = director.toViewState(),
            keywordsViewState = keywords.toViewState(),
            recommendationsViewState = recommendations.toViewStatePaginated(),
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
