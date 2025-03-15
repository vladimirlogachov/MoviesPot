package com.vlohachov.shared.presentation.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.BrokenImage
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil3.compose.rememberAsyncImagePainter
import com.vlohachov.shared.presentation.ui.theme.MoviesPotTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
internal fun Company(
    painter: Painter,
    name: String,
    modifier: Modifier = Modifier,
    error: Boolean = false,
    imageSize: Dp = CompanyDefaults.ImageSize,
    imageShape: Shape = CompanyDefaults.ImageShape,
) {
    Column(
        modifier = modifier.testTag(tag = CompanyDefaults.CompanyTestTag),
        verticalArrangement = Arrangement.spacedBy(space = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Surface(
            modifier = Modifier.size(size = imageSize),
            color = Color.White,
            shadowElevation = 1.dp,
            shape = imageShape,
        ) {
            Box(
                modifier = Modifier.padding(all = 12.dp),
                contentAlignment = Alignment.Center,
            ) {
                Image(
                    modifier = Modifier.testTag(tag = CompanyDefaults.ImageTestTag),
                    painter = painter,
                    contentDescription = name,
                    contentScale = ContentScale.Fit,
                )
                if (error) {
                    Icon(
                        modifier = Modifier.testTag(tag = CompanyDefaults.ErrorTestTag),
                        imageVector = Icons.Rounded.BrokenImage,
                        contentDescription = null,
                        tint = Color.Black,
                    )
                }
            }
        }
        Text(
            modifier = Modifier.testTag(tag = CompanyDefaults.NameTestTag),
            text = name,
            textAlign = TextAlign.Center,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

internal object CompanyDefaults {

    const val CompanyTestTag: String = "company"
    const val ImageTestTag: String = "company_image"
    const val ErrorTestTag: String = "company_error"
    const val NameTestTag: String = "company_name"

    val ImageShape: Shape = CircleShape
    val ImageSize: Dp = 64.dp

}

@Preview
@Composable
internal fun CompanyPreview() {
    MoviesPotTheme {
        Company(
            modifier = Modifier.padding(all = 16.dp),
            painter = rememberAsyncImagePainter(model = null),
            name = "Company"
        )
    }
}
