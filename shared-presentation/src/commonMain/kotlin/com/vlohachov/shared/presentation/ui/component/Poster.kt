package com.vlohachov.shared.presentation.ui.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.BrokenImage
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil3.compose.rememberAsyncImagePainter
import com.vlohachov.shared.presentation.ui.theme.MoviesPotTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
internal fun Poster(
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
        modifier = modifier.testTag(tag = PosterDefaults.PosterTestTag),
        shape = shape,
        color = color,
        contentColor = contentColor,
        tonalElevation = tonalElevation,
        shadowElevation = shadowElevation,
        border = border,
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Image(
                modifier = Modifier
                    .testTag(tag = PosterDefaults.ImageTestTag)
                    .fillMaxSize(),
                painter = painter,
                contentScale = ContentScale.Crop,
                contentDescription = contentDescription,
            )
            if (error) {
                Icon(
                    modifier = Modifier
                        .testTag(tag = PosterDefaults.ErrorTestTag)
                        .size(size = 48.dp)
                        .align(alignment = Alignment.Center),
                    imageVector = Icons.Rounded.BrokenImage,
                    contentDescription = null,
                )
            }
        }
    }
}

@Composable
internal fun Poster(
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
        modifier = modifier.testTag(tag = PosterDefaults.PosterTestTag),
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
                modifier = Modifier
                    .testTag(tag = PosterDefaults.ImageTestTag)
                    .fillMaxSize(),
                painter = painter,
                contentScale = ContentScale.Crop,
                contentDescription = contentDescription,
            )
            if (error) {
                Icon(
                    modifier = Modifier
                        .testTag(tag = PosterDefaults.ErrorTestTag)
                        .size(size = 48.dp)
                        .align(alignment = Alignment.Center),
                    imageVector = Icons.Rounded.BrokenImage,
                    contentDescription = null,
                )
            }
        }
    }
}

internal object PosterDefaults {

    const val PosterTestTag: String = "poster"
    const val ImageTestTag: String = "poster_image"
    const val ErrorTestTag: String = "poster_error"

    val Shape: Shape = RoundedCornerShape(size = 16.dp)
    val TonalElevation: Dp = 4.dp
    val ShadowElevation: Dp = 0.dp

}

@Preview
@Composable
internal fun PosterPreview() {
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
                painter = rememberAsyncImagePainter(model = null),
                error = true,
            )
            Poster(
                modifier = Modifier
                    .weight(weight = 1f)
                    .aspectRatio(ratio = .75f),
                painter = rememberAsyncImagePainter(model = null),
                onClick = { },
            )
        }
    }
}
