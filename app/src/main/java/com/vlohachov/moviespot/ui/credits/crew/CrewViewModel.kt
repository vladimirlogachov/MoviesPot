package com.vlohachov.moviespot.ui.credits.crew

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vlohachov.domain.Result
import com.vlohachov.domain.model.movie.credit.CrewMember
import com.vlohachov.domain.usecase.credits.CrewUseCase
import com.vlohachov.moviespot.core.ViewState
import com.vlohachov.moviespot.core.WhileUiSubscribed
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class CrewViewModel(
    movieId: Long,
    crew: CrewUseCase,
) : ViewModel() {

    private val error = MutableStateFlow<Throwable?>(value = null)

    private val crewResult: Flow<Result<List<CrewMember>>> =
        crew.resultFlow(param = CrewUseCase.Param(id = movieId))

    val uiState: StateFlow<CrewViewState> = combine(
        crewResult,
        error,
    ) { crewResult, error ->
        val viewState = when (crewResult) {
            Result.Loading -> ViewState.Loading
            is Result.Error -> ViewState.Error(error = crewResult.exception)
            is Result.Success -> ViewState.Success(data = crewResult.value)
        }

        CrewViewState(
            viewState = viewState,
            error = error,
        )
    }.stateIn(
        scope = viewModelScope,
        started = WhileUiSubscribed,
        initialValue = CrewViewState(),
    )

    fun onError(error: Throwable) {
        viewModelScope.launch { this@CrewViewModel.error.emit(value = error) }
    }

    fun onErrorConsumed() {
        viewModelScope.launch { this@CrewViewModel.error.emit(value = null) }
    }
}
