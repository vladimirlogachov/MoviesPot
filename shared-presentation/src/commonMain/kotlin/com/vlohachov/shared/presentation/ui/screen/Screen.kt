package com.vlohachov.shared.presentation.ui.screen

import androidx.navigation.NamedNavArgument
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder

/**
 * Abstract class representing a screen in a navigation graph.
 *
 * @param Params The type of parameters that can be passed to the screen.
 */
internal abstract class Screen<in Params> {

    /**
     * The path of the screen in the navigation graph.
     */
    protected abstract val path: String

    /**
     * The list of named navigation arguments that the screen accepts.
     */
    protected abstract val arguments: List<NamedNavArgument>

    /**
     * Generates the route for the screen based on the given parameters.
     *
     * @param params The parameters to use for route generation.
     * @return The generated route.
     */
    abstract fun route(params: Params): String

    /**
     * Adds the screen to the navigation graph.
     *
     * @param navController The NavController used to perform navigation to the other screens.
     */
    abstract fun NavGraphBuilder.composable(navController: NavController)

}
