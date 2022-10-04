package com.vlohachov.moviespot.ui.details

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.google.accompanist.flowlayout.FlowRow
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.vlohachov.domain.model.Company
import com.vlohachov.domain.model.movie.Movie
import com.vlohachov.domain.model.movie.MovieDetails
import com.vlohachov.domain.model.movie.keyword.Keyword
import com.vlohachov.moviespot.R
import com.vlohachov.moviespot.core.LoremIpsum
import com.vlohachov.moviespot.core.ViewState
import com.vlohachov.moviespot.core.utils.DateUtils
import com.vlohachov.moviespot.core.utils.DecimalUtils
import com.vlohachov.moviespot.core.utils.TimeUtils
import com.vlohachov.moviespot.ui.components.Company
import com.vlohachov.moviespot.ui.components.Poster
import com.vlohachov.moviespot.ui.components.movie.MoviesSection
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
    val unknownErrorText = stringResource(id = R.string.unknown_error_remote)
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val uiState by viewModel.uiState.collectAsState()

    uiState.error?.run {
        LaunchedEffect(snackbarHostState) {
            viewModel.onErrorConsumed()
            snackbarHostState.showSnackbar(message = localizedMessage ?: unknownErrorText)
        }
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(connection = scrollBehavior.nestedScrollConnection),
        topBar = {
            CenterAlignedTopAppBar(
                modifier = Modifier.fillMaxWidth(),
                title = { },
                navigationIcon = {
                    IconButton(
                        modifier = Modifier.semantics {
                            testTag = MovieDetailsDefaults.BackButtonTestTag
                        },
                        onClick = { navigator.navigateUp() },
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
        snackbarHost = {
            SnackbarHost(
                modifier = Modifier
                    .semantics {
                        testTag = MovieDetailsDefaults.ErrorBarTestTag
                    }
                    .navigationBarsPadding(),
                hostState = snackbarHostState,
            )
        },
    ) { paddingValues ->
        Content(
            modifier = Modifier
                .semantics {
                    testTag = MovieDetailsDefaults.DetailsContentTestTag
                }
                .fillMaxSize()
                .padding(paddingValues = paddingValues),
            detailsViewState = uiState.detailsViewState,
            directorViewState = uiState.directorViewState,
            keywordsViewState = uiState.keywordsViewState,
            recommendationsViewState = uiState.recommendationsViewState,
            onPoster = { path -> navigator.navigate(FullscreenImageDestination(path = path)) },
            onCast = { navigator.navigate(CastDestination(movieId = movieId)) },
            onCrew = { navigator.navigate(CrewDestination(movieId = movieId)) },
            onKeyword = { keyword ->
                navigator.navigate(
                    KeywordMoviesDestination(
                        keywordId = keyword.id,
                        keyword = keyword.name,
                    )
                )
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
            CircularProgressIndicator(
                modifier = Modifier
                    .semantics {
                        testTag = MovieDetailsDefaults.DetailsLoadingTestTag
                    }
                    .padding(all = 16.dp)
            )
        }
        is ViewState.Error -> detailsViewState.error?.run(onError)
        is ViewState.Success -> with(detailsViewState.data) {
            item {
                Headline(
                    modifier = Modifier
                        .semantics {
                            testTag = MovieDetailsDefaults.HeadlineTestTag
                        }
                        .padding(all = 16.dp)
                        .fillMaxWidth(),
                    poster = { modifier ->
                        var error by remember { mutableStateOf(false) }
                        val painter = rememberAsyncImagePainter(
                            model = posterPath,
                            onError = { error = true },
                        )

                        Poster(
                            modifier = modifier,
                            painter = painter,
                            onClick = { onPoster(posterPath) },
                            error = error,
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
                item {
                    Text(
                        modifier = Modifier
                            .semantics {
                                testTag = MovieDetailsDefaults.TaglineTestTag
                            }
                            .padding(horizontal = 16.dp),
                        text = "\"$tagline\"",
                    )
                }
            }
            item {
                BriefInfo(
                    modifier = Modifier
                        .semantics {
                            testTag = MovieDetailsDefaults.BriefInfoTestTag
                        }
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
                    OutlinedButton(
                        modifier = Modifier.semantics {
                            testTag = MovieDetailsDefaults.CastButtonTestTag
                        },
                        onClick = onCast,
                    ) {
                        Text(text = stringResource(id = R.string.cast))
                    }
                    OutlinedButton(
                        modifier = Modifier.semantics {
                            testTag = MovieDetailsDefaults.CrewButtonTestTag
                        },
                        onClick = onCrew,
                    ) {
                        Text(text = stringResource(id = R.string.crew))
                    }
                }
                Spacer(modifier = Modifier.height(height = 16.dp))
            }
            item {
                Divider(modifier = Modifier.padding(horizontal = 16.dp))
            }
            item {
                Overview(
                    modifier = Modifier
                        .semantics {
                            testTag = MovieDetailsDefaults.OverviewTestTag
                        }
                        .fillMaxWidth()
                        .padding(all = 16.dp),
                    text = overview,
                )
            }
            item {
                Production(
                    modifier = Modifier
                        .semantics {
                            testTag = MovieDetailsDefaults.ProductionTestTag
                        }
                        .fillMaxWidth(),
                    companies = productionCompanies,
                )
            }
        }
    }
}

@Composable
private fun Production(
    modifier: Modifier,
    companies: List<Company>,
) {
    Section(
        modifier = modifier,
        title = {
            SectionTitle(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                text = stringResource(id = R.string.production),
            )
        },
        horizontalAlignment = Alignment.CenterHorizontally,
        textStyles = SectionDefaults.smallTextStyles(
            contentTextStyle = MaterialTheme.typography.bodyMedium,
        ),
    ) {
        if (companies.isEmpty()) {
            Text(
                modifier = Modifier.padding(all = 16.dp),
                text = stringResource(id = R.string.no_results),
            )
        } else {
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(all = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(space = 16.dp)
            ) {
                items(items = companies) { company ->
                    var error by remember { mutableStateOf(false) }
                    val painter = rememberAsyncImagePainter(
                        model = company.logoPath,
                        onError = { error = true },
                    )

                    Company(
                        modifier = Modifier.width(width = 96.dp),
                        painter = painter,
                        name = company.name,
                        error = error,
                    )
                }
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
                    .semantics {
                        testTag = MovieDetailsDefaults.KeywordsTestTag
                    }
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                title = {
                    SectionTitle(
                        modifier = Modifier.fillMaxWidth(),
                        text = stringResource(id = R.string.keywords),
                    )
                },
                verticalArrangement = Arrangement.spacedBy(space = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                textStyles = SectionDefaults.smallTextStyles(
                    contentTextStyle = MaterialTheme.typography.bodyMedium,
                ),
            ) {
                if (viewState.data.isEmpty()) {
                    Text(text = stringResource(id = R.string.no_results))
                } else {
                    FlowRow(
                        modifier = Modifier.fillMaxWidth(),
                        mainAxisSpacing = 8.dp,
                        crossAxisSpacing = 8.dp,
                    ) {
                        for (keyword in viewState.data) {
//                            ClickableText(
//                                modifier = Modifier
//                                    .border(
//                                        width = 1.dp,
//                                        color = MaterialTheme.colorScheme.outline,
//                                        shape = SuggestionChipDefaults.shape,
//                                    )
//                                    .padding(vertical = 8.dp, horizontal = 16.dp),
//                                text = buildAnnotatedString { keyword.name },
//                                style = MaterialTheme.typography.labelMedium,
//                                onClick = { onKeyword(keyword) },
//                            )
                            Text(
                                modifier = Modifier
                                    .border(
                                        width = 1.dp,
                                        color = MaterialTheme.colorScheme.outline,
                                        shape = SuggestionChipDefaults.shape,
                                    )
                                    .clip(shape = SuggestionChipDefaults.shape)
                                    .clickable { onKeyword(keyword) }
                                    .padding(vertical = 8.dp, horizontal = 16.dp),
                                text = keyword.name,
                                style = MaterialTheme.typography.labelMedium,
                            )
                        }
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
        horizontalAlignment = Alignment.CenterHorizontally,
        textStyles = SectionDefaults.smallTextStyles(
            contentTextStyle = MaterialTheme.typography.bodyMedium,
        ),
    ) {
        Text(
            text = text.ifBlank { stringResource(id = R.string.no_results) },
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
                        text = DecimalUtils.format(number = voteAverage),
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

object MovieDetailsDefaults {

    const val BackButtonTestTag = "back_button"
    const val ErrorBarTestTag = "error_bar"
    const val DetailsContentTestTag = "details_content"
    const val DetailsLoadingTestTag = "details_loading"
    const val HeadlineTestTag = "movie_headline"
    const val TaglineTestTag = "movie_tagline"
    const val BriefInfoTestTag = "movie_brief_info"
    const val CastButtonTestTag = "movie_cast"
    const val CrewButtonTestTag = "movie_crew"
    const val OverviewTestTag = "movie_overview"
    const val ProductionTestTag = "movie_production"
    const val KeywordsTestTag = "movie_keywords"
}

@Preview(showBackground = true)
@Composable
fun HeadlinePreview() {
    MoviesPotTheme {
        Headline(
            modifier = Modifier
                .semantics {
                    testTag = MovieDetailsDefaults.HeadlineTestTag
                }
                .padding(all = 16.dp),
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
            modifier = Modifier
                .semantics {
                    testTag = MovieDetailsDefaults.BriefInfoTestTag
                }
                .padding(all = 16.dp),
            voteAverage = 7.25f,
            voteCount = 567,
            isAdult = true,
            runtime = 127,
        )
    }
}

@Preview(showBackground = true)
@Composable
fun OverviewPreview() {
    MoviesPotTheme {
        Overview(
            modifier = Modifier.semantics {
                testTag = MovieDetailsDefaults.OverviewTestTag
            },
            text = LoremIpsum,
        )
    }
}
