package com.vlohachov.shared.data.repository

import app.cash.turbine.test
import com.russhwolf.settings.Settings
import com.vlohachov.shared.data.local.LocalPreferences
import com.vlohachov.shared.data.local.isDynamicThemeAvailable
import dev.mokkery.answering.returns
import dev.mokkery.answering.throws
import dev.mokkery.every
import dev.mokkery.matcher.any
import dev.mokkery.mock
import dev.mokkery.verify
import dev.mokkery.verify.VerifyMode.Companion.atMost
import kotlinx.coroutines.test.runTest
import kotlin.js.JsName
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class LocalSettingsRepositoryTest {

    private val settings = mock<Settings> {
        every { getBooleanOrNull(key = any()) } returns null
        every { putBoolean(key = any(), value = any()) } returns Unit
    }

    private val preferences = LocalPreferences(settings = settings)
    private val repository = LocalSettingsRepository(preferences = preferences)

    @Test
    @JsName("settings_loading_success")
    fun `settings loading success`() = runTest {
        repository.getSettings().test {
            with(awaitItem()) {
                assertEquals(expected = isDynamicThemeAvailable(), actual = dynamicTheme)
                assertEquals(expected = isDynamicThemeAvailable(), actual = supportsDynamicTheme)
            }
        }
    }

    @Test
    @JsName("update_dynamic_theme_success")
    fun `update dynamic theme success`() = runTest {
        repository.applyDynamicTheme(apply = true)

        repository.getSettings().test {
            assertTrue(actual = awaitItem().dynamicTheme)
        }

        verify(mode = atMost(n = 1)) { settings.putBoolean(key = any(), value = true) }
    }

    @Test
    @JsName("update_dynamic_theme_failure")
    fun `update dynamic theme failure`() = runTest {
        every {
            settings.putBoolean(key = any(), value = any())
        } throws UnsupportedOperationException()

        assertFailsWith(exceptionClass = UnsupportedOperationException::class) {
            repository.applyDynamicTheme(apply = true)
        }
    }

}
