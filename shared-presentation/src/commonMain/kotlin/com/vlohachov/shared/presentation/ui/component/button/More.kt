package com.vlohachov.shared.presentation.ui.component.button

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.vlohachov.shared.presentation.ui.theme.MoviesPotTheme
import moviespot.shared_presentation.generated.resources.Res
import moviespot.shared_presentation.generated.resources.more
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
public fun More(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    tint: Color = MaterialTheme.colorScheme.primary,
) {
    CompositionLocalProvider(LocalContentColor provides tint) {
        IconButton(
            modifier = modifier.testTag(tag = MoreDefaults.ButtonTestTag),
            onClick = onClick,
        ) {
            Icon(
                modifier = Modifier.testTag(tag = MoreDefaults.IconTestTag),
                imageVector = Icons.AutoMirrored.Rounded.ArrowForward,
                contentDescription = stringResource(resource = Res.string.more),
            )
        }
    }
}

internal object MoreDefaults {

    internal const val ButtonTestTag = "more_button"
    internal const val IconTestTag = "more_button_icon"

}

@Preview
@Composable
internal fun MoreButtonPreview() {
    MoviesPotTheme {
        More(
            modifier = Modifier.padding(all = 16.dp),
            onClick = { },
        )
    }
}
