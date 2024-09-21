package com.vlohachov.shared.presentation.ui

import androidx.compose.ui.window.ComposeUIViewController
import platform.UIKit.UIViewController

@Suppress("FunctionName")
public fun MainViewController(): UIViewController = ComposeUIViewController {
    MoviesPotApp()
}
