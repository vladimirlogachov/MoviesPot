package com.vlohachov.shared.ui.component.section

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.vlohachov.shared.ui.theme.MoviesPotTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
public fun SectionTitle(
    text: String,
    modifier: Modifier = Modifier,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.Start,
    trailing: @Composable (() -> Unit)? = null,
) {
    Row(
        modifier = modifier.testTag(tag = SectionTitleDefaults.TitleTestTag),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = horizontalArrangement,
    ) {
        Text(
            modifier = Modifier.testTag(tag = SectionTitleDefaults.TitleTextTestTag),
            text = text,
        )
        trailing?.run {
            Box(
                modifier = Modifier.testTag(tag = SectionTitleDefaults.TitleIconTestTag),
            ) {
                this@run()
            }
        }
    }
}

public object SectionTitleDefaults {

    public const val TitleTestTag: String = "title"
    public const val TitleTextTestTag: String = "title_text"
    public const val TitleIconTestTag: String = "title_icon"

}

@Preview
@Composable
internal fun SectionTitlePreview() {
    MoviesPotTheme {
        Column(verticalArrangement = Arrangement.spacedBy(space = 16.dp)) {
            SectionTitle(text = "Section title")
            SectionTitle(
                text = "Section with trailing",
                trailing = {
                    Icon(imageVector = Icons.Rounded.Info, contentDescription = null)
                }
            )
        }
    }
}
