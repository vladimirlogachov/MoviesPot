package com.vlohachov.shared.ui.screen.discover.result

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.testTag
import androidx.paging.compose.collectAsLazyPagingItems
import com.vlohachov.shared.domain.model.movie.Movie
import com.vlohachov.shared.ui.component.bar.AppBar
import com.vlohachov.shared.ui.component.bar.ErrorBar
import com.vlohachov.shared.ui.component.button.ScrollToTop
import com.vlohachov.shared.ui.component.movie.MoviesPaginatedGrid
import moviespot.shared_ui.generated.resources.Res
import moviespot.shared_ui.generated.resources.discover_results
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject
import org.koin.core.module.Module
import org.koin.core.parameter.parametersOf
import org.koin.dsl.module

private const val VISIBLE_ITEMS_THRESHOLD = 3

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal actual fun DiscoverResult(
    year: Int?,
    genres: IntArray?,
    onBack: () -> Unit,
    onMovieDetails: (movie: Movie) -> Unit,
    snackbarHostState: SnackbarHostState,
) {
    val viewModel = koinInject<DiscoverResultViewModel> { parametersOf(year, genres) }
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val gridState = rememberLazyGridState()
    val showScrollToTop by remember { derivedStateOf { gridState.firstVisibleItemIndex > VISIBLE_ITEMS_THRESHOLD } }

    ErrorBar(
        error = viewModel.error,
        snackbarHostState = snackbarHostState,
        onDismissed = viewModel::onErrorConsumed,
    )

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(connection = scrollBehavior.nestedScrollConnection),
        topBar = {
            AppBar(
                modifier = Modifier.fillMaxWidth(),
                title = stringResource(resource = Res.string.discover_results),
                scrollBehavior = scrollBehavior,
                onBackClick = onBack,
            )
        },
        floatingActionButton = {
            ScrollToTop(
                modifier = Modifier.navigationBarsPadding(),
                visible = showScrollToTop,
                gridState = gridState,
            )
        },
        snackbarHost = {
            SnackbarHost(
                modifier = Modifier
                    .testTag(tag = DiscoverResultDefaults.ContentErrorTestTag)
                    .navigationBarsPadding(),
                hostState = snackbarHostState,
            )
        },
        contentWindowInsets = WindowInsets.ime
    ) { paddingValues ->
        MoviesPaginatedGrid(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues = paddingValues),
            state = gridState,
            columns = GridCells.Fixed(count = 3),
            movies = viewModel.movies.collectAsLazyPagingItems(),
            onClick = onMovieDetails,
            onError = viewModel::onError,
        )
    }
}

internal actual val discoverResultModule: Module = module {
    factory { params ->
        DiscoverResultViewModel(
            pager = DiscoverResultPager(
                year = params.getOrNull(),
                selectedGenres = params.get(),
                useCase = get(),
            )
        )
    }
}
