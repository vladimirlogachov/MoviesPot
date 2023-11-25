package com.vlohachov.domain.core

import com.vlohachov.domain.Result
import kotlinx.coroutines.flow.Flow

interface UseCase<in Param, out Output> {
    operator fun invoke(param: Param): Flow<Result<Output>>
}
