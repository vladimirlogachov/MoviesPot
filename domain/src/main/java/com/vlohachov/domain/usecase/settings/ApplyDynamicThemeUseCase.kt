package com.vlohachov.domain.usecase.settings

import com.vlohachov.domain.core.UseCase
import com.vlohachov.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.onEach
import kotlin.coroutines.CoroutineContext

class ApplyDynamicThemeUseCase(
    coroutineContext: CoroutineContext,
    private val repository: SettingsRepository,
) : UseCase<ApplyDynamicThemeUseCase.Param, Boolean>(coroutineContext = coroutineContext) {

    data class Param(val apply: Boolean)

    override fun execute(param: Param): Flow<Boolean> {
        return flowOf(value = param.apply).onEach(repository::applyDynamicTheme)
    }
}
