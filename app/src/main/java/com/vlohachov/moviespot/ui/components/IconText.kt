package com.vlohachov.moviespot.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import com.vlohachov.moviespot.ui.theme.MoviesPotTheme
import com.vlohachov.moviespot.ui.theme.Typography

@Composable
fun IconText(
    painter: Painter,
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
    textStyle: TextStyle = Typography.labelLarge,
    tint: Color = LocalContentColor.current,
    content: @Composable () -> Unit,
) {
    IconText(
        modifier = modifier,
        icon = { Icon(painter = painter, contentDescription = contentDescription) },
        content = content,
        textStyle = textStyle,
        tint = tint,
    )
}

@Composable
fun IconText(
    imageBitmap: ImageBitmap,
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
    textStyle: TextStyle = Typography.labelLarge,
    tint: Color = LocalContentColor.current,
    content: @Composable () -> Unit,
) {
    IconText(
        modifier = modifier,
        icon = { Icon(bitmap = imageBitmap, contentDescription = contentDescription) },
        content = content,
        textStyle = textStyle,
        tint = tint,
    )
}

@Composable
fun IconText(
    imageVector: ImageVector,
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
    textStyle: TextStyle = Typography.labelLarge,
    tint: Color = LocalContentColor.current,
    content: @Composable () -> Unit,
) {
    IconText(
        modifier = modifier,
        icon = { Icon(imageVector = imageVector, contentDescription = contentDescription) },
        content = content,
        textStyle = textStyle,
        tint = tint,
    )
}

@Composable
fun IconText(
    icon: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    textStyle: TextStyle = Typography.labelLarge,
    tint: Color = LocalContentColor.current,
    content: @Composable () -> Unit,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        CompositionLocalProvider(
            LocalContentColor provides tint
        ) {
            icon()
        }
        ProvideTextStyle(value = textStyle) {
            content()
        }
    }
}

@Preview
@Composable
fun IconTextPreview() {
    MoviesPotTheme {
        IconText(imageVector = Icons.Default.Star, contentDescription = null) {
            Text(text = "Rating")
        }
    }
}