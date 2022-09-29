package com.vlohachov.domain.core

fun interface ErrorProcessor {
    fun process(e: Throwable): Throwable
}
