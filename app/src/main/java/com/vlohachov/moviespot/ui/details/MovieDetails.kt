package com.vlohachov.moviespot.ui.details

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.google.accompanist.flowlayout.FlowRow
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.vlohachov.domain.model.movie.Movie
import com.vlohachov.domain.model.movie.MovieDetails
import com.vlohachov.domain.model.movie.keyword.Keyword
import com.vlohachov.moviespot.R
import com.vlohachov.moviespot.core.LoremIpsum
import com.vlohachov.moviespot.core.ViewState
import com.vlohachov.moviespot.core.utils.DateUtils
import com.vlohachov.moviespot.core.utils.DecimalUtils.format
import com.vlohachov.moviespot.core.utils.TimeUtils
import com.vlohachov.moviespot.ui.components.movie.MoviesSection
import com.vlohachov.moviespot.ui.components.movie.Poster
import com.vlohachov.moviespot.ui.components.section.Section
import com.vlohachov.moviespot.ui.components.section.SectionDefaults
import com.vlohachov.moviespot.ui.components.section.SectionTitle
import com.vlohachov.moviespot.ui.destinations.*
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
    movieTitle: String,
    viewModel: MovieDetailsViewModel = getViewModel { parametersOf(movieId) },
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
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
                            imageVector = Icons.Rounded.ArrowBack,
                            contentDescription = null,
                        )
                    }
                },
                scrollBehavior = scrollBehavior,
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
                directorViewState = uiState.directorViewState,
                keywordsViewState = uiState.keywordsViewState,
                recommendationsViewState = uiState.recommendationsViewState,
                onPoster = { path -> navigator.navigate(FullscreenImageDestination(path = path)) },
                onCast = { navigator.navigate(CastDestination(movieId = movieId)) },
                onCrew = { navigator.navigate(CrewDestination(movieId = movieId)) },
                onKeyword = { keyword ->

                },
                onMoreRecommendations = {
                    navigator.navigate(
                        SimilarMoviesDestination(
                            movieId = movieId,
                            movieTitle = movieTitle,
                        )
                    )
                },
                onMovieClick = { movie ->
                    navigator.navigate(
                        MovieDetailsDestination(
                            movieId = movie.id,
                            movieTitle = movie.title,
                        )
                    )
                },
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
    directorViewState: ViewState<String>,
    keywordsViewState: ViewState<List<Keyword>>,
    recommendationsViewState: ViewState<List<Movie>>,
    onPoster: (path: String) -> Unit,
    onCast: () -> Unit,
    onCrew: () -> Unit,
    onKeyword: (keyword: Keyword) -> Unit,
    onMoreRecommendations: () -> Unit,
    onMovieClick: (movie: Movie) -> Unit,
    onError: (error: Throwable) -> Unit,
) {
    LazyColumn(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        details(
            director = if (directorViewState is ViewState.Success) {
                directorViewState.data
            } else {
                ""
            },
            detailsViewState = detailsViewState,
            onPoster = onPoster,
            onCast = onCast,
            onCrew = onCrew,
            onError = onError,
        )
        item {
            MoviesSection(
                modifier = Modifier.fillMaxWidth(),
                title = stringResource(id = R.string.recommendations),
                viewState = recommendationsViewState,
                onMore = onMoreRecommendations,
                onMovieClick = onMovieClick,
                textStyles = SectionDefaults.smallTextStyles(
                    contentTextStyle = MaterialTheme.typography.bodyMedium
                ),
            )
        }
        keywords(
            onKeyword = onKeyword,
            viewState = keywordsViewState,
        )
    }
}

