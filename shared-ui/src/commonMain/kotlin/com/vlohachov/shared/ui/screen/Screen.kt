package com.vlohachov.shared.ui.screen

import androidx.compose.runtime.Composable
import com.vlohachov.shared.ui.screen.main.MainScreen

internal sealed interface Screen<Param> {

    val route: String

    @Composable
    fun Content(param: Param)

    data object Main : Screen<Nothing> {

        override val route: String = "main"

        @Composable
        override fun Content(param: Nothing) = MainScreen()

    }

}
