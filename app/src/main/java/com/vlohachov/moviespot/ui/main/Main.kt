package com.vlohachov.moviespot.ui.main

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.vlohachov.domain.model.movie.Movie
import com.vlohachov.moviespot.R
import com.vlohachov.moviespot.ui.components.movie.MoviesSection
import com.vlohachov.moviespot.ui.destinations.*
import org.koin.androidx.compose.getViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Destination(start = true)
@Composable
fun Main(
    navigator: DestinationsNavigator,
    viewModel: MainViewModel = getViewModel(),
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
) {
    val unknownErrorText = stringResource(id = R.string.uknown_error)
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(connection = scrollBehavior.nestedScrollConnection),
        topBar = {
            CenterAlignedTopAppBar(
                modifier = Modifier.fillMaxWidth(),
                title = {
                    Text(
                        text = stringResource(id = R.string.app_name),
                        fontFamily = FontFamily(Font(resId = R.font.chalkduster))
                    )
                },
                actions = {
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
                scrollBehavior = scrollBehavior,
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                modifier = Modifier
                    .semantics {
                        testTag = MainScreenDefaults.DiscoverButtonTestTag
                    }
                    .navigationBarsPadding(),
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
                LaunchedEffect(snackbarHostState) {
                    viewModel.onErrorConsumed()
                    snackbarHostState.showSnackbar(
                        message = error.localizedMessage ?: unknownErrorText
                    )
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
            modifier = Modifier
                .semantics {
                    testTag = MainScreenDefaults.SectionsTestTag
                }
                .fillMaxSize(),
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

object MainScreenDefaults {

    const val SearchButtonTestTag = "search_button"
    const val DiscoverButtonTestTag = "discover_button"
    const val ErrorBarTestTag = "error_bar"
    const val SectionsTestTag = "movies_sections"
}