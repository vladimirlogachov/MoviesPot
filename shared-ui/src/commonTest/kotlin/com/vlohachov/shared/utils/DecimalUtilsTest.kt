package com.vlohachov.shared.utils

import kotlin.js.JsName
import kotlin.test.Test
import kotlin.test.assertFails
import kotlin.test.expect

class DecimalUtilsTest {

    @Test
    @JsName(name = "decimal_default_format_success")
    fun `decimal default format success`() {
        expect(expected = "9.6") {
            DecimalUtils.format(number = 9.678f)
        }
    }

    @Test
    @JsName(name = "decimal_given_format_success")
    fun `decimal given format success`() {
        expect(expected = "9.67") {
            DecimalUtils.format(number = 9.678f, digitsAfterComma = 2)
        }
    }

    @Test
    @JsName(name = "decimal_malformed_format_success")
    fun `decimal malformed format success`() {
        assertFails {
            DecimalUtils.format(number = 9.678f, digitsAfterComma = -1)
        }
    }

}
