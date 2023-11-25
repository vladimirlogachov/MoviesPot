package com.vlohachov.domain.usecase.settings

import com.vlohachov.domain.Result
import com.vlohachov.domain.asResult
import com.vlohachov.domain.core.UseCase
import com.vlohachov.domain.model.settings.Settings
import com.vlohachov.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow

class LoadSettings(
    private val repository: SettingsRepository,
) : UseCase<Unit, Settings> {

    override fun invoke(param: Unit): Flow<Result<Settings>> =
        repository.getSettings()
            .asResult()

}
