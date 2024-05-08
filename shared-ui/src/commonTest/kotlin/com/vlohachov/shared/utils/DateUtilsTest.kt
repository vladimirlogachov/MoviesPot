package com.vlohachov.shared.utils

import kotlinx.datetime.LocalDate
import kotlinx.datetime.format.char
import kotlin.js.JsName
import kotlin.test.Test
import kotlin.test.assertFails
import kotlin.test.expect

class DateUtilsTest {

    private companion object {

        const val TestRemoteDate = "2022-08-24"
        const val TestReversedRemoteDate = "24-08-2022"

    }

    @Test
    @JsName(name = "valid_remote_to_default_local_format_success")
    fun `valid remote to default local format success`() {
        expect(expected = "24 Aug 2022") {
            DateUtils.format(date = TestRemoteDate)
        }
    }

    @Test
    @JsName(name = "wrong_remote_to_default_local_format_exception")
    fun `wrong remote to default local format exception`() {
        assertFails {
            DateUtils.format(date = TestReversedRemoteDate)
        }
    }

    @Test
    @JsName(name = "valid_remote_to_given_format_success")
    fun `valid remote to given format success`() {
        val format = LocalDate.Format {
            dayOfMonth()
            char('-')
            monthNumber()
            char('-')
            year()
        }

        expect(expected = TestReversedRemoteDate) {
            DateUtils.format(date = TestRemoteDate, format = format)
        }
    }

}
