package com.vlohachov.shared.domain.usecase.settings

import com.vlohachov.shared.domain.repository.SettingsRepository

public class ApplyDynamicTheme(
    private val repository: SettingsRepository,
) {

    public data class Param(val apply: Boolean)

    public suspend operator fun invoke(param: Param): Unit =
        repository.applyDynamicTheme(apply = param.apply)

}
