package com.vlohachov.moviespot.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.vlohachov.moviespot.ui.theme.MoviesPotTheme

@Composable
fun SectionTitle(
    text: String,
    modifier: Modifier = Modifier,
    trailing: @Composable (() -> Unit)? = null,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.headlineSmall,
        )
        trailing?.invoke()
    }
}

@Composable
fun Section(
    modifier: Modifier,
    title: @Composable ColumnScope.() -> Unit,
    content: @Composable ColumnScope.() -> Unit,
) {
    Column(modifier = modifier) {
        title()
        content()
    }
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
                    Icon(imageVector = Icons.Default.Info, contentDescription = null)
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SectionPreview() {
    MoviesPotTheme {
        Section(
            modifier = Modifier,
            title = { SectionTitle(text = "Title") },
            content = { Text(text = "Content") },
        )
    }
}