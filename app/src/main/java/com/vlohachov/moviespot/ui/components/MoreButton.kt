package com.vlohachov.moviespot.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.vlohachov.moviespot.R
import com.vlohachov.moviespot.ui.theme.MoviesPotTheme

@Composable
fun MoreButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    tint: Color = MaterialTheme.colorScheme.primary,
) {
    CompositionLocalProvider(LocalContentColor provides tint) {
        IconButton(
            modifier = modifier
                .semantics {
                    testTag = MoreButtonDefaults.MoreButtonTestTag
                },
            onClick = onClick,
        ) {
            Icon(
                modifier = Modifier.semantics {
                    testTag = MoreButtonDefaults.IconTestTag
                },
                imageVector = Icons.Rounded.ArrowForward,
                contentDescription = stringResource(id = R.string.more),
            )
        }
    }
}

object MoreButtonDefaults {

    const val MoreButtonTestTag = "more_button"
    const val IconTestTag = "more_button_icon"
}

@Preview(showBackground = true)
@Composable
fun MoreButtonPreview() {
    MoviesPotTheme {
        MoreButton(
            modifier = Modifier.padding(all = 16.dp),
            onClick = { },
        )
    }
}