private fun LazyListScope.details(
    director: String,
    detailsViewState: ViewState<MovieDetails>,
    onPoster: (path: String) -> Unit,
    onCast: () -> Unit,
    onCrew: () -> Unit,
    onError: (error: Throwable) -> Unit,
) {
    when (detailsViewState) {
        ViewState.Loading -> item {
            CircularProgressIndicator(modifier = Modifier.padding(all = 16.dp))
        }
        is ViewState.Error -> detailsViewState.error?.run(onError)
        is ViewState.Success -> with(detailsViewState.data) {
            item {
                Headline(
                    modifier = Modifier
                        .padding(all = 16.dp)
                        .fillMaxWidth(),
                    poster = { modifier ->
                        Poster(
                            modifier = modifier,
                            painter = rememberAsyncImagePainter(model = posterPath),
                            onClick = { onPoster(posterPath) },
                        )
                    },
                    title = { Text(text = title) },
                    subtitle = {
                        Text(
                            text = buildString {
                                if (releaseDate.isNotBlank()) {
                                    append(DateUtils.format(date = releaseDate))
                                    append(" ${Chars.bullet} ")
                                }
                                append(status)
                            }
                        )
                    },
                    info = {
                        Text(
                            modifier = Modifier.padding(vertical = 8.dp),
                            text = buildString {
                                for ((index, genre) in genres.withIndex()) {
                                    append(genre.name)
                                    if (index < genres.size - 1) {
                                        append(", ")
                                    }
                                }
                            }
                        )
                        if (director.isNotBlank()) {
                            Text(text = stringResource(id = R.string.directed_by, director))
                        }
                    },
                )
            }
            if (tagline.isNotBlank()) {
                item { Text(text = "\"$tagline\"") }
            }
            item {
                BriefInfo(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(all = 16.dp),
                    voteAverage = voteAverage,
                    voteCount = voteCount,
                    isAdult = isAdult,
                    runtime = runtime,
                )
            }
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(
                        space = 16.dp,
                        alignment = Alignment.End,
                    )
                ) {
                    OutlinedButton(onClick = onCast) {
                        Text(text = stringResource(id = R.string.cast))
                    }
                    OutlinedButton(onClick = onCrew) {
                        Text(text = stringResource(id = R.string.crew))
                    }
                }
            }
            item {
                Divider(modifier = Modifier.padding(all = 16.dp))
            }
            item {
                Overview(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp, horizontal = 16.dp),
                    text = overview,
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
private fun LazyListScope.keywords(
    onKeyword: (keyword: Keyword) -> Unit,
    viewState: ViewState<List<Keyword>>,
) {
    if (viewState is ViewState.Success) {
        item {
            Section(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                title = {
                    SectionTitle(text = stringResource(id = R.string.keywords))
                },
                verticalArrangement = Arrangement.spacedBy(space = 16.dp),
                textStyles = SectionDefaults.smallTextStyles(),
            ) {
                FlowRow(
                    mainAxisSpacing = 8.dp,
                    crossAxisSpacing = 8.dp,
                ) {
                    for (keyword in viewState.data) {
                        Text(
                            modifier = Modifier
                                .border(
                                    width = 1.dp,
                                    color = MaterialTheme.colorScheme.outline,
                                    shape = SuggestionChipDefaults.shape,
                                )
                                .clickable { onKeyword(keyword) }
                                .padding(vertical = 8.dp, horizontal = 16.dp),
                            text = keyword.name,
                            style = MaterialTheme.typography.labelMedium,
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(height = 16.dp))
        }
    }
}

@Composable
private fun Overview(
    modifier: Modifier,
    text: String,
) {
    Section(
        modifier = modifier,
        title = {
            SectionTitle(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(id = R.string.overview),
            )
        },
        verticalArrangement = Arrangement.spacedBy(space = 16.dp),
        textStyles = SectionDefaults.smallTextStyles(
            contentTextStyle = MaterialTheme.typography.bodyMedium,
        ),
    ) {
        Text(text = text, color = MaterialTheme.colorScheme.onSurfaceVariant)
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
                                imageVector = Icons.Rounded.Star,
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
    subtitle: @Composable ColumnScope.() -> Unit,
    info: @Composable ColumnScope.() -> Unit,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(space = 16.dp),
    ) {
        poster(
            modifier = Modifier
                .weight(weight = 1f)
                .aspectRatio(ratio = .75f),
        )
        Column(modifier = Modifier.weight(weight = 2f)) {
            ProvideTextStyle(value = Typography.titleLarge) {
                title()
            }
            subtitle()
            ProvideTextStyle(value = Typography.bodyMedium) {
                CompositionLocalProvider(LocalContentColor provides MaterialTheme.colorScheme.onSurfaceVariant) {
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
            subtitle = {
                Text(text = "12 Mar, 2022 ${Chars.bullet} Released")
            },
            info = {
                Text(text = "Directed by Walter J. Smith")
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
        Overview(
            modifier = Modifier,
            text = LoremIpsum,
        )
    }
}