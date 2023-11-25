package com.vlohachov.domain.usecase.settings

import com.vlohachov.domain.Result
import com.vlohachov.domain.asResult
import com.vlohachov.domain.core.UseCase
import com.vlohachov.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.onEach

class ApplyDynamicTheme(
    private val repository: SettingsRepository,
) : UseCase<ApplyDynamicTheme.Param, Boolean> {

    data class Param(val apply: Boolean)

    override fun invoke(param: Param): Flow<Result<Boolean>> =
        flowOf(value = param.apply).onEach(repository::applyDynamicTheme)
            .asResult()

}
