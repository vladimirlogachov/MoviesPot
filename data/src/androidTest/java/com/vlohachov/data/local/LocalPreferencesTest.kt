package com.vlohachov.data.local

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import app.cash.turbine.test
import com.google.common.truth.Truth
import kotlinx.coroutines.test.runTest
import org.junit.Test

class LocalPreferencesTest {

    private val context = ApplicationProvider.getApplicationContext<Context>()

    private val preferences = LocalPreferences(context = context)

    @Test
    fun `dynamic theme available`() {
        val actual = preferences.isDynamicThemeAvailable

        Truth.assertThat(actual).isTrue()
    }

    @Test
    fun `get dynamic theme`() = runTest {
        preferences.applyDynamicTheme.test {
            val actual = awaitItem()

            Truth.assertThat(actual).isNotNull()
        }
    }

    @Test
    fun `apply dynamic theme`() = runTest {
        preferences.applyDynamicTheme.test {
            val previous = awaitItem()

            preferences.applyDynamicTheme(apply = !previous)

            val actual = awaitItem()

            Truth.assertThat(actual).isNotEqualTo(previous)
        }
    }
}
