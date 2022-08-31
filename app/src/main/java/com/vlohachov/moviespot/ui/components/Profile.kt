package com.vlohachov.moviespot.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.vlohachov.moviespot.R
import com.vlohachov.moviespot.ui.theme.MoviesPotTheme

@Composable
fun Profile(
    modifier: Modifier,
    title: String,
    body: String,
    painter: Painter,
    onClick: () -> Unit,
    shape: Shape = RoundedCornerShape(size = 16.dp),
) {
    Surface(
        modifier = modifier,
        shape = shape,
        tonalElevation = 4.dp,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onClick),
        ) {
            Image(
                modifier = Modifier
                    .weight(weight = 2f)
                    .fillMaxWidth()
                    .background(color = MaterialTheme.colorScheme.surfaceVariant),
                painter = painter,
                contentScale = ContentScale.Crop,
                contentDescription = title,
            )
            Column(
                modifier = Modifier
                    .padding(vertical = 8.dp, horizontal = 12.dp),
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

@Preview(showBackground = true)
@Composable
fun ProfilePreview() {
    MoviesPotTheme {
        Profile(
            modifier = Modifier
                .padding(all = 16.dp)
                .height(intrinsicSize = IntrinsicSize.Min)
                .width(width = 160.dp),
            title = "Title",
            body = "Body",
            painter = painterResource(id = R.drawable.ic_launcher_background),
            onClick = {},
        )
    }
}