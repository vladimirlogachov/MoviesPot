package com.vlohachov.shared.presentation.ui.screen.image

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.BrokenImage
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import coil3.compose.rememberAsyncImagePainter
import com.vlohachov.shared.presentation.ui.component.bar.AppBar
import com.vlohachov.shared.presentation.ui.screen.Screen
import moviespot.shared_presentation.generated.resources.Res
import moviespot.shared_presentation.generated.resources.image_loading_failed
import org.jetbrains.compose.resources.stringResource

internal data object FullscreenImageScreen : Screen<FullscreenImageScreen.Params>() {

    internal data class Params(val path: String)

    private const val ArgPath = "path"

    override val path: String = "image?$ArgPath={$ArgPath}"

    override val arguments: List<NamedNavArgument> = listOf(
        navArgument(name = ArgPath) { type = NavType.StringType }
    )

    override fun route(params: Params): String =
        path.replace(oldValue = "{$ArgPath}", newValue = params.path)

    override fun NavGraphBuilder.composable(navController: NavController) {
        composable(route = path, arguments = arguments) { backStackEntry ->
            val imagePath = requireNotNull(value = backStackEntry.arguments?.getString(ArgPath)) {
                "Missing required argument $ArgPath"
            }

            FullscreenImage(path = imagePath, onBack = navController::navigateUp)
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun FullscreenImage(
    path: String,
    onBack: () -> Unit,
) = Scaffold(
    modifier = Modifier.fillMaxSize(),
    topBar = {
        AppBar(
            modifier = Modifier.fillMaxWidth(),
            title = "",
            onBackClick = onBack,
        )
    }
) { paddingValues ->
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues = paddingValues),
        contentAlignment = Alignment.Center,
    ) {
        var error by remember { mutableStateOf(false) }
        val painter = rememberAsyncImagePainter(
            model = path,
            onError = { error = true },
        )

        Image(
            modifier = Modifier.fillMaxWidth(),
            painter = painter,
            contentDescription = path,
        )

        if (error) {
            Icon(
                modifier = Modifier
                    .size(size = 96.dp)
                    .align(alignment = Alignment.Center),
                imageVector = Icons.Rounded.BrokenImage,
                contentDescription = stringResource(resource = Res.string.image_loading_failed),
            )
        }
    }
}
