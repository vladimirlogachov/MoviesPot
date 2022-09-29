package com.vlohachov.moviespot.core.utils

import com.google.common.truth.Truth
import org.junit.Test

class DecimalUtilsTest {

    @Test
    fun `Decimal default format success`() {
        val expected = "9.7"
        val actual = DecimalUtils.format(number = 9.678f)

        Truth.assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `Decimal given format success`() {
        val expected = "9.68"
        val actual = DecimalUtils.format(number = 9.678f, pattern = "#.##")

        Truth.assertThat(actual).isEqualTo(expected)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `Decimal malformed format success`() {
        DecimalUtils.format(number = 9.678f, pattern = "0#.##")
    }
}
