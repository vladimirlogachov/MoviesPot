package com.vlohachov.shared.domain.core

import com.vlohachov.shared.domain.Result
import kotlinx.coroutines.flow.Flow

public interface UseCase<in Param, out Output> {

    public operator fun invoke(param: Param): Flow<Result<Output>>

}
