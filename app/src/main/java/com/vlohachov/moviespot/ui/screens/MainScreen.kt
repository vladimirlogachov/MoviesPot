package com.vlohachov.moviespot.ui.screens

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.vlohachov.domain.model.Movie
import com.vlohachov.moviespot.R
import com.vlohachov.moviespot.ui.theme.MoviesPotTheme
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
            Movies(
                modifier = Modifier.fillMaxSize(),
                topRatedViewState = uiState.topRatedViewState,
            )
        }
    }
}

@Composable
private fun Movies(
    modifier: Modifier,
    topRatedViewState: MoviesViewState,
) {
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(space = 16.dp),
        contentPadding = PaddingValues(vertical = 16.dp),
    ) {
        item {
            TopRated(modifier = Modifier.fillMaxWidth(), viewState = topRatedViewState)
        }
    }
}

@Composable
private fun TopRated(
    modifier: Modifier,
    viewState: MoviesViewState,
) {
    Column(
        modifier = modifier,
    ) {
        SectionTitle(
            modifier = Modifier.padding(horizontal = 16.dp),
            title = stringResource(id = R.string.top_rated),
        )

        when {
            viewState.isLoading ->
                CircularProgressIndicator(
                    modifier = Modifier.align(alignment = Alignment.CenterHorizontally)
                )
            viewState.data.isNotEmpty() ->
                MoviesRow(
                    modifier = Modifier
                        .height(height = 160.dp)
                        .fillMaxWidth(),
                    movies = viewState.data,
                )
            else ->
                viewState.error?.run {
                    Log.e("Error", "TopRated: ", this)
                }
        }
    }
}

@Composable
private fun MoviesRow(
    modifier: Modifier,
    movies: List<Movie>,
    contentPadding: PaddingValues = PaddingValues(all = 16.dp),
) {
    LazyRow(
        modifier = modifier,
        contentPadding = contentPadding,
        horizontalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        items(movies) { movie ->
            MovieItem(
                modifier = Modifier
                    .fillMaxHeight()
                    .aspectRatio(
                        ratio = .75f,
                        matchHeightConstraintsFirst = true,
                    ),
                movie = movie,
            )
        }
    }
}

@Composable
private fun MovieItem(
    modifier: Modifier,
    movie: Movie,
    shape: Shape = RoundedCornerShape(size = 16.dp)
) {
    val backgroundModifier = Modifier
        .background(
            color = MaterialTheme.colorScheme.surfaceVariant,
            shape = shape,
        )
        .clip(shape = shape)
    Image(
        modifier = modifier.then(other = backgroundModifier),
        painter = rememberAsyncImagePainter(movie.posterPath),
        contentScale = ContentScale.Crop,
        contentDescription = movie.title,
    )
}

@Composable
fun SectionTitle(
    title: String,
    modifier: Modifier = Modifier
) {
    Text(
        modifier = modifier,
        text = title,
        style = MaterialTheme.typography.headlineSmall,
    )
}

@Preview(showBackground = true)
@Composable
fun MovieItemPreview() {
    MoviesPotTheme {
        MovieItem(
            modifier = Modifier
                .padding(all = 16.dp)
                .size(width = 160.dp, height = 160.dp),
            movie = Movie(
                id = 10,
                title = "title",
                overview = "",
                releaseDate = "",
                posterPath = null,
                genreIds = listOf(),
                isAdult = false,
                voteCount = 10,
                voteAverage = 10.0f,
            ),
        )
    }
}

@Preview(showBackground = true)
@Composable
fun MoviesRowPreview() {
    MoviesPotTheme {
        MoviesRow(
            modifier = Modifier
                .height(height = 160.dp)
                .fillMaxWidth(),
            movies = listOf(
                Movie(
                    id = 10,
                    title = "title",
                    overview = "",
                    releaseDate = "",
                    posterPath = null,
                    genreIds = listOf(),
                    isAdult = false,
                    voteCount = 10,
                    voteAverage = 10.0f,
                ),
                Movie(
                    id = 10,
                    title = "title",
                    overview = "",
                    releaseDate = "",
                    posterPath = null,
                    genreIds = listOf(),
                    isAdult = false,
                    voteCount = 10,
                    voteAverage = 10.0f,
                ),
                Movie(
                    id = 10,
                    title = "title",
                    overview = "",
                    releaseDate = "",
                    posterPath = null,
                    genreIds = listOf(),
                    isAdult = false,
                    voteCount = 10,
                    voteAverage = 10.0f,
                ),
                Movie(
                    id = 10,
                    title = "title",
                    overview = "",
                    releaseDate = "",
                    posterPath = null,
                    genreIds = listOf(),
                    isAdult = false,
                    voteCount = 10,
                    voteAverage = 10.0f,
                )
            ),
        )
    }
}