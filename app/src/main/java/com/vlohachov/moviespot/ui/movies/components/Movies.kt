package com.vlohachov.moviespot.ui.movies.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.vlohachov.domain.model.Movie
import com.vlohachov.moviespot.core.DummyMovies
import com.vlohachov.moviespot.core.ViewState
import com.vlohachov.moviespot.ui.theme.MoviesPotTheme

@Composable
fun Movies(
    viewState: ViewState<List<Movie>>,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier) {
        when (viewState) {
            ViewState.Loading ->
                CircularProgressIndicator(modifier = Modifier.align(alignment = Alignment.Center))
            is ViewState.Error ->
                viewState.error?.message?.run {
                    Text(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        text = this,
                    )
                }
            is ViewState.Success ->
                MoviesLazyRow(
                    modifier = Modifier
                        .height(height = 160.dp)
                        .fillMaxWidth(),
                    movies = viewState.data,
                )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MoviesSectionPreview() {
    MoviesPotTheme {
        Column(verticalArrangement = Arrangement.spacedBy(space = 16.dp)) {
            Movies(viewState = ViewState.Loading)
            Movies(viewState = ViewState.Error(error = Throwable("Error text")))
            Movies(viewState = ViewState.Success(data = DummyMovies))
        }
    }
}