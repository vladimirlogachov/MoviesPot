package com.vlohachov.moviespot.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.vlohachov.moviespot.R
import com.vlohachov.shared.theme.MoviesPotTheme

@Composable
fun Profile(
    painter: Painter,
    title: String,
    body: String,
    modifier: Modifier = Modifier,
    error: Boolean = false,
    footerPadding: PaddingValues = ProfileDefaults.FooterPadding,
    shape: Shape = ProfileDefaults.Shape,
    color: Color = MaterialTheme.colorScheme.surface,
    contentColor: Color = contentColorFor(color),
    tonalElevation: Dp = ProfileDefaults.TonalElevation,
    shadowElevation: Dp = ProfileDefaults.ShadowElevation,
    border: BorderStroke? = null,
) {
    Surface(
        modifier = modifier
            .semantics {
                testTag = ProfileDefaults.ProfileTestTag
            },
        shape = shape,
        color = color,
        contentColor = contentColor,
        tonalElevation = tonalElevation,
        shadowElevation = shadowElevation,
        border = border,
    ) {
        Content(
            modifier = Modifier.fillMaxSize(),
            painter = painter,
            title = title,
            body = body,
            error = error,
            footerPadding = footerPadding,
        )
    }
}

@Composable
fun Profile(
    painter: Painter,
    title: String,
    body: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    error: Boolean = false,
    footerPadding: PaddingValues = ProfileDefaults.FooterPadding,
    shape: Shape = ProfileDefaults.Shape,
    color: Color = MaterialTheme.colorScheme.surface,
    contentColor: Color = contentColorFor(color),
    tonalElevation: Dp = ProfileDefaults.TonalElevation,
    shadowElevation: Dp = ProfileDefaults.ShadowElevation,
    border: BorderStroke? = null,
) {
    Surface(
        modifier = modifier
            .semantics {
                testTag = ProfileDefaults.ProfileTestTag
            },
        onClick = onClick,
        shape = shape,
        color = color,
        contentColor = contentColor,
        tonalElevation = tonalElevation,
        shadowElevation = shadowElevation,
        border = border,
    ) {
        Content(
            modifier = Modifier.fillMaxSize(),
            painter = painter,
            title = title,
            body = body,
            error = error,
            footerPadding = footerPadding,
        )
    }
}

@Composable
private fun Content(
    modifier: Modifier,
    painter: Painter,
    title: String,
    body: String,
    error: Boolean,
    footerPadding: PaddingValues,
) {
    Column(modifier = modifier) {
        Image(
            modifier = Modifier
                .weight(weight = 2f)
                .fillMaxWidth(),
            painter = painter,
            title = title,
            error = error,
        )
        Footer(
            modifier = Modifier.fillMaxWidth(),
            title = title,
            body = body,
            footerPadding = footerPadding,
        )
    }
}

@Composable
private fun Image(
    modifier: Modifier,
    painter: Painter,
    title: String,
    error: Boolean,
) {
    Box(modifier = modifier) {
        Image(
            modifier = Modifier
                .semantics {
                    testTag = ProfileDefaults.ImageTestTag
                }
                .fillMaxSize(),
            painter = painter,
            contentScale = ContentScale.Crop,
            contentDescription = title,
        )
        if (error) {
            Icon(
                modifier = Modifier
                    .semantics {
                        testTag = ProfileDefaults.ErrorTestTag
                    }
                    .size(size = 48.dp)
                    .align(alignment = Alignment.Center),
                painter = painterResource(id = R.drawable.ic_round_broken_image_24),
                contentDescription = null,
            )
        }
    }
}

@Composable
private fun Footer(
    modifier: Modifier,
    title: String,
    body: String,
    footerPadding: PaddingValues
) {
    Column(
        modifier = modifier
            .background(color = MaterialTheme.colorScheme.surfaceVariant)
            .padding(paddingValues = footerPadding),
    ) {
        ProvideTextStyle(value = MaterialTheme.typography.bodySmall) {
            Text(
                modifier = Modifier
                    .semantics {
                        testTag = ProfileDefaults.TitleTestTag
                    },
                text = title,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
        ProvideTextStyle(value = MaterialTheme.typography.labelSmall) {
            Text(
                modifier = Modifier.semantics {
                    testTag = ProfileDefaults.BodyTestTag
                },
                text = body,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

object ProfileDefaults {

    const val ProfileTestTag = "profile"
    const val ImageTestTag = "profile_image"
    const val ErrorTestTag = "profile_error"
    const val TitleTestTag = "profile_title"
    const val BodyTestTag = "profile_body"

    val FooterPadding = PaddingValues(vertical = 8.dp, horizontal = 16.dp)
    val Shape = RoundedCornerShape(size = 16.dp)
    val TonalElevation = 4.dp
    val ShadowElevation = 0.dp
}

@Preview(showBackground = true)
@Composable
fun ProfilePreview() {
    MoviesPotTheme {
        Row(
            modifier = Modifier
                .padding(all = 16.dp)
                .height(intrinsicSize = IntrinsicSize.Min),
            horizontalArrangement = Arrangement.spacedBy(space = 16.dp),
        ) {
            Profile(
                modifier = Modifier
                    .weight(weight = 1f)
                    .aspectRatio(ratio = .75f),
                title = "Title",
                body = "Body",
                painter = rememberAsyncImagePainter(model = null),
                error = true,
            )
            Profile(
                modifier = Modifier
                    .weight(weight = 1f)
                    .aspectRatio(ratio = .75f),
                title = "Title",
                body = "Body",
                painter = rememberAsyncImagePainter(model = null),
                onClick = {},
            )
        }
    }
}
