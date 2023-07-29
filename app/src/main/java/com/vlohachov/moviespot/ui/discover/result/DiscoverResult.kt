package com.vlohachov.moviespot.ui.discover.result

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
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
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.vlohachov.moviespot.R
import com.vlohachov.moviespot.ui.components.ErrorBar
import com.vlohachov.moviespot.ui.components.movie.MoviesPaginatedGrid
import com.vlohachov.moviespot.ui.destinations.MovieDetailsDestination
import kotlinx.coroutines.launch
import org.koin.androidx.compose.getViewModel
import org.koin.core.parameter.parametersOf

@OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalMaterialApi::class,
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
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val gridState = rememberLazyGridState()
    val coroutineScope = rememberCoroutineScope()
    val showScrollToTop by remember { derivedStateOf { gridState.firstVisibleItemIndex > 3 } }

    viewModel.error?.run {
        ErrorBar(
            error = this,
            snackbarHostState = snackbarHostState,
            onDismissed = viewModel::onErrorConsumed,
        )
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
                    IconButton(onClick = navigator::navigateUp) {
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
        val isRefreshing = movies.loadState.refresh is LoadState.Loading
        val refreshState =
            rememberPullRefreshState(refreshing = isRefreshing, onRefresh = movies::refresh)
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues = paddingValues)
                .pullRefresh(state = refreshState),
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

            PullRefreshIndicator(
                modifier = Modifier
                    .align(alignment = Alignment.TopCenter)
                    .semantics {
                        testTag = DiscoverResultDefaults.ContentLoadingTestTag
                        contentDescription = isRefreshing.toString()
                    },
                refreshing = isRefreshing,
                state = refreshState,
            )
        }
    }
}

object DiscoverResultDefaults {

    const val ContentLoadingTestTag = "content_loading"
    const val ContentErrorTestTag = "content_error"
    const val ScrollToTopTestTag = "scroll_to_top"
}
