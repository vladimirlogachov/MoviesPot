package com.vlohachov.shared.ui.core.utils

import kotlin.time.Duration.Companion.hours
import kotlin.time.DurationUnit

internal object TimeUtils {

    private val minutesInHour = 1.hours.toInt(DurationUnit.MINUTES)

    fun hours(runtime: Int): Int = runtime.div(other = minutesInHour)

    fun minutes(runtime: Int): Int = runtime.mod(other = minutesInHour)

}
