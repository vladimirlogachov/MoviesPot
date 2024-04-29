package com.vlohachov.shared.data.local

import android.os.Build

internal actual fun isDynamicThemeAvailable(): Boolean =
    Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
