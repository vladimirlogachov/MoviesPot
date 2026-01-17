package com.vlohachov.shared.presentation.utils

import kotlinx.datetime.LocalDate
import kotlinx.datetime.format
import kotlinx.datetime.format.DateTimeFormat
import kotlinx.datetime.format.MonthNames
import kotlinx.datetime.format.Padding
import kotlinx.datetime.format.char

internal object DateUtils {

    private val RemoteDateFormat = LocalDate.Format {
        year()
        char('-')
        monthNumber(padding = Padding.SPACE)
        char('-')
        day()
    }
    private val LocalDateFormat: DateTimeFormat<LocalDate> = LocalDate.Format {
        day()
        char(' ')
        monthName(names = MonthNames.ENGLISH_ABBREVIATED)
        char(' ')
        year()
    }

    fun format(date: String, format: DateTimeFormat<LocalDate> = LocalDateFormat): String =
        parse(date = date).format(format = format)

    private fun parse(
        date: String,
        format: DateTimeFormat<LocalDate> = RemoteDateFormat,
    ): LocalDate =
        LocalDate.parse(input = date, format = format)

}
