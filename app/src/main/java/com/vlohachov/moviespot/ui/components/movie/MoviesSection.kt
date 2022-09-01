package com.vlohachov.moviespot.ui.components.movie

import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.vlohachov.domain.model.movie.Movie
import com.vlohachov.moviespot.R
import com.vlohachov.moviespot.core.DummyMovies
import com.vlohachov.moviespot.core.ViewState
import com.vlohachov.moviespot.ui.components.MoreButton
import com.vlohachov.moviespot.ui.components.section.*
import com.vlohachov.moviespot.ui.theme.MoviesPotTheme

@Composable
fun MoviesSection(
    title: String,
    viewState: ViewState<List<Movie>>,
    modifier: Modifier = Modifier,
    onMovieClick: ((movie: Movie) -> Unit)? = null,
    onMore: (() -> Unit)? = null,
    titlePadding: PaddingValues = PaddingValues(start = 16.dp, end = 4.dp),
    contentPadding: PaddingValues = PaddingValues(all = 16.dp),
    textStyles: SectionTextStyles = SectionDefaults.mediumTextStyles(),
    colors: SectionColors = SectionDefaults.sectionColors(),
) {
    val isEmpty = viewState is ViewState.Success && viewState.data.isEmpty()
    val moreButton: @Composable (() -> Unit)? =
        if (!isEmpty && onMore != null) {
            @Composable { MoreButton(onClick = onMore) }
        } else {
            null
        }
    Section(
        modifier = modifier,
        title = {
            SectionTitle(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(paddingValues = titlePadding),
                text = title,
                trailing = moreButton,
                horizontalArrangement = Arrangement.SpaceBetween,
            )
        },
        textStyles = textStyles,
        colors = colors,
    ) {
        Movies(
            modifier = Modifier.fillMaxWidth(),
            viewState = viewState,
            onMovieClick = onMovieClick,
            contentPadding = contentPadding,
        )
    }
}

@Composable
private fun Movies(
    viewState: ViewState<List<Movie>>,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(all = 16.dp),
    onMovieClick: ((movie: Movie) -> Unit)? = null,
) {
    Box(modifier = modifier) {
        when (viewState) {
            ViewState.Loading ->
                CircularProgressIndicator(
                    modifier = Modifier
                        .padding(paddingValues = contentPadding)
                        .align(alignment = Alignment.Center)
                )
            is ViewState.Error ->
                viewState.error?.message?.run {
                    Text(
                        modifier = Modifier.padding(paddingValues = contentPadding),
                        text = this,
                    )
                }
            is ViewState.Success ->
                if (viewState.data.isEmpty()) {
                    Text(
                        modifier = Modifier
                            .padding(paddingValues = contentPadding)
                            .align(alignment = Alignment.Center),
                        text = stringResource(id = R.string.no_results),
                    )
                } else {
                    MoviesLazyRow(
                        modifier = Modifier
                            .height(height = 168.dp)
                            .fillMaxWidth(),
                        movies = viewState.data,
                        contentPadding = contentPadding,
                        onClick = onMovieClick,
                    )
                }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MoviesSectionPreview() {
    MoviesPotTheme {
        Column {
            MoviesSection(title = "Title", viewState = ViewState.Loading)
            MoviesSection(
                title = "Title",
                viewState = ViewState.Error(error = Throwable("Error text")),
            )
            MoviesSection(title = "Title", viewState = ViewState.Success(data = DummyMovies))
            MoviesSection(
                title = "Title",
                viewState = ViewState.Success(data = DummyMovies),
                onMore = {},
            )
        }
    }
}