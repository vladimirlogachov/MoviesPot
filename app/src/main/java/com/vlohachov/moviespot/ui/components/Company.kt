package com.vlohachov.moviespot.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.vlohachov.moviespot.R
import com.vlohachov.moviespot.ui.theme.MoviesPotTheme

@Composable
fun Company(
    painter: Painter,
    name: String,
    modifier: Modifier = Modifier,
    error: Boolean = false,
    imageSize: Dp = CompanyDefaults.ImageSize,
    imageShape: Shape = CompanyDefaults.ImageShape,
) {
    Column(
        modifier = modifier
            .semantics {
                testTag = CompanyDefaults.CompanyTestTag
            },
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
                    modifier = Modifier
                        .semantics {
                            testTag = CompanyDefaults.ImageTestTag
                        },
                    painter = painter,
                    contentDescription = name,
                    contentScale = ContentScale.Fit,
                )
                if (error) {
                    Icon(
                        modifier = Modifier
                            .semantics {
                                testTag = CompanyDefaults.ErrorTestTag
                            },
                        painter = painterResource(id = R.drawable.ic_round_broken_image_24),
                        contentDescription = null,
                        tint = Color.Black,
                    )
                }
            }
        }
        Text(
            modifier = Modifier
                .semantics {
                    testTag = CompanyDefaults.NameTestTag
                },
            text = name,
            textAlign = TextAlign.Center,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

object CompanyDefaults {

    const val CompanyTestTag = "company"
    const val ImageTestTag = "company_image"
    const val ErrorTestTag = "company_error"
    const val NameTestTag = "company_name"

    val ImageShape = CircleShape
    val ImageSize = 64.dp
}

@Preview(showBackground = true)
@Composable
fun CompanyPreview() {
    MoviesPotTheme {
        Company(
            modifier = Modifier.padding(all = 16.dp),
            painter = rememberAsyncImagePainter(model = null),
            name = "Company"
        )
    }
}