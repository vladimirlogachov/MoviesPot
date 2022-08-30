package com.vlohachov.moviespot.ui.details

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.vlohachov.domain.model.movie.MovieDetails
import com.vlohachov.moviespot.R
import com.vlohachov.moviespot.core.DateUtils
import com.vlohachov.moviespot.core.TimeUtils
import com.vlohachov.moviespot.core.ViewState
import com.vlohachov.moviespot.ui.theme.MoviesPotTheme
import com.vlohachov.moviespot.ui.theme.Typography
import org.koin.androidx.compose.getViewModel
import org.koin.core.parameter.parametersOf
import kotlin.text.Typography as Chars

@OptIn(ExperimentalMaterial3Api::class)
@Destination
@Composable
fun MovieDetails(
    navigator: DestinationsNavigator,
    movieId: Long,
    viewModel: MovieDetailsViewModel = getViewModel { parametersOf(movieId) },
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
    topAppBarState: TopAppBarState = rememberTopAppBarState(),
    scrollBehavior: TopAppBarScrollBehavior = remember {
        TopAppBarDefaults.enterAlwaysScrollBehavior(topAppBarState)
    },
    topAppBarColors: TopAppBarColors = TopAppBarDefaults.centerAlignedTopAppBarColors(),
) {
    val unknownErrorText = stringResource(id = R.string.uknown_error)
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(connection = scrollBehavior.nestedScrollConnection),
        topBar = {
            CenterAlignedTopAppBar(
                modifier = Modifier.fillMaxWidth(),
                title = { },
                navigationIcon = {
                    IconButton(onClick = { navigator.navigateUp() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = null,
                        )
                    }
                },
                scrollBehavior = scrollBehavior,
                colors = topAppBarColors,
            )
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues = paddingValues),
        ) {
            Content(
                modifier = Modifier.fillMaxSize(),
                viewState = uiState.detailsViewState,
                onError = viewModel::onError,
            )

            uiState.error?.run {
                LaunchedEffect(snackbarHostState) {
                    snackbarHostState.showSnackbar(message = localizedMessage ?: unknownErrorText)
                    viewModel.onErrorConsumed()
                }
            }
        }
    }
}

@Composable
private fun Content(
    modifier: Modifier,
    viewState: ViewState<MovieDetails>,
    onError: (error: Throwable) -> Unit,
    contentPadding: PaddingValues = PaddingValues(all = 16.dp)
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = contentPadding,
    ) {
        when (viewState) {
            ViewState.Loading -> item { CircularProgressIndicator() }
            is ViewState.Error -> viewState.error?.run(onError)
            is ViewState.Success -> details(details = viewState.data)
        }
    }
}

private fun LazyListScope.details(details: MovieDetails) {
    item {
        Headline(
            modifier = Modifier
                .fillMaxWidth()
                .padding(all = 16.dp),
            poster = { modifier ->
                Image(
                    modifier = modifier,
                    painter = rememberAsyncImagePainter(model = details.posterPath),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                )
            },
            title = {
                val year = DateUtils.format(
                    date = details.releaseDate,
                    pattern = DateUtils.YEAR,
                )
                Text(text = "${details.title} ($year)")
            },
            info = {
                Text(
                    text = buildString {
                        append(DateUtils.format(date = details.releaseDate))
                        append(" ${Chars.bullet} ")
                        append(details.status)
                        append(" ${Chars.bullet} ")
                        append(
                            stringResource(
                                id = R.string.duration,
                                TimeUtils.hours(details.runtime),
                                TimeUtils.minutes(details.runtime),
                            )
                        )
                    }
                )
            },
        )
    }
    item {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(intrinsicSize = IntrinsicSize.Min),
            verticalAlignment = Alignment.CenterVertically,
        ) {

        }
    }
}

@Composable
private fun Headline(
    modifier: Modifier,
    poster: @Composable RowScope.(modifier: Modifier) -> Unit,
    title: @Composable ColumnScope.() -> Unit,
    info: @Composable ColumnScope.() -> Unit,
    posterShape: Shape = ShapeDefaults.Small,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(space = 16.dp),
    ) {
        poster(
            modifier = Modifier
                .weight(weight = 1f)
                .aspectRatio(ratio = .75f)
                .background(
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    shape = posterShape,
                )
                .clip(shape = posterShape)
        )
        Column(
            modifier = Modifier.weight(weight = 2f),
        ) {
            ProvideTextStyle(value = Typography.headlineSmall) {
                title()
            }
            ProvideTextStyle(value = Typography.bodyMedium) {
                CompositionLocalProvider(
                    LocalContentColor provides MaterialTheme.colorScheme.onSurfaceVariant
                ) {
                    info()
                }
            }
        }
    }
}

@Composable
private fun Status(
    status: String,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        Text(
            text = stringResource(id = R.string.status),
            style = Typography.labelLarge,
            color = MaterialTheme.colorScheme.secondary,
        )
        Text(
            text = status,
            style = Typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

@Preview(showBackground = true)
@Composable
fun HeadlinePreview() {
    MoviesPotTheme {
        Headline(
            modifier = Modifier.padding(all = 16.dp),
            poster = { modifier ->
                Surface(modifier = modifier, color = Color.Red) {}
            },
            title = {
                Text(text = "Title (2022)")
            },
            info = {
                Text(text = "12 Mar, 2022 ${Chars.bullet} Released ${Chars.bullet} 1h 30m")
            }
        )
    }
}