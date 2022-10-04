package com.vlohachov.domain.usecase.settings

import com.vlohachov.domain.core.UseCase
import com.vlohachov.domain.model.settings.Settings
import com.vlohachov.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow
import kotlin.coroutines.CoroutineContext

class GetSettingsUseCase(
    coroutineContext: CoroutineContext,
    private val repository: SettingsRepository,
) : UseCase<Unit, Settings>(coroutineContext = coroutineContext) {

    override fun execute(param: Unit): Flow<Settings> {
        return repository.getSettings()
    }
}
