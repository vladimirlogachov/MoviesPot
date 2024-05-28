package com.vlohachov.shared.ui.screen.discover

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.vlohachov.shared.core.ViewState
import com.vlohachov.shared.domain.model.genre.Genre
import com.vlohachov.shared.ui.component.bar.AppBar
import com.vlohachov.shared.ui.component.bar.ErrorBar
import com.vlohachov.shared.ui.component.bar.ErrorBarDefaults
import com.vlohachov.shared.ui.screen.Screen
import com.vlohachov.shared.ui.screen.discover.result.DiscoverResultScreen
import com.vlohachov.shared.ui.theme.MoviesPotTheme
import com.vlohachov.shared.utils.DummyGenres
import moviespot.shared_ui.generated.resources.Res
import moviespot.shared_ui.generated.resources.clear
import moviespot.shared_ui.generated.resources.discover
import moviespot.shared_ui.generated.resources.discover_movies
import moviespot.shared_ui.generated.resources.year
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.koinInject

internal data object DiscoverScreen : Screen<Unit>() {

    override val path: String = "discover"
    override val arguments: List<NamedNavArgument> = emptyList()

    override fun route(params: Unit): String = path

    override fun NavGraphBuilder.composable(navController: NavController) {
        composable(route = path, arguments = arguments) {
            Discover(
                onBack = navController::navigateUp,
                onDiscover = { year, genres ->
                    DiscoverResultScreen.Params(year = year, genres = genres)
                        .run(DiscoverResultScreen::route)
                        .run(navController::navigate)
                },
            )
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun Discover(
    onBack: () -> Unit,
    onDiscover: (year: Int?, genres: List<Int>?) -> Unit,
    viewModel: DiscoverViewModel = koinInject(),
    snackbarDuration: SnackbarDuration = SnackbarDuration.Short,
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val keyboardController = LocalSoftwareKeyboardController.current
    val uiState by viewModel.uiState.collectAsState()

    ErrorBar(
        error = uiState.error,
        duration = snackbarDuration,
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
                title = stringResource(resource = Res.string.discover),
                scrollBehavior = scrollBehavior,
                onBackClick = {
                    keyboardController?.hide()
                    onBack()
                }
            )
        },
        snackbarHost = {
            SnackbarHost(
                modifier = Modifier
                    .testTag(tag = ErrorBarDefaults.ErrorTestTag)
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
                onDiscover(
                    uiState.year.toIntOrNull(),
                    uiState.selectedGenres.map(Genre::id)
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
        modifier = modifier.testTag(tag = DiscoverDefaults.ContentTestTag),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Genres(
            modifier = Modifier
                .testTag(tag = DiscoverDefaults.GenresTestTag)
                .fillMaxWidth(),
            viewState = viewState.genresViewState,
            selectedGenres = viewState.selectedGenres,
            onSelect = onSelect,
            onClearSelection = onClearSelection,
            onError = onError,
        )
        Input(
            modifier = Modifier
                .testTag(tag = DiscoverDefaults.YearTestTag)
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            value = viewState.year,
            onValueChange = onYear,
        )
        Button(
            modifier = Modifier
                .testTag(tag = DiscoverDefaults.DiscoverButtonTestTag)
                .padding(all = 16.dp),
            onClick = onDiscover,
            enabled = viewState.discoverEnabled,
        ) {
            Text(text = stringResource(resource = Res.string.discover_movies))
        }
    }
}

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
        ViewState.Loading -> CircularProgressIndicator(
            modifier = Modifier.testTag(tag = DiscoverDefaults.GenresLoadingTestTag)
        )

        is ViewState.Error -> LaunchedEffect(key1 = viewState.error) {
            viewState.error?.run(onError)
        }

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
        label = { Text(text = stringResource(resource = Res.string.year)) },
        trailingIcon = {
            AnimatedVisibility(
                visible = value.isNotBlank(),
                enter = fadeIn() + scaleIn(),
                exit = fadeOut() + scaleOut(),
            ) {
                IconButton(
                    modifier = Modifier.testTag(tag = DiscoverDefaults.YearClearTestTag),
                    onClick = { onValueChange("") }
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Clear,
                        contentDescription = stringResource(resource = Res.string.clear),
                    )
                }
            }
        },
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
    )
}

@Preview
@Composable
internal fun DiscoverContentPreview() {
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

internal object DiscoverDefaults {

    const val ContentTestTag = "content"
    const val GenresTestTag = "content_genres"
    const val GenresLoadingTestTag = "content_genres_loading"
    const val YearTestTag = "content_year"
    const val YearClearTestTag = "content_year_clear"
    const val DiscoverButtonTestTag = "content_discover_button"

}
