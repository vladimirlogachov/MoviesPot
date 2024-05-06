package com.vlohachov.shared.data.local

import app.cash.turbine.test
import com.russhwolf.settings.Settings
import dev.mokkery.answering.returns
import dev.mokkery.every
import dev.mokkery.matcher.any
import dev.mokkery.mock
import kotlinx.coroutines.test.runTest
import kotlin.js.JsName
import kotlin.test.Test
import kotlin.test.assertEquals

class LocalPreferencesTest {

    private val settings = mock<Settings> {
        every { getBooleanOrNull(key = any()) } returns null
        every { putBoolean(key = any(), value = any()) } returns Unit
    }

    private val preferences = LocalPreferences(settings = settings)

    @Test
    @JsName("return_default_when_storage_empty")
    fun `return default when storage empty`() = runTest {
        preferences.applyDynamicThemeFlow.test {
            assertEquals(expected = false, actual = awaitItem())
        }
    }

    @Test
    @JsName("return_default_when_storage_not_empty")
    fun `flow emits value after apply`() = runTest {
        preferences.applyDynamicTheme(apply = true)
        preferences.applyDynamicThemeFlow.test {
            assertEquals(expected = true, actual = expectMostRecentItem())
        }
    }

}
