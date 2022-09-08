package com.vlohachov.moviespot.ui.components.movie

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.vlohachov.moviespot.R
import com.vlohachov.moviespot.ui.theme.MoviesPotTheme

@Composable
fun Poster(
    painter: Painter,
    modifier: Modifier = Modifier,
    error: Boolean = false,
    contentDescription: String? = null,
    shape: Shape = PosterDefaults.Shape,
    color: Color = MaterialTheme.colorScheme.surface,
    contentColor: Color = contentColorFor(color),
    tonalElevation: Dp = PosterDefaults.TonalElevation,
    shadowElevation: Dp = PosterDefaults.ShadowElevation,
    border: BorderStroke? = null,
) {
    Surface(
        modifier = modifier,
        shape = shape,
        color = color,
        contentColor = contentColor,
        tonalElevation = tonalElevation,
        shadowElevation = shadowElevation,
        border = border,
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Image(
                modifier = Modifier.fillMaxSize(),
                painter = painter,
                contentScale = ContentScale.Crop,
                contentDescription = contentDescription,
            )
            if (error) {
                Icon(
                    modifier = Modifier
                        .size(size = 48.dp)
                        .align(alignment = Alignment.Center),
                    painter = painterResource(id = R.drawable.ic_round_broken_image_24),
                    contentDescription = null,
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Poster(
    painter: Painter,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    error: Boolean = false,
    contentDescription: String? = null,
    shape: Shape = PosterDefaults.Shape,
    color: Color = MaterialTheme.colorScheme.surface,
    contentColor: Color = contentColorFor(color),
    tonalElevation: Dp = PosterDefaults.TonalElevation,
    shadowElevation: Dp = PosterDefaults.ShadowElevation,
    border: BorderStroke? = null,
) {
    Surface(
        modifier = modifier,
        onClick = onClick,
        shape = shape,
        color = color,
        contentColor = contentColor,
        tonalElevation = tonalElevation,
        shadowElevation = shadowElevation,
        border = border,
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Image(
                modifier = Modifier.fillMaxSize(),
                painter = painter,
                contentScale = ContentScale.Crop,
                contentDescription = contentDescription,
            )
            if (error) {
                Icon(
                    modifier = Modifier
                        .size(size = 48.dp)
                        .align(alignment = Alignment.Center),
                    painter = painterResource(id = R.drawable.ic_round_broken_image_24),
                    contentDescription = null,
                )
            }
        }
    }
}

object PosterDefaults {

    val Shape = RoundedCornerShape(size = 16.dp)
    val TonalElevation = 4.dp
    val ShadowElevation = 0.dp
}

@Preview(showBackground = true)
@Composable
fun PosterPreview() {
    MoviesPotTheme {
        Row(
            modifier = Modifier
                .padding(all = 16.dp)
                .height(intrinsicSize = IntrinsicSize.Min),
            horizontalArrangement = Arrangement.spacedBy(space = 16.dp),
        ) {
            Poster(
                modifier = Modifier
                    .weight(weight = 1f)
                    .aspectRatio(ratio = .75f),
                painter = painterResource(id = R.drawable.ic_launcher_background),
                error = true,
            )
            Poster(
                modifier = Modifier
                    .weight(weight = 1f)
                    .aspectRatio(ratio = .75f),
                painter = painterResource(id = R.drawable.ic_launcher_background),
                onClick = {},
            )
        }
    }
}