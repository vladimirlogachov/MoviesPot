package com.vlohachov.moviespot.ui.details

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.vlohachov.moviespot.R
import com.vlohachov.moviespot.ui.theme.MoviesPotTheme

@Composable
fun Credits(
    modifier: Modifier = Modifier,
    onCast: () -> Unit,
    onCrew: () -> Unit,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(
            space = 16.dp,
            alignment = Alignment.End,
        )
    ) {
        OutlinedButton(
            modifier = Modifier.semantics {
                testTag = CreditsDefaults.CastButtonTestTag
            },
            onClick = onCast,
        ) {
            Text(text = stringResource(id = R.string.cast))
        }
        OutlinedButton(
            modifier = Modifier.semantics {
                testTag = CreditsDefaults.CrewButtonTestTag
            },
            onClick = onCrew,
        ) {
            Text(text = stringResource(id = R.string.crew))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CreditsPreview() {
    MoviesPotTheme {
        Credits(
            modifier = Modifier.padding(all = 16.dp),
            onCast = {},
            onCrew = {},
        )
    }
}

object CreditsDefaults {
    const val CastButtonTestTag = "CastButtonTestTag"
    const val CrewButtonTestTag = "CrewButtonTestTag"
}
