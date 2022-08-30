package com.vlohachov.moviespot.core

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

object DateUtils {

    private const val REMOTE_DATE_PATTERN = "yyyy-MM-dd"
    private const val LOCAL_DATE_PATTERN = "d MMM yyyy"

    const val YEAR = "yyyy"

    fun format(date: String, pattern: String = LOCAL_DATE_PATTERN): String =
        parse(date = date).format(DateTimeFormatter.ofPattern(pattern))

    private fun parse(date: String, pattern: String = REMOTE_DATE_PATTERN): LocalDate =
        LocalDate.parse(date, DateTimeFormatter.ofPattern(pattern, Locale.getDefault()))
}
