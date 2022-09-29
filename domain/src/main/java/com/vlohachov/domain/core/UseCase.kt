package com.vlohachov.domain.core

import com.vlohachov.domain.Result
import com.vlohachov.domain.asResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlin.coroutines.CoroutineContext

abstract class UseCase<in Param, out Output>(private val coroutineContext: CoroutineContext) {

    protected abstract fun execute(param: Param): Flow<Output>

    fun resultFlow(param: Param): Flow<Result<Output>> =
        execute(param)
            .asResult()
            .flowOn(coroutineContext)
}
