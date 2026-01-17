package com.vlohachov.shared.presentation.ui.screen.discover

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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.vlohachov.shared.domain.model.genre.Genre
import com.vlohachov.shared.presentation.ui.component.bar.AppBar
import com.vlohachov.shared.presentation.ui.component.bar.ErrorBar
import com.vlohachov.shared.presentation.ui.component.bar.ErrorBarDefaults
import com.vlohachov.shared.presentation.ui.screen.Screen
import com.vlohachov.shared.presentation.ui.screen.discover.result.DiscoverResultScreen
import com.vlohachov.shared.presentation.ui.theme.MoviesPotTheme
import com.vlohachov.shared.presentation.utils.DummyGenres
import moviespot.shared_presentation.generated.resources.Res
import moviespot.shared_presentation.generated.resources.clear
import moviespot.shared_presentation.generated.resources.discover
import moviespot.shared_presentation.generated.resources.discover_movies
import moviespot.shared_presentation.generated.resources.year
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI

internal data object DiscoverScreen : Screen<Unit>() {

    override val path: String = "discover"
    override val arguments: List<NamedNavArgument> = emptyList()

    override fun route(params: Unit): String = path

    override fun NavGraphBuilder.composable(navController: NavController) {
        composable(route = path, arguments = arguments) {
            val viewModel = koinViewModel<DiscoverViewModel>()
            val uiState by viewModel.state.collectAsStateWithLifecycle()

            LaunchedEffect(viewModel) {
                viewModel.effect.collect { event ->
                    when (event) {
                        DiscoverEvent.NavigateBack ->
                            navController.navigateUp()

                        is DiscoverEvent.NavigateToResults ->
                            DiscoverResultScreen.Params(year = event.year, genres = event.genres)
                                .let(DiscoverResultScreen::route)
                                .run(navController::navigate)
                    }
                }
            }
            Discover(onAction = viewModel::onAction, state = uiState)
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class, KoinExperimentalAPI::class)
@Composable
internal fun Discover(
    onAction: (DiscoverAction) -> Unit,
    state: DiscoverViewState,
    snackbarDuration: SnackbarDuration = SnackbarDuration.Short,
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val keyboardController = LocalSoftwareKeyboardController.current

    ErrorBar(
        error = state.error,
        duration = snackbarDuration,
        snackbarHostState = snackbarHostState,
        onDismissed = { onAction(DiscoverAction.HideError) },
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
                    onAction(DiscoverAction.Back)
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
            onAction = onAction,
            state = state,
        )
    }
}

@Composable
private fun Content(
    onAction: (DiscoverAction) -> Unit,
    state: DiscoverViewState,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.testTag(tag = DiscoverDefaults.ContentTestTag),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        if (state.showProgress) CircularProgressIndicator(
            modifier = Modifier.testTag(tag = DiscoverDefaults.GenresLoadingTestTag)
        )
        else if (state.error == null) Genres(
            modifier = Modifier
                .testTag(tag = DiscoverDefaults.GenresTestTag)
                .fillMaxWidth(),
            genres = state.genres,
            selectedGenres = state.selectedGenres,
            onSelect = { genre -> onAction(DiscoverAction.SelectGenre(genre = genre)) },
            onClearSelection = { genre -> onAction(DiscoverAction.RemoveGenre(genre = genre)) },
        )
        Input(
            modifier = Modifier
                .testTag(tag = DiscoverDefaults.YearTestTag)
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            value = state.year,
            onValueChange = { value -> onAction(DiscoverAction.EnterYear(year = value)) },
        )
        Button(
            modifier = Modifier
                .testTag(tag = DiscoverDefaults.DiscoverButtonTestTag)
                .padding(all = 16.dp),
            onClick = { onAction(DiscoverAction.Discover) },
            enabled = state.discoverEnabled,
        ) {
            Text(text = stringResource(resource = Res.string.discover_movies))
        }
    }
}

@Composable
private fun Genres(
    genres: List<Genre>,
    selectedGenres: List<Genre>,
    onSelect: (genre: Genre) -> Unit,
    onClearSelection: (genre: Genre) -> Unit,
    modifier: Modifier = Modifier,
) = LazyRow(
    modifier = modifier,
    contentPadding = PaddingValues(horizontal = 16.dp),
    horizontalArrangement = Arrangement.spacedBy(space = 8.dp),
) {
    items(items = genres, key = { item -> item.id }) { genre ->
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

@[Composable Preview(showBackground = true)]
internal fun DiscoverContentPreview() {
    MoviesPotTheme {
        Discover(
            onAction = { /* no ops. */ },
            state = DiscoverViewState(
                year = "",
                genres = DummyGenres,
            ),
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
