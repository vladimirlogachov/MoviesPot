package com.vlohachov.moviespot.ui.movies.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.vlohachov.domain.model.Movie
import com.vlohachov.moviespot.core.DummyMovies
import com.vlohachov.moviespot.ui.theme.MoviesPotTheme

@Composable
fun MoviesLazyRow(
    movies: List<Movie>,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(all = 16.dp),
) {
    LazyRow(
        modifier = modifier,
        contentPadding = contentPadding,
        horizontalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        items(movies) { movie ->
            Movie(
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

@Preview(showBackground = true)
@Composable
fun MoviesLazyRowPreview() {
    MoviesPotTheme {
        MoviesLazyRow(
            modifier = Modifier
                .height(height = 160.dp)
                .fillMaxWidth(),
            movies = DummyMovies,
        )
    }
}