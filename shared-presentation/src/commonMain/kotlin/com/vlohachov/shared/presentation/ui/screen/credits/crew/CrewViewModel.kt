package com.vlohachov.shared.presentation.ui.screen.credits.crew

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vlohachov.shared.domain.usecase.credits.LoadCrew
import com.vlohachov.shared.presentation.core.ViewState
import com.vlohachov.shared.presentation.core.WhileUiSubscribed
import com.vlohachov.shared.presentation.core.toViewState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

@Stable
internal class CrewViewModel(
    movieId: Long,
    loadCrew: LoadCrew,
) : ViewModel() {

    private val _error = MutableStateFlow<Throwable?>(value = null)

    val error: StateFlow<Throwable?> = _error
    val crew = loadCrew(param = LoadCrew.Param(id = movieId))
        .map { result -> result.toViewState() }
        .stateIn(
            scope = viewModelScope,
            started = WhileUiSubscribed,
            initialValue = ViewState.Loading,
        )

    fun onError(error: Throwable) {
        _error.tryEmit(value = error)
    }

    fun onErrorConsumed() {
        _error.tryEmit(value = null)
    }

}
