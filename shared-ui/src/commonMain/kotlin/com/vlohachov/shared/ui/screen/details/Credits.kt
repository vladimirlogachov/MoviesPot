package com.vlohachov.shared.ui.screen.details

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.vlohachov.shared.ui.theme.MoviesPotTheme
import moviespot.shared_ui.generated.resources.Res
import moviespot.shared_ui.generated.resources.cast
import moviespot.shared_ui.generated.resources.crew
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalResourceApi::class)
@Composable
internal fun Credits(
    modifier: Modifier = Modifier,
    onCast: () -> Unit,
    onCrew: () -> Unit,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(space = 16.dp, alignment = Alignment.End)
    ) {
        OutlinedButton(
            modifier = Modifier.testTag(tag = CreditsDefaults.CastButtonTestTag),
            onClick = onCast,
        ) {
            Text(text = stringResource(resource = Res.string.cast))
        }
        OutlinedButton(
            modifier = Modifier.testTag(tag = CreditsDefaults.CrewButtonTestTag),
            onClick = onCrew,
        ) {
            Text(text = stringResource(resource = Res.string.crew))
        }
    }
}

@Preview
@Composable
internal fun CreditsPreview() {
    MoviesPotTheme {
        Credits(
            modifier = Modifier.padding(all = 16.dp),
            onCast = {},
            onCrew = {},
        )
    }
}

internal object CreditsDefaults {

    const val CastButtonTestTag = "CastButtonTestTag"
    const val CrewButtonTestTag = "CrewButtonTestTag"

}
