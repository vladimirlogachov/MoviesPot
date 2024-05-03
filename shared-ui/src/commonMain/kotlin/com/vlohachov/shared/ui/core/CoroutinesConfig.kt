package com.vlohachov.shared.ui.core

import kotlinx.coroutines.flow.SharingStarted

private const val StopTimeoutMillis: Long = 5000

internal val WhileUiSubscribed: SharingStarted = SharingStarted.WhileSubscribed(StopTimeoutMillis)
