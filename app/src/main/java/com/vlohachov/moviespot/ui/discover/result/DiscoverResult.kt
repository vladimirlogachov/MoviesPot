package com.vlohachov.moviespot.ui.discover.result

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.vlohachov.domain.model.movie.Movie
import com.vlohachov.moviespot.R
import com.vlohachov.moviespot.ui.components.bar.AppBar
import com.vlohachov.moviespot.ui.components.bar.ErrorBar
import com.vlohachov.moviespot.ui.components.button.ScrollToTop
import com.vlohachov.moviespot.ui.components.movie.MoviesPaginatedGrid
import com.vlohachov.moviespot.ui.destinations.MovieDetailsDestination
import org.koin.androidx.compose.getViewModel
import org.koin.core.parameter.parametersOf

private const val VISIBLE_ITEMS_THRESHOLD = 3

@OptIn(
    ExperimentalMaterial3Api::class,
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
    val showScrollToTop by remember { derivedStateOf { gridState.firstVisibleItemIndex > VISIBLE_ITEMS_THRESHOLD } }

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
            AppBar(
                modifier = Modifier.fillMaxWidth(),
                title = stringResource(id = R.string.discover_results),
                scrollBehavior = scrollBehavior,
                onBackClick = navigator::navigateUp,
            )
        },
        floatingActionButton = {
            ScrollToTop(visible = showScrollToTop, gridState = gridState)
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
        Content(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues = paddingValues),
            movies = viewModel.movies.collectAsLazyPagingItems(),
            gridState = gridState,
            onItemClick = { movie ->
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

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun Content(
    modifier: Modifier,
    movies: LazyPagingItems<Movie>,
    gridState: LazyGridState,
    onItemClick: (Movie) -> Unit,
    onError: (Throwable) -> Unit,
) {
    val isRefreshing = movies.loadState.refresh is LoadState.Loading
    val refreshState =
        rememberPullRefreshState(refreshing = isRefreshing, onRefresh = movies::refresh)
    Box(
        modifier = modifier
            .pullRefresh(state = refreshState),
    ) {
        MoviesPaginatedGrid(
            modifier = Modifier.fillMaxSize(),
            state = gridState,
            columns = GridCells.Fixed(count = 3),
            movies = movies,
            onClick = onItemClick,
            onError = onError,
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

object DiscoverResultDefaults {

    const val ContentLoadingTestTag = "content_loading"
    const val ContentErrorTestTag = "content_error"
    const val ScrollToTopTestTag = "scroll_to_top"
}
