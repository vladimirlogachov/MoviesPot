package com.vlohachov.shared.presentation.ui.screen.details

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.vlohachov.shared.presentation.ui.component.section.Section
import com.vlohachov.shared.presentation.ui.component.section.SectionDefaults
import com.vlohachov.shared.presentation.ui.component.section.SectionTitle
import com.vlohachov.shared.presentation.ui.theme.MoviesPotTheme
import com.vlohachov.shared.presentation.utils.LoremIpsum
import moviespot.shared_presentation.generated.resources.Res
import moviespot.shared_presentation.generated.resources.no_results
import moviespot.shared_presentation.generated.resources.overview
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
internal fun Overview(
    text: String,
    modifier: Modifier = Modifier,
) = Section(
    modifier = modifier.testTag(tag = OverviewDefaults.TestTag),
    title = {
        SectionTitle(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(resource = Res.string.overview),
        )
    },
    verticalArrangement = Arrangement.spacedBy(space = 16.dp),
    horizontalAlignment = Alignment.CenterHorizontally,
    textStyles = SectionDefaults.smallTextStyles(
        contentTextStyle = MaterialTheme.typography.bodyMedium,
    ),
) {
    Text(
        text = text.ifBlank { stringResource(resource = Res.string.no_results) },
        color = MaterialTheme.colorScheme.onSurfaceVariant,
    )
}

@Preview
@Composable
internal fun OverviewPreview() {
    MoviesPotTheme {
        Overview(text = LoremIpsum)
    }
}

internal object OverviewDefaults {

    const val TestTag = "OverviewTestTag"

}
