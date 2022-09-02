package com.vlohachov.moviespot.core.utils

import java.util.concurrent.TimeUnit

object TimeUtils {

    fun hours(runtime: Int): Int = runtime.div(TimeUnit.HOURS.toMinutes(1)).toInt()

    fun minutes(runtime: Int): Int = runtime.mod(TimeUnit.HOURS.toMinutes(1)).toInt()
}