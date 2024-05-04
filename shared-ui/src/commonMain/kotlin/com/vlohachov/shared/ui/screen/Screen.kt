package com.vlohachov.shared.ui.screen

internal sealed interface Screen {

    val route: String

    data object Main : Screen {
        override val route: String = "main"
    }

    data object Settings : Screen {
        override val route: String = "settings"
    }

}
