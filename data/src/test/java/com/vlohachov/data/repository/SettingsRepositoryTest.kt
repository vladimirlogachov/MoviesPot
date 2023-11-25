package com.vlohachov.data.repository

import com.google.common.truth.Truth
import com.vlohachov.data.data.TestSettings
import com.vlohachov.data.local.LocalPreferences
import io.mockk.coEvery
import io.mockk.coJustRun
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Test

class SettingsRepositoryTest {

    private val preferences = mockk<LocalPreferences>()

    private val repository = SettingsRepositoryImpl(preferences = preferences)

    @Test
    fun `settings loading success`() = runTest {
        val expected = TestSettings

        every { preferences.applyDynamicTheme } returns flowOf(value = TestSettings.dynamicTheme)
        every { preferences.isDynamicThemeAvailable } returns TestSettings.supportsDynamicTheme

        val actual = repository.getSettings().first()

        Truth.assertThat(actual).isEqualTo(expected)
    }

    @Test(expected = NullPointerException::class)
    fun `settings loading failure`() = runTest {
        every { preferences.applyDynamicTheme } returns flow { throw NullPointerException() }
        every { preferences.isDynamicThemeAvailable } returns TestSettings.supportsDynamicTheme

        repository.getSettings().first()
    }

    @Test
    fun `update dynamic theme success`() = runTest {
        coJustRun { preferences.applyDynamicTheme(apply = any()) }

        repository.applyDynamicTheme(apply = true)

        coVerify(exactly = 1) { preferences.applyDynamicTheme(apply = any()) }
    }

    @Test(expected = NullPointerException::class)
    fun `update dynamic theme failure`() = runTest {
        coEvery { preferences.applyDynamicTheme(apply = any()) } throws NullPointerException()

        repository.applyDynamicTheme(apply = true)
    }
}
