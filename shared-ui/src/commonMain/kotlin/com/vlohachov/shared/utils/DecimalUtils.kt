package com.vlohachov.shared.utils

internal object DecimalUtils {

    fun format(number: Float, digitsAfterComma: Int = 1): String =
        number.toString()
            .split('.')
            .toMutableList()
            .apply { this[1] = this[1].take(digitsAfterComma) }
            .joinToString(separator = ".")

}
