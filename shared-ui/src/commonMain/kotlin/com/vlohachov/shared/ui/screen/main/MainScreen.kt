package com.vlohachov.shared.ui.screen.main

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.MovieFilter
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.vlohachov.shared.domain.model.movie.Movie
import com.vlohachov.shared.domain.model.movie.MovieCategory
import com.vlohachov.shared.ui.component.bar.ErrorBar
import com.vlohachov.shared.ui.component.movie.MoviesSection
import com.vlohachov.shared.ui.state.ViewState
import moviespot.shared_ui.generated.resources.Res
import moviespot.shared_ui.generated.resources.app_name
import moviespot.shared_ui.generated.resources.chalkduster
import moviespot.shared_ui.generated.resources.discover
import moviespot.shared_ui.generated.resources.now_playing
import moviespot.shared_ui.generated.resources.popular
import moviespot.shared_ui.generated.resources.search
import moviespot.shared_ui.generated.resources.settings
import moviespot.shared_ui.generated.resources.top_rated
import moviespot.shared_ui.generated.resources.upcoming
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.Font
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun MainScreen(
    viewModel: MainViewModel = koinInject(),
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val uiState by viewModel.uiState.collectAsState()

    val navController = rememberNavController()

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(connection = scrollBehavior.nestedScrollConnection),
        topBar = {
            MainAppBar(
                modifier = Modifier
                    .fillMaxWidth(),
                scrollBehavior = scrollBehavior,
                navController = navController,
            )
        },
        floatingActionButton = {
            Discover(
                modifier = Modifier.navigationBarsPadding(),
                navController = navController,
            )
        },
        snackbarHost = {
            SnackbarHost(
                modifier = Modifier
                    .semantics {
                        testTag = MainScreenDefaults.ErrorBarTestTag
                    }
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
            viewState = uiState,
            onError = { error ->
                ErrorBar(
                    error = error,
                    snackbarHostState = snackbarHostState,
                    onDismissed = viewModel::onErrorConsumed,
                )
            },
            navController = navController
        )
    }
}

@OptIn(ExperimentalResourceApi::class)
@Composable
private fun Content(
    modifier: Modifier,
    viewState: MainViewState,
    onError: @Composable (error: Throwable) -> Unit,
    navController: NavController,
) {
    val onSeeDetails: (Movie) -> Unit = { movie ->
//        navController.navigate(MovieDetailsDestination(movieId = movie.id, movieTitle = movie.title))
    }
    Box(
        modifier = modifier,
        contentAlignment = Alignment.TopCenter,
    ) {
        LazyColumn(
            modifier = Modifier
                .semantics {
                    testTag = MainScreenDefaults.SectionsTestTag
                }
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            contentPadding = WindowInsets.navigationBars.asPaddingValues(),
        ) {
            items(MovieCategory.entries) { category ->
                MoviesSection(
                    modifier = Modifier.fillMaxWidth(),
                    title = stringResource(resource = category.stringRes()),
                    viewState = viewState.moviesState(category = category),
                    onMovieClick = onSeeDetails,
                    onMore = {
//                        navigator.navigate(MoviesDestination(category = category))
                    },
                )
            }
        }

        viewState.error?.run {
            onError(this)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalResourceApi::class)
@Composable
private fun MainAppBar(
    modifier: Modifier,
    scrollBehavior: TopAppBarScrollBehavior,
    navController: NavController,
) {
    CenterAlignedTopAppBar(
        modifier = modifier,
        title = {
            Text(
                text = stringResource(resource = Res.string.app_name),
                fontFamily = FontFamily(Font(resource = Res.font.chalkduster))
            )
        },
        navigationIcon = {
            IconButton(
                modifier = Modifier.testTag(tag = MainScreenDefaults.SearchButtonTestTag),
                onClick = {
//                    navigator.navigate(SearchMoviesDestination)
                },
            ) {
                Icon(
                    imageVector = Icons.Rounded.Search,
                    contentDescription = stringResource(resource = Res.string.search),
                )
            }
        },
        actions = {
            IconButton(
                modifier = Modifier.testTag(tag = MainScreenDefaults.SettingsButtonTestTag),
                onClick = {
//                    navigator.navigate(SettingsDestination)
                },
            ) {
                Icon(
                    imageVector = Icons.Rounded.Settings,
                    contentDescription = stringResource(resource = Res.string.settings),
                )
            }
        },
        scrollBehavior = scrollBehavior,
    )
}

@OptIn(ExperimentalResourceApi::class)
@Composable
private fun Discover(
    modifier: Modifier,
    navController: NavController,
) {
    ExtendedFloatingActionButton(
        modifier = modifier.testTag(tag = MainScreenDefaults.DiscoverButtonTestTag),
        text = { Text(text = stringResource(resource = Res.string.discover)) },
        icon = {
            Icon(
                imageVector = Icons.Rounded.MovieFilter,
                contentDescription = stringResource(resource = Res.string.discover),
            )
        },
        onClick = {
//            navController.navigate(DiscoverDestination)
        },
    )
}

@OptIn(ExperimentalResourceApi::class)
private fun MovieCategory.stringRes(): StringResource = when (this) {
    MovieCategory.UPCOMING -> Res.string.upcoming
    MovieCategory.NOW_PLAYING -> Res.string.now_playing
    MovieCategory.POPULAR -> Res.string.popular
    MovieCategory.TOP_RATED -> Res.string.top_rated
}

private fun MainViewState.moviesState(category: MovieCategory): ViewState<List<Movie>> =
    when (category) {
        MovieCategory.UPCOMING -> this.upcomingViewState
        MovieCategory.NOW_PLAYING -> this.nowPlayingViewState
        MovieCategory.POPULAR -> this.popularViewState
        MovieCategory.TOP_RATED -> this.topRatedViewState
    }

internal object MainScreenDefaults {

    const val SettingsButtonTestTag = "settings_button"
    const val SearchButtonTestTag = "search_button"
    const val DiscoverButtonTestTag = "discover_button"
    const val ErrorBarTestTag = "error_bar"
    const val SectionsTestTag = "movies_sections"

}
