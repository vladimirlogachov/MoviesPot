package com.vlohachov.moviespot.ui.main

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.vlohachov.domain.model.Movie
import com.vlohachov.moviespot.R
import com.vlohachov.moviespot.core.ViewState
import com.vlohachov.moviespot.ui.movies.MoviesSection
import com.vlohachov.moviespot.ui.movies.components.Movies
import com.vlohachov.moviespot.ui.movies.components.Section
import com.vlohachov.moviespot.ui.movies.components.SectionTitle
import org.koin.androidx.compose.getViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    modifier: Modifier,
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
    viewModel: MainViewModel = getViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()

    val unknownErrorText = stringResource(id = R.string.uknown_error)

    uiState.error?.run {
        LaunchedEffect(snackbarHostState) {
            snackbarHostState.showSnackbar(message = localizedMessage ?: unknownErrorText)
            viewModel.onErrorConsumed()
        }
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            CenterAlignedTopAppBar(
                modifier = Modifier.fillMaxWidth(),
                title = {
                    Text(text = stringResource(id = R.string.app_name))
                },
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
                onSeeAll = {},
            )
        }
    }
}

@Composable
private fun Content(
    modifier: Modifier,
    moviesViewStates: Map<MoviesSection, ViewState<List<Movie>>>,
    onSeeAll: (section: MoviesSection) -> Unit,
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(space = 16.dp),
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
                        trailing = {
                            TextButton(onClick = { onSeeAll(section) }) {
                                Text(
                                    text = stringResource(id = R.string.see_all).uppercase(),
                                    textDecoration = TextDecoration.Underline
                                )
                            }
                        }
                    )
                },
                content = {
                    Movies(
                        modifier = Modifier.fillMaxWidth(),
                        viewState = moviesViewStates[section] ?: ViewState.Loading,
                    )
                },
            )
        }
    }
}
