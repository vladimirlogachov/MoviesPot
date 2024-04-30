package com.vlohachov.shared.ui.component.movie

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import androidx.compose.ui.unit.dp
import coil3.compose.rememberAsyncImagePainter
import com.vlohachov.shared.domain.model.movie.Movie
import com.vlohachov.shared.ui.component.Poster
import com.vlohachov.shared.ui.theme.MoviesPotTheme
import com.vlohachov.shared.ui.utils.DummyMovies
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
public fun MoviesLazyRow(
    movies: List<Movie>,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = MoviesLazyRowDefaults.ContentPadding,
    onClick: ((movie: Movie) -> Unit)? = null,
) {
    val itemModifier = Modifier
        .fillMaxHeight()
        .aspectRatio(
            ratio = .75f,
            matchHeightConstraintsFirst = true,
        )
    LazyRow(
        modifier = modifier
            .semantics { testTag = MoviesLazyRowDefaults.MoviesLazyRowTestTag },
        contentPadding = contentPadding,
        horizontalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        items(movies) { movie ->
            var error by remember { mutableStateOf(false) }
            val painter = rememberAsyncImagePainter(
                model = movie.posterPath,
                onError = { error = true },
            )

            if (onClick != null) {
                Poster(
                    modifier = itemModifier,
                    painter = painter,
                    contentDescription = movie.title,
                    onClick = { onClick(movie) },
                    error = error,
                )
            } else {
                Poster(
                    modifier = itemModifier,
                    painter = painter,
                    contentDescription = movie.title,
                    error = error,
                )
            }
        }
    }
}

public object MoviesLazyRowDefaults {

    internal const val MoviesLazyRowTestTag = "movies_lazy_row"

    public val ContentPadding: PaddingValues = PaddingValues(all = 16.dp)

}

@Preview
@Composable
internal fun MoviesLazyRowPreview() {
    MoviesPotTheme {
        MoviesLazyRow(
            modifier = Modifier
                .height(height = 160.dp)
                .fillMaxWidth(),
            movies = DummyMovies,
        )
    }
}
