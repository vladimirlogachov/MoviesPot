package com.vlohachov.moviespot.ui.search

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import androidx.paging.compose.collectAsLazyPagingItems
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.vlohachov.moviespot.R
import com.vlohachov.moviespot.ui.components.ErrorBar
import com.vlohachov.moviespot.ui.components.movie.MoviesPaginatedGrid
import com.vlohachov.moviespot.ui.destinations.MovieDetailsDestination
import kotlinx.coroutines.launch
import org.koin.androidx.compose.getViewModel

@OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalComposeUiApi::class,
)
@Destination
@Composable
fun SearchMovies(
    navigator: DestinationsNavigator,
    viewModel: SearchMoviesViewModel = getViewModel(),
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val gridState = rememberLazyGridState()
    val coroutineScope = rememberCoroutineScope()
    val showScrollToTop by remember { derivedStateOf { gridState.firstVisibleItemIndex > 3 } }
    val showTitle by remember { derivedStateOf { scrollBehavior.state.overlappedFraction > 0 } }

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
                    AnimatedVisibility(
                        visible = showTitle,
                        enter = fadeIn(),
                        exit = fadeOut(),
                    ) {
                        Text(text = stringResource(id = R.string.search))
                    }
                },
                navigationIcon = {
                    IconButton(
                        modifier = Modifier.semantics {
                            testTag = SearchMoviesDefaults.BackButtonTestTag
                        },
                        onClick = {
                            keyboardController?.hide()
                            navigator.navigateUp()
                        }
                    ) {
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
                    modifier = Modifier
                        .semantics {
                            testTag = SearchMoviesDefaults.ScrollToTopTestTag
                        }
                        .imePadding(),
                    onClick = {
                        coroutineScope.launch {
                            gridState.scrollToItem(index = 0)
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
                        testTag = SearchMoviesDefaults.ContentErrorTestTag
                    }
                    .imePadding()
                    .navigationBarsPadding(),
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
                state = gridState,
                columns = GridCells.Fixed(count = 3),
                movies = viewModel.movies.collectAsLazyPagingItems(),
                contentPadding = PaddingValues(
                    top = 80.dp,
                    start = 16.dp,
                    end = 16.dp,
                    bottom = 16.dp,
                ),
                onClick = { movie ->
                    keyboardController?.hide()
                    navigator.navigate(
                        MovieDetailsDestination(
                            movieId = movie.id,
                            movieTitle = movie.title,
                        )
                    )
                },
                onError = viewModel::onError,
                progress = {
                    item(span = { GridItemSpan(currentLineSpan = 3) }) {
                        Box(
                            modifier = Modifier
                                .semantics {
                                    testTag = SearchMoviesDefaults.ContentLoadingTestTag
                                }
                                .fillMaxWidth(),
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
                value = viewModel.search.collectAsState(initial = "").value,
                onClear = viewModel::onClear,
                onValueChange = viewModel::onSearch,
            )
        }
    }
}

@Composable
private fun SearchField(
    value: String,
    onValueChange: (value: String) -> Unit,
    onClear: () -> Unit,
    modifier: Modifier = Modifier,
    keyboardOptions: KeyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences),
) {
    TextField(
        modifier = modifier.semantics {
            testTag = SearchMoviesDefaults.SearchFieldTestTag
        },
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
        trailingIcon = {
            AnimatedVisibility(
                visible = value.isNotBlank(),
                enter = fadeIn() + scaleIn(),
                exit = fadeOut() + scaleOut(),
            ) {
                IconButton(
                    modifier = Modifier.semantics {
                        testTag = SearchMoviesDefaults.ClearSearchFieldTestTag
                    },
                    onClick = onClear,
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Clear,
                        contentDescription = stringResource(id = R.string.clear),
                    )
                }
            }
        },
        shape = ButtonDefaults.shape,
        colors = TextFieldDefaults.colors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
            errorIndicatorColor = Color.Transparent,
        ),
        keyboardOptions = keyboardOptions,
    )
}

object SearchMoviesDefaults {

    const val BackButtonTestTag = "back_button"
    const val SearchFieldTestTag = "search_field"
    const val ClearSearchFieldTestTag = "clear_search_field"
    const val ContentLoadingTestTag = "content_loading"
    const val ContentErrorTestTag = "content_error"
    const val ScrollToTopTestTag = "scroll_to_top"
}
