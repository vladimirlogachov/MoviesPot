package com.vlohachov.moviespot.ui.components.section

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
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.vlohachov.shared.theme.MoviesPotTheme

@Composable
fun SectionTitle(
    text: String,
    modifier: Modifier = Modifier,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.Start,
    trailing: @Composable (() -> Unit)? = null,
) {
    Row(
        modifier = modifier
            .semantics {
                testTag = SectionTitleDefaults.TitleTestTag
            },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = horizontalArrangement,
    ) {
        Text(
            modifier = Modifier
                .semantics {
                    testTag = SectionTitleDefaults.TitleTextTestTag
                },
            text = text,
        )
        trailing?.run {
            Box(
                modifier = Modifier
                    .semantics {
                        testTag = SectionTitleDefaults.TitleIconTestTag
                    },
            ) {
                this@run()
            }
        }
    }
}

object SectionTitleDefaults {

    const val TitleTestTag = "title"
    const val TitleTextTestTag = "title_text"
    const val TitleIconTestTag = "title_icon"
}

@Preview(showBackground = true)
@Composable
fun SectionTitlePreview() {
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
