package com.vlohachov.shared.ui.state

import com.vlohachov.shared.domain.Result
import com.vlohachov.shared.domain.model.PaginatedData

public sealed class ViewState<out T> {
    public data object Loading : ViewState<Nothing>()
    public data class Success<T>(val data: T) : ViewState<T>()
    public data class Error(val error: Throwable?) : ViewState<Nothing>()
}

public fun <T> Result<T>.toViewState(): ViewState<T> = when (this) {
    Result.Loading -> ViewState.Loading
    is Result.Error -> ViewState.Error(error = this.exception)
    is Result.Success -> ViewState.Success(data = this.value)
}

public fun <T> Result<PaginatedData<T>>.toViewStatePaginated(): ViewState<List<T>> = when (this) {
    Result.Loading -> ViewState.Loading
    is Result.Error -> ViewState.Error(error = this.exception)
    is Result.Success -> ViewState.Success(data = this.value.data)
}
