package com.vlohachov.moviespot.ui.search

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.paging.compose.collectAsLazyPagingItems
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.vlohachov.moviespot.R
import com.vlohachov.moviespot.ui.components.movie.MoviesPaginatedGrid
import org.koin.androidx.compose.getViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Destination
@Composable
fun Search(
    navigator: DestinationsNavigator,
    viewModel: SearchViewModel = getViewModel(),
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val unknownErrorText = stringResource(id = R.string.uknown_error)
    val showTitle by remember { derivedStateOf { scrollBehavior.state.overlappedFraction > 0 } }
    val uiState by viewModel.uiState.collectAsState()
    val movies = uiState.results.collectAsLazyPagingItems()

    uiState.error?.run {
        LaunchedEffect(snackbarHostState) {
            snackbarHostState.showSnackbar(message = localizedMessage ?: unknownErrorText)
            viewModel.onErrorConsumed()
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
                    AnimatedVisibility(
                        visible = showTitle,
                        enter = fadeIn(),
                        exit = fadeOut(),
                    ) {
                        Text(text = stringResource(id = R.string.search))
                    }
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
        snackbarHost = {
            SnackbarHost(
                modifier = Modifier.navigationBarsPadding(),
                hostState = snackbarHostState,
            )
        },
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues = paddingValues),
        ) {
            MoviesPaginatedGrid(
                modifier = Modifier.fillMaxSize(),
                columns = GridCells.Fixed(count = 3),
                movies = movies,
                contentPadding = PaddingValues(
                    top = 80.dp,
                    start = 16.dp,
                    end = 16.dp,
                    bottom = 16.dp,
                ),
                onError = viewModel::onError,
                progress = {
                    item(span = { GridItemSpan(currentLineSpan = 3) }) {
                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.Center,
                        ) {
                            CircularProgressIndicator()
                        }
                    }
                }
            )
            SearchField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp, horizontal = 16.dp),
                value = viewModel.search.collectAsState().value,
                onValueChange = viewModel::onSearch,
            )
        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SearchField(
    value: String,
    onValueChange: (value: String) -> Unit,
    modifier: Modifier = Modifier,
) {
    TextField(
        modifier = modifier,
        value = value,
        onValueChange = onValueChange,
        placeholder = {
            Text(text = stringResource(id = R.string.search))
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Rounded.Search,
                contentDescription = stringResource(id = R.string.search),
            )
        },
        shape = ButtonDefaults.shape,
        colors = TextFieldDefaults.textFieldColors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
            errorIndicatorColor = Color.Transparent,
        ),
    )
}