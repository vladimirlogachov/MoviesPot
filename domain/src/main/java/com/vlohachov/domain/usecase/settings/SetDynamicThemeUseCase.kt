package com.vlohachov.domain.usecase.settings

import com.vlohachov.domain.core.UseCase
import com.vlohachov.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.onEach
import kotlin.coroutines.CoroutineContext

class SetDynamicThemeUseCase(
    coroutineContext: CoroutineContext,
    private val repository: SettingsRepository,
) : UseCase<SetDynamicThemeUseCase.Param, Boolean>(coroutineContext = coroutineContext) {

    data class Param(val dynamicTheme: Boolean)

    override fun execute(param: Param): Flow<Boolean> {
        return flowOf(value = param.dynamicTheme).onEach(repository::applyDynamicTheme)
    }
}
