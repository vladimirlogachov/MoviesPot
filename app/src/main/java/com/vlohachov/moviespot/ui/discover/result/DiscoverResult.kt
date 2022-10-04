package com.vlohachov.moviespot.ui.discover.result

import androidx.compose.animation.*
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import androidx.compose.ui.text.style.TextOverflow
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.SwipeRefreshIndicator
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.vlohachov.moviespot.R
import com.vlohachov.moviespot.ui.components.movie.MoviesPaginatedGrid
import com.vlohachov.moviespot.ui.destinations.MovieDetailsDestination
import kotlinx.coroutines.launch
import org.koin.androidx.compose.getViewModel
import org.koin.core.parameter.parametersOf

@OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalAnimationApi::class,
)
@Destination
@Composable
fun DiscoverResult(
    navigator: DestinationsNavigator,
    year: Int?,
    selectedGenres: IntArray?,
    viewModel: DiscoverResultViewModel = getViewModel { parametersOf(year, selectedGenres) },
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
) {
    val unknownErrorText = stringResource(id = R.string.unknown_error_remote)
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val gridState = rememberLazyGridState()
    val coroutineScope = rememberCoroutineScope()
    val showScrollToTop by remember { derivedStateOf { gridState.firstVisibleItemIndex > 3 } }

    viewModel.error?.run {
        LaunchedEffect(snackbarHostState) {
            viewModel.onErrorConsumed()
            snackbarHostState.showSnackbar(message = localizedMessage ?: unknownErrorText)
        }
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(connection = scrollBehavior.nestedScrollConnection),
        topBar = {
            CenterAlignedTopAppBar(
                modifier = Modifier.fillMaxWidth(),
                title = {
                    Text(
                        text = stringResource(id = R.string.discover_results),
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navigator.navigateUp() }) {
                        Icon(
                            imageVector = Icons.Rounded.ArrowBack,
                            contentDescription = null,
                        )
                    }
                },
                scrollBehavior = scrollBehavior,
            )
        },
        floatingActionButton = {
            AnimatedVisibility(
                visible = showScrollToTop,
                enter = fadeIn() + scaleIn(),
                exit = fadeOut() + scaleOut(),
            ) {
                FloatingActionButton(
                    modifier = Modifier.semantics {
                        testTag = DiscoverResultDefaults.ScrollToTopTestTag
                    },
                    onClick = {
                        coroutineScope.launch {
                            gridState.scrollToItem(index = 0)
                            // TODO: Find way to animate LargeTopAppBar on scrolled to top
                        }
                    }
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_round_arrow_upward_24),
                        contentDescription = stringResource(id = R.string.scroll_to_top),
                    )
                }
            }
        },
        snackbarHost = {
            SnackbarHost(
                modifier = Modifier
                    .semantics {
                        testTag = DiscoverResultDefaults.ContentErrorTestTag
                    }
                    .navigationBarsPadding(),
                hostState = snackbarHostState,
            )
        },
    ) { paddingValues ->
        val movies = viewModel.movies.collectAsLazyPagingItems()

        SwipeRefresh(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues = paddingValues),
            state = rememberSwipeRefreshState(isRefreshing = movies.loadState.refresh is LoadState.Loading),
            indicator = { state, refreshTrigger ->
                SwipeRefreshIndicator(
                    modifier = Modifier.semantics {
                        testTag = DiscoverResultDefaults.ContentLoadingTestTag
                        contentDescription = state.isRefreshing.toString()
                    },
                    state = state,
                    refreshTriggerDistance = refreshTrigger,
                )
            },
            onRefresh = movies::refresh,
        ) {
            MoviesPaginatedGrid(
                modifier = Modifier.fillMaxSize(),
                state = gridState,
                columns = GridCells.Fixed(count = 3),
                movies = movies,
                onClick = { movie ->
                    navigator.navigate(
                        MovieDetailsDestination(
                            movieId = movie.id,
                            movieTitle = movie.title,
                        )
                    )
                },
                onError = viewModel::onError,
            )
        }
    }
}

object DiscoverResultDefaults {

    const val ContentLoadingTestTag = "content_loading"
    const val ContentErrorTestTag = "content_error"
    const val ScrollToTopTestTag = "scroll_to_top"
}
