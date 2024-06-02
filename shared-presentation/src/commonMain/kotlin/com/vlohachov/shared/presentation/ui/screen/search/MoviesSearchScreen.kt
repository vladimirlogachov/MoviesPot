package com.vlohachov.shared.presentation.ui.screen.search

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.vlohachov.shared.domain.model.movie.Movie
import com.vlohachov.shared.presentation.core.collectAsLazyPagingItems
import com.vlohachov.shared.presentation.ui.component.bar.AppBarDefaults
import com.vlohachov.shared.presentation.ui.component.bar.ErrorBar
import com.vlohachov.shared.presentation.ui.component.bar.ErrorBarDefaults
import com.vlohachov.shared.presentation.ui.component.button.ScrollToTop
import com.vlohachov.shared.presentation.ui.component.movie.MoviesPaginatedGrid
import com.vlohachov.shared.presentation.ui.screen.Screen
import com.vlohachov.shared.presentation.ui.screen.details.MovieDetailsScreen
import moviespot.shared_presentation.generated.resources.Res
import moviespot.shared_presentation.generated.resources.clear
import moviespot.shared_presentation.generated.resources.navigate_back
import moviespot.shared_presentation.generated.resources.search
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject

internal data object MoviesSearchScreen : Screen<Unit>() {

    override val path: String = "search"

    override val arguments: List<NamedNavArgument> = emptyList()

    override fun route(params: Unit): String = path

    @OptIn(ExperimentalMaterial3Api::class)
    override fun NavGraphBuilder.composable(navController: NavController) {
        composable(route = path, arguments = arguments) {
            MoviesSearch(
                onBack = navController::navigateUp,
                onMovieDetails = { movie ->
                    MovieDetailsScreen.Params(movieId = movie.id, movieTitle = movie.title)
                        .run(MovieDetailsScreen::route)
                        .run(navController::navigate)
                },
            )
        }
    }

}

private const val VISIBLE_ITEMS_THRESHOLD = 3
private const val COLOR_TRANSITION_FRACTION_THRESHOLD = 0.01f

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun MoviesSearch(
    onBack: () -> Unit,
    onMovieDetails: (Movie) -> Unit,
    viewModel: MoviesSearchViewModel = koinInject(),
    gridState: LazyGridState = rememberLazyGridState(),
    snackbarDuration: SnackbarDuration = SnackbarDuration.Short,
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
    scrollBehavior: TopAppBarScrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(),
    keyboardController: SoftwareKeyboardController? = LocalSoftwareKeyboardController.current,
) {
    val showScrollToTop by remember {
        derivedStateOf { gridState.firstVisibleItemIndex > VISIBLE_ITEMS_THRESHOLD }
    }
    val search by viewModel.search.collectAsState()
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
            SearchAppBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp, horizontal = 16.dp),
                value = search,
                onValueChange = viewModel::onSearch,
                onBack = onBack,
                onClear = viewModel::onClear,
                scrollBehavior = scrollBehavior,
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
                .fillMaxSize()
                .padding(paddingValues = paddingValues),
            viewModel = viewModel,
            gridState = gridState,
            onMovieClick = { movie ->
                keyboardController?.hide()
                onMovieDetails(movie)
            },
        )
    }
}

@Composable
private fun Content(
    modifier: Modifier,
    viewModel: MoviesSearchViewModel,
    gridState: LazyGridState,
    onMovieClick: (Movie) -> Unit,
) {
    Box(modifier = modifier) {
        MoviesPaginatedGrid(
            modifier = Modifier.fillMaxSize(),
            state = gridState,
            columns = GridCells.Fixed(count = 3),
            movies = viewModel.movies.collectAsLazyPagingItems(),
            onClick = onMovieClick,
            onError = viewModel::onError,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SearchAppBar(
    value: String,
    onValueChange: (value: String) -> Unit,
    onBack: () -> Unit,
    onClear: () -> Unit,
    modifier: Modifier = Modifier,
    scrollBehavior: TopAppBarScrollBehavior,
    appBarColors: TopAppBarColors = TopAppBarDefaults.centerAlignedTopAppBarColors(),
) {
    val heightOffsetLimit = with(LocalDensity.current) { -64.dp.toPx() }
    SideEffect {
        if (scrollBehavior.state.heightOffsetLimit != heightOffsetLimit) {
            scrollBehavior.state.heightOffsetLimit = heightOffsetLimit
        }
    }
    val colorTransitionFraction = scrollBehavior.state.overlappedFraction
    val appBarContainerColor by animateAppBarColor(
        fraction = if (colorTransitionFraction > COLOR_TRANSITION_FRACTION_THRESHOLD) 1f else 0f,
        appBarColors = appBarColors
    )
    Surface(color = appBarContainerColor) {
        SearchBar(
            modifier = modifier.testTag(tag = SearchMoviesDefaults.SearchBarTestTag),
            query = value,
            onQueryChange = onValueChange,
            active = false,
            onActiveChange = { },
            onSearch = { },
            placeholder = { Text(text = stringResource(resource = Res.string.search)) },
            leadingIcon = {
                IconButton(
                    modifier = Modifier.testTag(tag = AppBarDefaults.BackButtonTestTag),
                    onClick = onBack
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                        contentDescription = stringResource(resource = Res.string.navigate_back),
                    )
                }
            },
            trailingIcon = {
                AnimatedVisibility(
                    visible = value.isNotBlank(),
                    enter = fadeIn() + scaleIn(),
                    exit = fadeOut() + scaleOut(),
                ) {
                    IconButton(
                        modifier = Modifier.testTag(tag = SearchMoviesDefaults.ClearSearchBarTestTag),
                        onClick = onClear,
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Clear,
                            contentDescription = stringResource(resource = Res.string.clear),
                        )
                    }
                }
            },
            content = {},
            colors = SearchBarDefaults.colors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun animateAppBarColor(fraction: Float, appBarColors: TopAppBarColors): State<Color> =
    animateColorAsState(
        targetValue = lerp(
            start = appBarColors.containerColor,
            stop = appBarColors.scrolledContainerColor,
            fraction = FastOutLinearInEasing.transform(fraction)
        ),
        animationSpec = spring(stiffness = Spring.StiffnessMediumLow),
        label = "anim_app_bar_container_color"
    )

internal object SearchMoviesDefaults {

    const val SearchBarTestTag = "search_bar"
    const val ClearSearchBarTestTag = "clear_search_bar"

}
