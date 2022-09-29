package com.vlohachov.moviespot.core.utils

import java.text.DecimalFormat

object DecimalUtils {

    private const val DEFAULT_PATTERN = "#.#"

    fun format(number: Float, pattern: String = DEFAULT_PATTERN): String =
        DecimalFormat(pattern).format(number)
}
