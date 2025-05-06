package com.vlohachov.shared.presentation.ui.screen

import androidx.navigation.NamedNavArgument
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.savedstate.SavedState
import androidx.savedstate.SavedStateReader
import androidx.savedstate.read

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

    /**
     * Calls the specified function [block] with a [SavedStateReader] value as its receiver and returns
     * the [block] value.
     *
     * **IMPORTANT:** The [SavedStateReader] passed as a receiver to the [block] is valid only inside
     * that function. Using it outside of the function may produce an unspecified behavior.
     *
     * @param block A lambda function that performs read operations using the [SavedStateReader].
     * @param lazyMessage A function that returns a message to be used if the [SavedState] is null.
     * @return The result of the lambda function's execution.
     */
    protected inline fun <T> SavedState?.readOrThrow(
        block: SavedStateReader.() -> T,
        lazyMessage: () -> Any = { "Missing saved state" },
    ): T = requireNotNull(value = this, lazyMessage = lazyMessage)
        .read(block = block)

}
