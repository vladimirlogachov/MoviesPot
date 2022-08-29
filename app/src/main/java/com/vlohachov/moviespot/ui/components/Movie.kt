package com.vlohachov.moviespot.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.vlohachov.domain.model.Movie
import com.vlohachov.moviespot.core.DummyMovie
import com.vlohachov.moviespot.ui.theme.MoviesPotTheme

@Composable
fun Movie(
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

@Preview(showBackground = true)
@Composable
fun MoviePreview() {
    MoviesPotTheme {
        Movie(
            modifier = Modifier
                .padding(all = 16.dp)
                .size(width = 160.dp, height = 160.dp),
            movie = DummyMovie,
        )
    }
}