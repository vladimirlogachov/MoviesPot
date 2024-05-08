package com.vlohachov.shared.utils

import kotlin.js.JsName
import kotlin.test.Test
import kotlin.test.expect

class TimeUtilsTest {

    private companion object {

        const val TestRuntime = 3 * 60 + 36

    }

    @Test
    @JsName(name = "hours_from_Int_value")
    fun `hours from Int value success`() {
        expect(expected = 3) {
            TimeUtils.hours(runtime = TestRuntime)
        }
    }

    @Test
    @JsName(name = "minutes_from_Int_value")
    fun `minutes from Int value success`() {
        expect(expected = 36) {
            TimeUtils.minutes(runtime = TestRuntime)
        }
    }

}
