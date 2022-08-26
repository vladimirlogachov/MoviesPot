package com.vlohachov.moviespot.ui.genres

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vlohachov.domain.Result
import com.vlohachov.domain.model.Genre
import com.vlohachov.domain.usecase.GenresUseCase
import com.vlohachov.moviespot.core.ViewState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class GenresViewModel(genresUseCase: GenresUseCase) : ViewModel() {

    private val _viewState = MutableStateFlow<ViewState<List<Genre>>>(value = ViewState.Loading)

    val viewState = _viewState.asStateFlow()

    private val genresResult: Flow<Result<List<Genre>>> =
        genresUseCase.resultFlow(param = GenresUseCase.Param())

    init {
        viewModelScope.launch {
            genresResult.collect { result ->
                _viewState.value = when (result) {
                    Result.Loading -> ViewState.Loading
                    is Result.Success -> ViewState.Success(data = result.data)
                    is Result.Error -> ViewState.Error(error = result.exception)
                }
            }
        }
    }
}