package com.vlohachov.moviespot.ui.main

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.vlohachov.domain.model.movie.Movie
import com.vlohachov.domain.model.movie.MovieCategory
import com.vlohachov.moviespot.R
import com.vlohachov.moviespot.core.ViewState
import com.vlohachov.moviespot.ui.components.bar.ErrorBar
import com.vlohachov.moviespot.ui.components.movie.MoviesSection
import com.vlohachov.moviespot.ui.destinations.DiscoverDestination
import com.vlohachov.moviespot.ui.destinations.MovieDetailsDestination
import com.vlohachov.moviespot.ui.destinations.MoviesDestination
import com.vlohachov.moviespot.ui.destinations.SearchMoviesDestination
import com.vlohachov.moviespot.ui.destinations.SettingsDestination
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Destination
@RootNavGraph(start = true)
@Composable
fun Main(
    navigator: DestinationsNavigator,
    viewModel: MainViewModel = koinViewModel(),
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(connection = scrollBehavior.nestedScrollConnection),
        topBar = {
            MainAppBar(
                modifier = Modifier.fillMaxWidth(),
                scrollBehavior = scrollBehavior,
                navigator = navigator,
            )
        },
        floatingActionButton = {
            Discover(navigator = navigator)
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
            navigator = navigator
        )
    }
}

@Composable
private fun Content(
    modifier: Modifier,
    viewState: MainViewState,
    onError: @Composable (error: Throwable) -> Unit,
    navigator: DestinationsNavigator,
) {
    val onSeeDetails: (Movie) -> Unit = { movie ->
        navigator.navigate(MovieDetailsDestination(movieId = movie.id, movieTitle = movie.title))
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
        ) {
            items(MovieCategory.entries) { category ->
                MoviesSection(
                    modifier = Modifier.fillMaxWidth(),
                    title = stringResource(id = category.textResId()),
                    viewState = viewState.moviesState(category = category),
                    onMovieClick = onSeeDetails,
                    onMore = { navigator.navigate(MoviesDestination(category = category)) },
                )
            }
        }

        viewState.error?.run {
            onError(this)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MainAppBar(
    modifier: Modifier,
    scrollBehavior: TopAppBarScrollBehavior,
    navigator: DestinationsNavigator,
) {
    CenterAlignedTopAppBar(
        modifier = modifier,
        title = {
            Text(
                text = stringResource(id = R.string.app_name),
                fontFamily = FontFamily(Font(resId = R.font.chalkduster))
            )
        },
        navigationIcon = {
            IconButton(
                modifier = Modifier.semantics {
                    testTag = MainScreenDefaults.SearchButtonTestTag
                },
                onClick = { navigator.navigate(SearchMoviesDestination) },
            ) {
                Icon(
                    imageVector = Icons.Rounded.Search,
                    contentDescription = stringResource(id = R.string.search),
                )
            }
        },
        actions = {
            IconButton(
                modifier = Modifier.semantics {
                    testTag = MainScreenDefaults.SettingsButtonTestTag
                },
                onClick = { navigator.navigate(SettingsDestination) },
            ) {
                Icon(
                    imageVector = Icons.Rounded.Settings,
                    contentDescription = stringResource(id = R.string.settings),
                )
            }
        },
        scrollBehavior = scrollBehavior,
    )
}

@Composable
private fun Discover(navigator: DestinationsNavigator) {
    ExtendedFloatingActionButton(
        modifier = Modifier.semantics {
            testTag = MainScreenDefaults.DiscoverButtonTestTag
        },
        text = {
            Text(text = stringResource(id = R.string.discover))
        },
        icon = {
            Icon(
                painter = painterResource(id = R.drawable.ic_round_movie_filter_24),
                contentDescription = stringResource(id = R.string.discover),
            )
        },
        onClick = { navigator.navigate(DiscoverDestination) },
    )
}

private fun MovieCategory.textResId(): Int = when (this) {
    MovieCategory.UPCOMING -> R.string.upcoming
    MovieCategory.NOW_PLAYING -> R.string.now_playing
    MovieCategory.POPULAR -> R.string.popular
    MovieCategory.TOP_RATED -> R.string.top_rated
}

private fun MainViewState.moviesState(category: MovieCategory): ViewState<List<Movie>> =
    when (category) {
        MovieCategory.UPCOMING -> this.upcomingViewState
        MovieCategory.NOW_PLAYING -> this.nowPlayingViewState
        MovieCategory.POPULAR -> this.popularViewState
        MovieCategory.TOP_RATED -> this.topRatedViewState
    }

object MainScreenDefaults {

    const val SettingsButtonTestTag = "settings_button"
    const val SearchButtonTestTag = "search_button"
    const val DiscoverButtonTestTag = "discover_button"
    const val ErrorBarTestTag = "error_bar"
    const val SectionsTestTag = "movies_sections"
}
