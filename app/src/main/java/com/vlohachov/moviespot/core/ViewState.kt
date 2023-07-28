package com.vlohachov.moviespot.core

sealed class ViewState<out T> {
    data object Loading : ViewState<Nothing>()
    data class Success<T>(val data: T) : ViewState<T>()
    data class Error(val error: Throwable?) : ViewState<Nothing>()
}
