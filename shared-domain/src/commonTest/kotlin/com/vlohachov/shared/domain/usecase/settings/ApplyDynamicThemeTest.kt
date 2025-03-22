package com.vlohachov.shared.domain.usecase.settings

import com.vlohachov.shared.domain.repository.SettingsRepository
import dev.mokkery.answering.returns
import dev.mokkery.everySuspend
import dev.mokkery.matcher.any
import dev.mokkery.mock
import dev.mokkery.verify.VerifyMode
import dev.mokkery.verifySuspend
import kotlinx.coroutines.test.runTest
import kotlin.js.JsName
import kotlin.test.Test

class ApplyDynamicThemeTest {

    private companion object {
        val TestParam = ApplyDynamicTheme.Param(apply = true)
    }

    private val repository = mock<SettingsRepository>()

    private val useCase = ApplyDynamicTheme(repository = repository)

    @Test
    @JsName(name = "check_applies_dynamic_theme")
    fun `check applies dynamic theme`() = runTest {
        everySuspend { repository.applyDynamicTheme(apply = any()) } returns Unit

        useCase(param = TestParam)

        verifySuspend(mode = VerifyMode.atMost(n = 1)) {
            repository.applyDynamicTheme(apply = any())
        }
    }

}
