package com.vlohachov.moviespot.ui.details

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Star
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.vlohachov.domain.model.movie.MovieDetails
import com.vlohachov.moviespot.R
import com.vlohachov.moviespot.core.LoremIpsum
import com.vlohachov.moviespot.core.ViewState
import com.vlohachov.moviespot.core.utils.DateUtils
import com.vlohachov.moviespot.core.utils.DecimalUtils.format
import com.vlohachov.moviespot.core.utils.TimeUtils
import com.vlohachov.moviespot.ui.components.section.Section
import com.vlohachov.moviespot.ui.components.section.SectionDefaults
import com.vlohachov.moviespot.ui.components.section.SectionTitle
import com.vlohachov.moviespot.ui.destinations.MovieCreditsDestination
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
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues = paddingValues),
        ) {
            Content(
                modifier = Modifier.fillMaxSize(),
                detailsViewState = uiState.detailsViewState,
                onCredits = { navigator.navigate(MovieCreditsDestination(movieId = movieId)) },
                onMore = {},
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
    detailsViewState: ViewState<MovieDetails>,
    onCredits: () -> Unit,
    onMore: () -> Unit,
    onError: (error: Throwable) -> Unit,
) {
    LazyColumn(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        item {
            Details(
                modifier = Modifier.fillMaxWidth(),
                viewState = detailsViewState,
                onCredits = onCredits,
                onMore = onMore,
                onError = onError,
            )
        }
    }
}

@Composable
private fun Details(
    modifier: Modifier,
    viewState: ViewState<MovieDetails>,
    onCredits: () -> Unit,
    onMore: () -> Unit,
    onError: (error: Throwable) -> Unit,
) {
    when (viewState) {
        ViewState.Loading -> CircularProgressIndicator(modifier = Modifier.padding(all = 16.dp))
        is ViewState.Error -> viewState.error?.run(onError)
        is ViewState.Success -> with(viewState.data) {
            Column(modifier = modifier) {
                Headline(
                    modifier = Modifier
                        .padding(all = 16.dp)
                        .fillMaxWidth(),
                    poster = { modifier ->
                        Image(
                            modifier = modifier,
                            painter = rememberAsyncImagePainter(model = posterPath),
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                        )
                    },
                    title = { Text(text = title) },
                    info = {
                        Text(
                            text = buildString {
                                append(DateUtils.format(date = releaseDate))
                                append(" ${Chars.bullet} ")
                                append(status)
                            }
                        )
                    },
                )
                BriefInfo(
                    modifier = Modifier
                        .padding(vertical = 8.dp, horizontal = 16.dp)
                        .fillMaxWidth(),
                    voteAverage = voteAverage,
                    voteCount = voteCount,
                    isAdult = isAdult,
                    runtime = runtime,
                )
                TextButton(
                    modifier = Modifier
                        .align(alignment = Alignment.End)
                        .padding(horizontal = 16.dp),
                    shape = ShapeDefaults.ExtraSmall,
                    onClick = onCredits,
                ) {
                    Text(text = stringResource(id = R.string.credits))
                }
                Divider(modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp))
                About(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp, horizontal = 16.dp),
                    text = overview,
                    onMore = onMore,
                )
            }
        }
    }
}

@Composable
private fun About(
    modifier: Modifier,
    text: String,
    onMore: () -> Unit,
) {
    Section(
        modifier = Modifier
            .clickable(onClick = onMore)
            .then(other = modifier),
        title = {
            SectionTitle(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(id = R.string.about_this_film),
                trailing = {
                    Icon(
                        imageVector = Icons.Default.ArrowForward,
                        contentDescription = stringResource(id = R.string.more),
                        tint = MaterialTheme.colorScheme.primary,
                    )
                },
                horizontalArrangement = Arrangement.SpaceBetween,
            )
        },
        verticalArrangement = Arrangement.spacedBy(space = 16.dp),
    ) {
        Text(
            text = text,
            maxLines = 4,
            overflow = TextOverflow.Ellipsis,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

@Composable
private fun BriefInfo(
    modifier: Modifier,
    voteAverage: Float,
    voteCount: Int,
    isAdult: Boolean,
    runtime: Int,
    tint: Color = MaterialTheme.colorScheme.onSurfaceVariant,
) {
    CompositionLocalProvider(LocalContentColor provides tint) {
        Row(
            modifier = modifier
                .then(other = Modifier.height(intrinsicSize = IntrinsicSize.Min)),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Section(
                modifier = Modifier.weight(weight = 1f),
                title = {
                    SectionTitle(
                        text = voteAverage.format(),
                        trailing = {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = null,
                            )
                        }
                    )
                },
                horizontalAlignment = Alignment.CenterHorizontally,
                textStyles = SectionDefaults.smallTextStyles(),
            ) {
                Text(text = stringResource(id = R.string.reviews, voteCount))
            }
            Divider(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(width = 1.dp)
                    .padding(vertical = 8.dp)
            )
            Section(
                modifier = Modifier.weight(weight = 1f),
                title = {
                    Text(
                        modifier = Modifier
                            .height(height = 32.dp)
                            .padding(vertical = 6.dp)
                            .border(
                                width = 1.dp,
                                color = LocalContentColor.current,
                                shape = ShapeDefaults.ExtraSmall,
                            )
                            .padding(horizontal = 4.dp),
                        text = if (isAdult) "R" else "G"
                    )
                },
                horizontalAlignment = Alignment.CenterHorizontally,
                textStyles = SectionDefaults.smallTextStyles(
                    titleTextStyle = Typography.titleSmall,
                ),
            ) {
                Text(text = stringResource(id = R.string.audience))
            }
            Divider(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(width = 1.dp)
                    .padding(vertical = 8.dp)
            )
            Section(
                modifier = Modifier.weight(weight = 1f),
                title = {
                    SectionTitle(
                        text = stringResource(
                            id = R.string.format_duration,
                            TimeUtils.hours(runtime),
                            TimeUtils.minutes(runtime),
                        ),
                    )
                },
                horizontalAlignment = Alignment.CenterHorizontally,
                textStyles = SectionDefaults.smallTextStyles(),
            ) {
                Text(text = stringResource(id = R.string.duration))
            }
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
                Text(text = "12 Mar, 2022 ${Chars.bullet} Released")
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun BriefInfoPreview() {
    MoviesPotTheme {
        BriefInfo(
            modifier = Modifier.padding(all = 16.dp),
            voteAverage = 7.25f,
            voteCount = 567,
            isAdult = true,
            runtime = 127,
        )
    }
}

@Preview(showBackground = true)
@Composable
fun AboutPreview() {
    MoviesPotTheme {
        About(
            modifier = Modifier,
            text = LoremIpsum,
            onMore = {},
        )
    }
}