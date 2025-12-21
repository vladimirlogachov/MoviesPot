package com.vlohachov.shared.presentation

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestCoroutineScheduler
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

@OptIn(ExperimentalCoroutinesApi::class)
fun runViewModelTest(
    context: CoroutineContext = EmptyCoroutineContext,
    timeout: Duration = 60.seconds,
    dispatcher: (TestCoroutineScheduler) -> TestDispatcher = { scheduler ->
        StandardTestDispatcher(scheduler = scheduler)
    },
    block: suspend TestScope.() -> Unit,
) = runTest(context = context, timeout = timeout) {
    Dispatchers.setMain(dispatcher = dispatcher(testScheduler))
    try {
        block()
    } finally {
        Dispatchers.resetMain()
    }
}
