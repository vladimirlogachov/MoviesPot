package com.vlohachov.moviespot.core.utils

import com.google.common.truth.Truth
import org.junit.Test

class TimeUtilsTest {

    private companion object {
        const val TestRuntime = 3 * 60 + 36
    }

    @Test
    fun `Hours from Int value success`() {
        val expected = 3
        val actual = TimeUtils.hours(runtime = TestRuntime)

        Truth.assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `Minutes from Int value success`() {
        val expected = 36
        val actual = TimeUtils.minutes(runtime = TestRuntime)

        Truth.assertThat(actual).isEqualTo(expected)
    }
}
