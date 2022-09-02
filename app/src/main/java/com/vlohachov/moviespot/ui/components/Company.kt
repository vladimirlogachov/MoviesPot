package com.vlohachov.moviespot.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.vlohachov.moviespot.R
import com.vlohachov.moviespot.ui.theme.MoviesPotTheme

@Composable
fun Company(
    painter: Painter,
    name: String,
    modifier: Modifier = Modifier,
    imageSize: Dp = 64.dp,
    imageShape: Shape = CircleShape,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(space = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Surface(
            modifier = Modifier.size(size = imageSize),
            color = Color.White,
            tonalElevation = 4.dp,
            shape = imageShape,
        ) {
            Image(
                modifier = Modifier.padding(all = 12.dp),
                painter = painter,
                contentDescription = name,
                contentScale = ContentScale.Fit,
            )
        }
        Text(
            text = name,
            textAlign = TextAlign.Center,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

@Preview(showBackground = true)
@Composable
fun CompanyPreview() {
    MoviesPotTheme {
        Company(
            modifier = Modifier.padding(all = 16.dp),
            painter = painterResource(id = R.drawable.ic_launcher_background),
            name = "Company"
        )
    }
}