package com.vlohachov.shared.domain.usecase.settings

import com.vlohachov.shared.domain.Result
import com.vlohachov.shared.domain.asResult
import com.vlohachov.shared.domain.core.UseCase
import com.vlohachov.shared.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.onEach

public class ApplyDynamicTheme(
    private val repository: SettingsRepository,
) : UseCase<ApplyDynamicTheme.Param, Boolean> {

    public data class Param(val apply: Boolean)

    override fun invoke(param: Param): Flow<Result<Boolean>> =
        flowOf(value = param.apply)
            .onEach(repository::applyDynamicTheme)
            .asResult()

}
