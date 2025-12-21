package com.vlohachov.shared.presentation.ui.screen.discover

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vlohachov.shared.domain.Result
import com.vlohachov.shared.domain.model.genre.Genre
import com.vlohachov.shared.domain.usecase.LoadGenres
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@Stable
internal class DiscoverViewModel(loadGenres: LoadGenres) : ViewModel() {

    private companion object {
        const val YearInputLength = 4
    }

    private val _state = MutableStateFlow(value = DiscoverViewState())
    val state: StateFlow<DiscoverViewState> = _state

    private val _effect = Channel<DiscoverEvent>()
    val effect = _effect.receiveAsFlow()

    init {
        loadGenres(param = LoadGenres.Param())
            .onEach { result ->
                when (result) {
                    is Result.Error -> _state.update { current ->
                        current.copy(showProgress = false, error = result.exception)
                    }

                    Result.Loading -> _state.update { current ->
                        current.copy(showProgress = true)
                    }

                    is Result.Success<List<Genre>> -> _state.update { current ->
                        current.copy(showProgress = false, genres = result.value)
                    }
                }
            }
            .launchIn(scope = viewModelScope)
    }

    fun onAction(action: DiscoverAction) {
        when (action) {
            is DiscoverAction.EnterYear -> onYear(year = action.year)
            DiscoverAction.HideError -> onErrorConsumed()
            is DiscoverAction.RemoveGenre -> onClearSelection(genre = action.genre)
            is DiscoverAction.SelectGenre -> onSelect(genre = action.genre)
            DiscoverAction.Back -> onBack()
            DiscoverAction.Discover -> onDiscover()
        }
    }

    private fun onYear(year: String) = _state.update { current ->
        current.copy(year = year.take(n = YearInputLength))
    }

    private fun onSelect(genre: Genre) = _state.update { current ->
        current.copy(selectedGenres = current.selectedGenres + genre)
    }

    private fun onClearSelection(genre: Genre) = _state.update { current ->
        current.copy(selectedGenres = current.selectedGenres - genre)
    }

    private fun onErrorConsumed() = _state.update { current ->
        current.copy(error = null)
    }

    private fun onBack() {
        viewModelScope.launch {
            _effect.send(element = DiscoverEvent.NavigateBack)
        }
    }

    private fun onDiscover() {
        val state = state.value
        viewModelScope.launch {
            _effect.send(
                element = DiscoverEvent.NavigateToResults(
                    year = state.year.toIntOrNull(),
                    genres = state.selectedGenres.map(transform = Genre::id),
                )
            )
        }
    }

}
