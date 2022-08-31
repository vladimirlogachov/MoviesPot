package com.vlohachov.moviespot.ui.main

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.vlohachov.domain.model.movie.Movie
import com.vlohachov.moviespot.R
import com.vlohachov.moviespot.core.DummyMovies
import com.vlohachov.moviespot.core.ViewState
import com.vlohachov.moviespot.ui.components.MoreButton
import com.vlohachov.moviespot.ui.components.SetSystemBarsColor
import com.vlohachov.moviespot.ui.components.movie.MoviesLazyRow
import com.vlohachov.moviespot.ui.components.section.Section
import com.vlohachov.moviespot.ui.components.section.SectionTitle
import com.vlohachov.moviespot.ui.destinations.*
import com.vlohachov.moviespot.ui.theme.MoviesPotTheme
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
        TopAppBarDefaults.pinnedScrollBehavior(topAppBarState)
    },
    topAppBarColors: TopAppBarColors = TopAppBarDefaults.centerAlignedTopAppBarColors(),
) {
    val unknownErrorText = stringResource(id = R.string.uknown_error)
    val colorTransitionFraction = scrollBehavior.state.overlappedFraction
    val appBarContainerColor by topAppBarColors.containerColor(colorTransitionFraction)
    val uiState by viewModel.uiState.collectAsState()

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
                navigator.navigate(MovieDetailsDestination(movieId = movie.id))
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
                    onSeeMore = onMoreUpcoming,
                )
            }
            item {
                MoviesSection(
                    modifier = Modifier.fillMaxWidth(),
                    title = stringResource(id = R.string.now_playing),
                    viewState = viewState.nowPlayingViewState,
                    onMovieClick = onSeeDetails,
                    onSeeMore = onMoreNowPlaying,
                )
            }
            item {
                MoviesSection(
                    modifier = Modifier.fillMaxWidth(),
                    title = stringResource(id = R.string.popular),
                    viewState = viewState.popularViewState,
                    onMovieClick = onSeeDetails,
                    onSeeMore = onMorePopular,
                )
            }
            item {
                MoviesSection(
                    modifier = Modifier.fillMaxWidth(),
                    title = stringResource(id = R.string.top_rated),
                    viewState = viewState.topRatedViewState,
                    onMovieClick = onSeeDetails,
                    onSeeMore = onMoreTopRated,
                )
            }
        }

        viewState.error?.run {
            onError(error = this)
        }
    }
}

@Composable
private fun MoviesSection(
    title: String,
    viewState: ViewState<List<Movie>>,
    modifier: Modifier = Modifier,
    onMovieClick: ((movie: Movie) -> Unit)? = null,
    onSeeMore: (() -> Unit)? = null,
) {
    val moreButton: @Composable (() -> Unit)? =
        if (viewState is ViewState.Success && onSeeMore != null) {
            @Composable { MoreButton(onClick = onSeeMore) }
        } else {
            null
        }
    Section(
        modifier = modifier,
        title = {
            SectionTitle(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp),
                text = title,
                trailing = moreButton,
                horizontalArrangement = Arrangement.SpaceBetween,
            )
        },
    ) {
        Movies(
            modifier = Modifier.fillMaxWidth(),
            viewState = viewState,
            onMovieClick = onMovieClick,
        )
    }
}

@Composable
private fun Movies(
    viewState: ViewState<List<Movie>>,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(all = 16.dp),
    onMovieClick: ((movie: Movie) -> Unit)? = null,
) {
    Box(modifier = modifier) {
        when (viewState) {
            ViewState.Loading ->
                CircularProgressIndicator(
                    modifier = Modifier
                        .padding(paddingValues = contentPadding)
                        .align(alignment = Alignment.Center)
                )
            is ViewState.Error ->
                viewState.error?.message?.run {
                    Text(
                        modifier = Modifier.padding(paddingValues = contentPadding),
                        text = this,
                    )
                }
            is ViewState.Success ->
                MoviesLazyRow(
                    modifier = Modifier
                        .height(height = 160.dp)
                        .fillMaxWidth(),
                    movies = viewState.data,
                    contentPadding = contentPadding,
                    onClick = onMovieClick,
                )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MoviesSectionPreview() {
    MoviesPotTheme {
        Column {
            MoviesSection(title = "Title", viewState = ViewState.Loading)
            MoviesSection(
                title = "Title",
                viewState = ViewState.Error(error = Throwable("Error text")),
            )
            MoviesSection(title = "Title", viewState = ViewState.Success(data = DummyMovies))
            MoviesSection(
                title = "Title",
                viewState = ViewState.Success(data = DummyMovies),
                onSeeMore = {},
            )
        }
    }
}