package com.vlohachov.shared.ui.screen.credits.crew

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vlohachov.shared.domain.usecase.credits.LoadCrew
import com.vlohachov.shared.ui.core.WhileUiSubscribed
import com.vlohachov.shared.ui.state.toViewState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

internal class CrewViewModel(
    movieId: Long,
    loadCrew: LoadCrew,
) : ViewModel() {

    private val error = MutableStateFlow<Throwable?>(value = null)

    val uiState: StateFlow<CrewViewState> = combine(
        loadCrew(param = LoadCrew.Param(id = movieId)),
        error,
    ) { crew, error ->
        CrewViewState(
            viewState = crew.toViewState(),
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
