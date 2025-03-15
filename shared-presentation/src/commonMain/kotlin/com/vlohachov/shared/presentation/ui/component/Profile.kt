package com.vlohachov.shared.presentation.ui.component

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.BrokenImage
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil3.compose.rememberAsyncImagePainter
import com.vlohachov.shared.presentation.ui.theme.MoviesPotTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
internal fun Profile(
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
        modifier = modifier.testTag(tag = ProfileDefaults.ProfileTestTag),
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
internal fun Profile(
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
        modifier = modifier.testTag(tag = ProfileDefaults.ProfileTestTag),
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
                .testTag(tag = ProfileDefaults.ImageTestTag)
                .fillMaxSize(),
            painter = painter,
            contentScale = ContentScale.Crop,
            contentDescription = title,
        )
        if (error) {
            Icon(
                modifier = Modifier
                    .testTag(tag = ProfileDefaults.ErrorTestTag)
                    .size(size = 48.dp)
                    .align(alignment = Alignment.Center),
                imageVector = Icons.Rounded.BrokenImage,
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
    footerPadding: PaddingValues,
) {
    Column(
        modifier = modifier
            .background(color = MaterialTheme.colorScheme.surfaceVariant)
            .padding(paddingValues = footerPadding),
    ) {
        ProvideTextStyle(value = MaterialTheme.typography.bodySmall) {
            Text(
                modifier = Modifier.testTag(tag = ProfileDefaults.TitleTestTag),
                text = title,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
        ProvideTextStyle(value = MaterialTheme.typography.labelSmall) {
            Text(
                modifier = Modifier.testTag(tag = ProfileDefaults.BodyTestTag),
                text = body,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

internal object ProfileDefaults {

    const val ProfileTestTag: String = "profile"
    const val ImageTestTag: String = "profile_image"
    const val ErrorTestTag: String = "profile_error"
    const val TitleTestTag: String = "profile_title"
    const val BodyTestTag: String = "profile_body"

    val FooterPadding: PaddingValues = PaddingValues(vertical = 8.dp, horizontal = 16.dp)
    val Shape: Shape = RoundedCornerShape(size = 16.dp)
    val TonalElevation: Dp = 4.dp
    val ShadowElevation: Dp = 0.dp

}

@Preview
@Composable
internal fun ProfilePreview() {
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
                onClick = { },
            )
        }
    }
}
