package com.vlohachov.shared.domain.usecase.settings

import com.vlohachov.shared.domain.Result
import com.vlohachov.shared.domain.asResult
import com.vlohachov.shared.domain.model.settings.Settings
import com.vlohachov.shared.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow

public class LoadSettings(
    private val repository: SettingsRepository,
) {

    public operator fun invoke(): Flow<Result<Settings>> =
        repository.getSettings()
            .asResult()

}
