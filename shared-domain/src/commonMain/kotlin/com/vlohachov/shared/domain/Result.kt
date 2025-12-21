package com.vlohachov.shared.domain

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart

public sealed interface Result<out T> {
    public data class Success<T>(val value: T) : Result<T>
    public data class Error(val exception: Throwable) : Result<Nothing>
    public data object Loading : Result<Nothing>
}

public fun <T> Flow<T>.asResult(): Flow<Result<T>> =
    map<T, Result<T>> { value -> Result.Success(value = value) }
        .onStart { emit(value = Result.Loading) }
        .catch { exception -> emit(value = Result.Error(exception = exception)) }
