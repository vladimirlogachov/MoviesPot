package com.vlohachov.moviespot.ui.discover

import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.vlohachov.domain.model.genre.Genre
import com.vlohachov.moviespot.R
import com.vlohachov.moviespot.core.DummyGenres
import com.vlohachov.moviespot.core.ViewState
import com.vlohachov.moviespot.ui.components.bar.AppBar
import com.vlohachov.moviespot.ui.components.bar.ErrorBar
import com.vlohachov.moviespot.ui.destinations.DiscoverResultDestination
import com.vlohachov.moviespot.ui.theme.MoviesPotTheme
import org.koin.androidx.compose.getViewModel

@OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalComposeUiApi::class,
)
@Destination
@Composable
fun Discover(
    navigator: DestinationsNavigator,
    viewModel: DiscoverViewModel = getViewModel(),
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val keyboardController = LocalSoftwareKeyboardController.current
    val uiState by viewModel.uiState.collectAsState()

    uiState.error?.run {
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
                title = stringResource(id = R.string.discover),
                scrollBehavior = scrollBehavior,
                onBackClick = {
                    keyboardController?.hide()
                    navigator.navigateUp()
                }
            )
        },
        snackbarHost = {
            SnackbarHost(
                modifier = Modifier
                    .semantics {
                        testTag = DiscoverDefaults.GenresErrorTestTag
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
            onYear = viewModel::onYear,
            onSelect = viewModel::onSelect,
            onClearSelection = viewModel::onClearSelection,
            onError = viewModel::onError,
            onDiscover = {
                navigator.navigate(
                    DiscoverResultDestination(
                        year = uiState.year.toIntOrNull(),
                        selectedGenres = uiState.selectedGenres.map { genre ->
                            genre.id
                        }.toIntArray(),
                    )
                )
            }
        )
    }
}

@Composable
private fun Content(
    modifier: Modifier,
    viewState: DiscoverViewState,
    onYear: (year: String) -> Unit,
    onSelect: (genre: Genre) -> Unit,
    onClearSelection: (genre: Genre) -> Unit,
    onError: (error: Throwable) -> Unit,
    onDiscover: () -> Unit,
) {
    Column(
        modifier = modifier
            .semantics {
                testTag = DiscoverDefaults.ContentTestTag
            },
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Genres(
            modifier = Modifier
                .semantics {
                    testTag = DiscoverDefaults.GenresTestTag
                }
                .fillMaxWidth(),
            viewState = viewState.genresViewState,
            selectedGenres = viewState.selectedGenres,
            onSelect = onSelect,
            onClearSelection = onClearSelection,
            onError = onError,
        )
        Input(
            modifier = Modifier
                .semantics {
                    testTag = DiscoverDefaults.YearTestTag
                }
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            value = viewState.year,
            onValueChange = onYear,
        )
        Button(
            modifier = Modifier
                .semantics {
                    testTag = DiscoverDefaults.DiscoverButtonTestTag
                }
                .padding(all = 16.dp),
            onClick = onDiscover,
            enabled = viewState.discoverEnabled,
        ) {
            Text(text = stringResource(id = R.string.discover_movies))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun Genres(
    modifier: Modifier,
    viewState: ViewState<List<Genre>>,
    selectedGenres: List<Genre>,
    onSelect: (genre: Genre) -> Unit,
    onClearSelection: (genre: Genre) -> Unit,
    onError: (error: Throwable) -> Unit,
) {
    when (viewState) {
        ViewState.Loading ->
            CircularProgressIndicator(
                modifier = Modifier.semantics {
                    testTag = DiscoverDefaults.GenresLoadingTestTag
                }
            )

        is ViewState.Error ->
            viewState.error?.run(onError)

        is ViewState.Success ->
            LazyRow(
                modifier = modifier,
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(space = 8.dp),
            ) {
                items(items = viewState.data) { genre ->
                    val selected = selectedGenres.contains(element = genre)
                    FilterChip(
                        selected = selected,
                        onClick = { if (!selected) onSelect(genre) else onClearSelection(genre) },
                        leadingIcon = {
                            if (selected) {
                                Icon(
                                    imageVector = Icons.Rounded.Check,
                                    contentDescription = null,
                                )
                            }
                        },
                        label = { Text(text = genre.name) },
                    )
                }
            }
    }
}

@Composable
private fun Input(
    modifier: Modifier,
    value: String,
    onValueChange: (text: String) -> Unit,
) {
    OutlinedTextField(
        modifier = modifier,
        value = value,
        onValueChange = onValueChange,
        label = { Text(text = stringResource(id = R.string.year)) },
        trailingIcon = {
            AnimatedVisibility(
                visible = value.isNotBlank(),
                enter = fadeIn() + scaleIn(),
                exit = fadeOut() + scaleOut(),
            ) {
                IconButton(
                    modifier = Modifier.semantics {
                        testTag = DiscoverDefaults.YearClearTestTag
                    },
                    onClick = { onValueChange("") }
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Clear,
                        contentDescription = stringResource(id = R.string.clear),
                    )
                }
            }
        },
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
    )
}

@Preview(showBackground = true)
@Composable
fun DiscoverContentPreview() {
    MoviesPotTheme {
        Content(
            modifier = Modifier.padding(all = 16.dp),
            viewState = DiscoverViewState(
                year = "2022",
                genresViewState = ViewState.Success(data = DummyGenres),
                discoverEnabled = true,
            ),
            onYear = {},
            onSelect = {},
            onClearSelection = {},
            onError = {},
            onDiscover = {},
        )
    }
}

object DiscoverDefaults {

    const val ContentTestTag = "content"
    const val GenresTestTag = "content_genres"
    const val GenresLoadingTestTag = "content_genres_loading"
    const val GenresErrorTestTag = "content_genres_error"
    const val YearTestTag = "content_year"
    const val YearClearTestTag = "content_year_clear"
    const val DiscoverButtonTestTag = "content_discover_button"
    const val BackButtonTestTag = "back_button"
}
