package com.vlohachov.moviespot.ui.main

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.vlohachov.domain.model.movie.Movie
import com.vlohachov.moviespot.R
import com.vlohachov.moviespot.core.ViewState
import com.vlohachov.moviespot.ui.components.Movies
import com.vlohachov.moviespot.ui.components.Section
import com.vlohachov.moviespot.ui.components.SectionTitle
import com.vlohachov.moviespot.ui.components.SetSystemBarsColor
import com.vlohachov.moviespot.ui.destinations.NowPlayingMoviesDestination
import com.vlohachov.moviespot.ui.destinations.PopularMoviesDestination
import com.vlohachov.moviespot.ui.destinations.TopRatedMoviesDestination
import com.vlohachov.moviespot.ui.destinations.UpcomingMoviesDestination
import com.vlohachov.moviespot.ui.movies.MoviesSection
import org.koin.androidx.compose.getViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Destination(start = true)
@Composable
fun MainScreen(
    navigator: DestinationsNavigator,
    viewModel: MainViewModel = getViewModel(),
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
    topAppBarState: TopAppBarState = rememberTopAppBarState(),
    scrollBehavior: TopAppBarScrollBehavior = remember {
        TopAppBarDefaults.pinnedScrollBehavior(
            topAppBarState
        )
    },
    topAppBarColors: TopAppBarColors = TopAppBarDefaults.centerAlignedTopAppBarColors(),
) {
    val colorTransitionFraction = scrollBehavior.state.overlappedFraction
    val appBarContainerColor by topAppBarColors.containerColor(colorTransitionFraction)
    val uiState by viewModel.uiState.collectAsState()

    val unknownErrorText = stringResource(id = R.string.uknown_error)

    uiState.error?.run {
        LaunchedEffect(snackbarHostState) {
            snackbarHostState.showSnackbar(message = localizedMessage ?: unknownErrorText)
            viewModel.onErrorConsumed()
        }
    }

    SetSystemBarsColor(color = appBarContainerColor)

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(connection = scrollBehavior.nestedScrollConnection),
        topBar = {
            CenterAlignedTopAppBar(
                modifier = Modifier.fillMaxWidth(),
                title = {
                    Text(text = stringResource(id = R.string.app_name))
                },
                scrollBehavior = scrollBehavior,
                colors = topAppBarColors,
            )
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
    ) { paddingValues ->
        SwipeRefresh(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues = paddingValues),
            state = rememberSwipeRefreshState(isRefreshing = uiState.isLoading),
            onRefresh = viewModel::onRefresh,
        ) {
            Content(
                modifier = Modifier.fillMaxSize(),
                moviesViewStates = uiState.moviesViewStates,
                onSeeMore = { section ->
                    when (section) {
                        MoviesSection.Upcoming -> navigator.navigate(UpcomingMoviesDestination)
                        MoviesSection.NowPlaying -> navigator.navigate(NowPlayingMoviesDestination)
                        MoviesSection.Popular -> navigator.navigate(PopularMoviesDestination)
                        MoviesSection.TopRated -> navigator.navigate(TopRatedMoviesDestination)
                    }
                },
            )
        }
    }
}

@Composable
private fun Content(
    modifier: Modifier,
    moviesViewStates: Map<MoviesSection, ViewState<List<Movie>>>,
    onSeeMore: (section: MoviesSection) -> Unit,
) {
    LazyColumn(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        items(items = moviesViewStates.keys.toTypedArray()) { section ->
            Section(
                modifier = Modifier.fillMaxWidth(),
                title = {
                    SectionTitle(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        text = stringResource(id = section.textRes),
                    )
                },
                content = {
                    Movies(
                        modifier = Modifier.fillMaxWidth(),
                        viewState = moviesViewStates[section] ?: ViewState.Loading,
                        onSeeMore = { onSeeMore(section) },
                    )
                },
            )
        }
    }
}
