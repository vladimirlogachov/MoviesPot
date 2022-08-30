package com.vlohachov.moviespot.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.vlohachov.domain.model.movie.Movie
import com.vlohachov.moviespot.R
import com.vlohachov.moviespot.core.DummyMovies
import com.vlohachov.moviespot.ui.theme.MoviesPotTheme

@Composable
fun MoviesLazyRow(
    movies: List<Movie>,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(all = 16.dp),
    onSeeDetails: ((movie: Movie) -> Unit)? = null,
    onSeeMore: (() -> Unit)? = null,
) {
    LazyRow(
        modifier = modifier,
        contentPadding = contentPadding,
        horizontalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        items(movies) { movie ->
            val onClickModifier = onSeeDetails?.let { onClick ->
                Modifier.clickable { onClick(movie) }
            } ?: Modifier
            Movie(
                modifier = Modifier
                    .fillMaxHeight()
                    .aspectRatio(
                        ratio = .75f,
                        matchHeightConstraintsFirst = true,
                    )
                    .then(other = onClickModifier),
                movie = movie,
            )
        }
        onSeeMore?.let { onClick ->
            item {
                TextButton(
                    modifier = Modifier
                        .fillMaxHeight()
                        .aspectRatio(
                            ratio = .75f,
                            matchHeightConstraintsFirst = true,
                        ),
                    onClick = onClick,
                ) {
                    Text(text = stringResource(id = R.string.more).uppercase())
                }
            }
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