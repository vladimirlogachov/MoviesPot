package com.vlohachov.moviespot.core.utils

import com.google.common.truth.Truth
import org.junit.Test
import java.time.format.DateTimeParseException

class DateUtilsTest {

    private companion object {
        const val TestRemoteDate = "2022-08-24"
        const val TestReversedRemoteDate = "24-08-2022"
    }

    @Test
    fun `Valid remote to default local format success`() {
        val expected = "24 Aug 2022"

        val actual = DateUtils.format(date = TestRemoteDate)

        Truth.assertThat(actual).isEqualTo(expected)
    }

    @Test(expected = DateTimeParseException::class)
    fun `Wrong remote to default local format exception`() {
        DateUtils.format(date = TestReversedRemoteDate)
    }

    @Test
    fun `Valid remote to given format success`() {
        val actual = DateUtils.format(date = TestRemoteDate, pattern = "dd-MM-yyyy")

        Truth.assertThat(actual).isEqualTo(TestReversedRemoteDate)
    }
}