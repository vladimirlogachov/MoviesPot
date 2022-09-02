package com.vlohachov.moviespot.ui.main

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.vlohachov.domain.model.movie.Movie
import com.vlohachov.moviespot.R
import com.vlohachov.moviespot.ui.components.movie.MoviesSection
import com.vlohachov.moviespot.ui.destinations.*
import org.koin.androidx.compose.getViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Destination(start = true)
@Composable
fun MainScreen(
    navigator: DestinationsNavigator,
    viewModel: MainViewModel = getViewModel(),
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val unknownErrorText = stringResource(id = R.string.uknown_error)
    val uiState by viewModel.uiState.collectAsState()

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
            )
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
    ) { paddingValues ->
        Content(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues = paddingValues),
            viewState = uiState,
            onError = { error ->
                LaunchedEffect(snackbarHostState) {
                    snackbarHostState.showSnackbar(
                        message = error.localizedMessage ?: unknownErrorText
                    )
                    viewModel.onErrorConsumed()
                }
            },
            onSeeDetails = { movie ->
                navigator.navigate(
                    MovieDetailsDestination(
                        movieId = movie.id,
                        movieTitle = movie.title,
                    )
                )
            },
            onMoreUpcoming = { navigator.navigate(UpcomingMoviesDestination) },
            onMoreNowPlaying = { navigator.navigate(NowPlayingMoviesDestination) },
            onMorePopular = { navigator.navigate(PopularMoviesDestination) },
            onMoreTopRated = { navigator.navigate(TopRatedMoviesDestination) },
        )
    }
}

@Composable
private fun Content(
    modifier: Modifier,
    viewState: MainViewState,
    onError: @Composable (error: Throwable) -> Unit,
    onSeeDetails: (movie: Movie) -> Unit,
    onMoreUpcoming: () -> Unit,
    onMoreNowPlaying: () -> Unit,
    onMorePopular: () -> Unit,
    onMoreTopRated: () -> Unit,
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.TopCenter,
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            item {
                MoviesSection(
                    modifier = Modifier.fillMaxWidth(),
                    title = stringResource(id = R.string.upcoming),
                    viewState = viewState.upcomingViewState,
                    onMovieClick = onSeeDetails,
                    onMore = onMoreUpcoming,
                )
            }
            item {
                MoviesSection(
                    modifier = Modifier.fillMaxWidth(),
                    title = stringResource(id = R.string.now_playing),
                    viewState = viewState.nowPlayingViewState,
                    onMovieClick = onSeeDetails,
                    onMore = onMoreNowPlaying,
                )
            }
            item {
                MoviesSection(
                    modifier = Modifier.fillMaxWidth(),
                    title = stringResource(id = R.string.popular),
                    viewState = viewState.popularViewState,
                    onMovieClick = onSeeDetails,
                    onMore = onMorePopular,
                )
            }
            item {
                MoviesSection(
                    modifier = Modifier.fillMaxWidth(),
                    title = stringResource(id = R.string.top_rated),
                    viewState = viewState.topRatedViewState,
                    onMovieClick = onSeeDetails,
                    onMore = onMoreTopRated,
                )
            }
        }

        viewState.error?.run {
            onError(error = this)
        }
    }
}