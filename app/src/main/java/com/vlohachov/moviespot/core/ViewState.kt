package com.vlohachov.moviespot.core

import com.vlohachov.domain.Result
import com.vlohachov.domain.model.PaginatedData

sealed class ViewState<out T> {
    data object Loading : ViewState<Nothing>()
    data class Success<T>(val data: T) : ViewState<T>()
    data class Error(val error: Throwable?) : ViewState<Nothing>()
}

fun <T> Result<T>.toViewState(): ViewState<T> = when (this) {
    Result.Loading -> ViewState.Loading
    is Result.Error -> ViewState.Error(error = this.exception)
    is Result.Success -> ViewState.Success(data = this.value)
}

fun <T> Result<PaginatedData<T>>.toViewStatePaginated(): ViewState<List<T>> = when (this) {
    Result.Loading -> ViewState.Loading
    is Result.Error -> ViewState.Error(error = this.exception)
    is Result.Success -> ViewState.Success(data = this.value.data)
}
