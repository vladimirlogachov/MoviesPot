package com.vlohachov.shared.presentation.ui.screen.credits.crew

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import coil3.compose.rememberAsyncImagePainter
import com.vlohachov.shared.domain.model.movie.credit.CrewMember
import com.vlohachov.shared.presentation.core.ViewState
import com.vlohachov.shared.presentation.ui.component.Profile
import com.vlohachov.shared.presentation.ui.component.bar.AppBar
import com.vlohachov.shared.presentation.ui.component.bar.ErrorBar
import com.vlohachov.shared.presentation.ui.component.bar.ErrorBarDefaults
import com.vlohachov.shared.presentation.ui.component.button.ScrollToTop
import com.vlohachov.shared.presentation.ui.screen.Screen
import moviespot.shared_presentation.generated.resources.Res
import moviespot.shared_presentation.generated.resources.crew
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.core.parameter.parametersOf

internal data object CrewScreen : Screen<CrewScreen.Params>() {

    internal data class Params(val movieId: Long)

    private const val ArgMovieId = "movieId"

    override val path: String = "credits/crew?$ArgMovieId={$ArgMovieId}"

    override val arguments: List<NamedNavArgument> = listOf(
        navArgument(name = ArgMovieId) { type = NavType.LongType }
    )

    override fun route(params: Params): String =
        path.replace(oldValue = "{$ArgMovieId}", newValue = params.movieId.toString())

    override fun NavGraphBuilder.composable(navController: NavController) {
        composable(route = path, arguments = arguments) { backStackEntry ->
            val movieId = backStackEntry.arguments.readOrThrow(
                block = { getLong(ArgMovieId) },
                lazyMessage = { "Missing required argument $ArgMovieId" },
            )

            Crew(movieId = movieId, onBack = navController::navigateUp)
        }
    }

}

private const val VISIBLE_ITEMS_THRESHOLD = 3

@OptIn(ExperimentalMaterial3Api::class, KoinExperimentalAPI::class)
@Composable
internal fun Crew(
    movieId: Long,
    onBack: () -> Unit,
    viewModel: CrewViewModel = koinViewModel { parametersOf(movieId) },
    snackbarDuration: SnackbarDuration = SnackbarDuration.Short,
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val gridState = rememberLazyGridState()
    val showScrollToTop by remember {
        derivedStateOf { gridState.firstVisibleItemIndex > VISIBLE_ITEMS_THRESHOLD }
    }
    val crewState by viewModel.crew.collectAsState()
    val error by viewModel.error.collectAsState()
    ErrorBar(
        error = error,
        duration = snackbarDuration,
        snackbarHostState = snackbarHostState,
        onDismissed = viewModel::onErrorConsumed,
    )
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(connection = scrollBehavior.nestedScrollConnection),
        topBar = {
            AppBar(
                modifier = Modifier.fillMaxWidth(),
                title = stringResource(resource = Res.string.crew),
                scrollBehavior = scrollBehavior,
                onBackClick = onBack,
            )
        },
        floatingActionButton = {
            ScrollToTop(
                modifier = Modifier.navigationBarsPadding(),
                visible = showScrollToTop,
                gridState = gridState
            )
        },
        snackbarHost = {
            SnackbarHost(
                modifier = Modifier
                    .testTag(tag = ErrorBarDefaults.ErrorTestTag)
                    .navigationBarsPadding(),
                hostState = snackbarHostState,
            )
        },
        contentWindowInsets = WindowInsets.ime
    ) { paddingValues ->
        Content(
            modifier = Modifier
                .testTag(tag = CrewDefaults.ContentTestTag)
                .fillMaxSize()
                .padding(paddingValues = paddingValues),
            gridState = gridState,
            viewState = crewState,
            onCredit = { },
            onError = viewModel::onError,
        )
    }
}

@Composable
private fun Content(
    modifier: Modifier,
    gridState: LazyGridState,
    viewState: ViewState<List<CrewMember>>,
    onCredit: (creditId: Long) -> Unit,
    onError: (error: Throwable) -> Unit,
) = LazyVerticalGrid(
    modifier = modifier,
    state = gridState,
    columns = GridCells.Fixed(count = 2),
    contentPadding = PaddingValues(all = 16.dp),
    verticalArrangement = Arrangement.spacedBy(space = 16.dp),
    horizontalArrangement = Arrangement.spacedBy(space = 16.dp),
) {
    when (viewState) {
        ViewState.Loading -> item(span = { GridItemSpan(currentLineSpan = 2) }) {
            Box(
                modifier = Modifier.testTag(tag = CrewDefaults.ContentLoadingTestTag),
                contentAlignment = Alignment.TopCenter,
            ) {
                CircularProgressIndicator()
            }
        }

        is ViewState.Error -> item {
            LaunchedEffect(key1 = viewState.error) {
                viewState.error?.run(onError)
            }
        }

        is ViewState.Success ->
            items(
                items = viewState.data,
                key = { item -> item.name + item.job },
            ) { member ->
                var error by remember { mutableStateOf(false) }
                val painter = rememberAsyncImagePainter(
                    model = member.profilePath,
                    onError = { error = true },
                )

                Profile(
                    modifier = Modifier
                        .width(width = 164.dp)
                        .aspectRatio(ratio = 0.75f),
                    title = member.name,
                    body = member.job,
                    painter = painter,
                    onClick = { onCredit(member.id) },
                    error = error,
                )
            }
    }
}

internal object CrewDefaults {

    const val ContentTestTag = "content"
    const val ContentLoadingTestTag = "content_loading"

}
