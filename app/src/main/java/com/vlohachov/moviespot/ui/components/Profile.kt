package com.vlohachov.moviespot.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.vlohachov.moviespot.R
import com.vlohachov.moviespot.ui.theme.MoviesPotTheme

@Composable
fun Profile(
    painter: Painter,
    title: String,
    body: String,
    modifier: Modifier = Modifier,
    footerPadding: PaddingValues = ProfileDefaults.FooterPadding,
    shape: Shape = ProfileDefaults.Shape,
    color: Color = MaterialTheme.colorScheme.surface,
    contentColor: Color = contentColorFor(color),
    tonalElevation: Dp = ProfileDefaults.TonalElevation,
    shadowElevation: Dp = ProfileDefaults.ShadowElevation,
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
        Column(modifier = Modifier.fillMaxSize()) {
            Image(
                modifier = Modifier
                    .weight(weight = 2f)
                    .fillMaxWidth(),
                painter = painter,
                contentScale = ContentScale.Crop,
                contentDescription = title,
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = MaterialTheme.colorScheme.surfaceVariant)
                    .padding(paddingValues = footerPadding),
            ) {
                ProvideTextStyle(value = MaterialTheme.typography.bodySmall) {
                    Text(
                        text = title,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
                ProvideTextStyle(value = MaterialTheme.typography.labelSmall) {
                    Text(
                        text = body,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Profile(
    painter: Painter,
    title: String,
    body: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    footerPadding: PaddingValues = ProfileDefaults.FooterPadding,
    shape: Shape = ProfileDefaults.Shape,
    color: Color = MaterialTheme.colorScheme.surface,
    contentColor: Color = contentColorFor(color),
    tonalElevation: Dp = ProfileDefaults.TonalElevation,
    shadowElevation: Dp = ProfileDefaults.ShadowElevation,
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
        Column(modifier = Modifier.fillMaxSize()) {
            Image(
                modifier = Modifier
                    .weight(weight = 2f)
                    .fillMaxWidth(),
                painter = painter,
                contentScale = ContentScale.Crop,
                contentDescription = title,
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = MaterialTheme.colorScheme.surfaceVariant)
                    .padding(paddingValues = footerPadding),
            ) {
                ProvideTextStyle(value = MaterialTheme.typography.bodySmall) {
                    Text(
                        text = title,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
                ProvideTextStyle(value = MaterialTheme.typography.labelSmall) {
                    Text(
                        text = body,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
        }
    }
}

object ProfileDefaults {

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
                painter = painterResource(id = R.drawable.ic_launcher_background),
            )
            Profile(
                modifier = Modifier
                    .weight(weight = 1f)
                    .aspectRatio(ratio = .75f),
                title = "Title",
                body = "Body",
                painter = painterResource(id = R.drawable.ic_launcher_background),
                onClick = {},
            )
        }
    }
}