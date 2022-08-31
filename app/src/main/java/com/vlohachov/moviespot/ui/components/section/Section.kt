package com.vlohachov.moviespot.ui.components.section

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.vlohachov.moviespot.ui.theme.MoviesPotTheme
import com.vlohachov.moviespot.ui.theme.Typography

@Composable
fun SectionTitle(
    text: String,
    modifier: Modifier = Modifier,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.Start,
    trailing: @Composable (() -> Unit)? = null,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = horizontalArrangement,
    ) {
        Text(text = text)
        trailing?.invoke()
    }
}

@Composable
fun Section(
    title: @Composable ColumnScope.() -> Unit,
    modifier: Modifier = Modifier,
    verticalArrangement: Arrangement.Vertical = Arrangement.Top,
    horizontalAlignment: Alignment.Horizontal = Alignment.Start,
    textStyles: SectionTextStyles = SectionDefaults.mediumTextStyles(),
    colors: SectionColors = SectionDefaults.sectionColors(),
    content: @Composable ColumnScope.() -> Unit,
) {
    Column(
        modifier = modifier,
        verticalArrangement = verticalArrangement,
        horizontalAlignment = horizontalAlignment,
    ) {
        ProvideTextStyle(value = textStyles.titleTextStyle().value) {
            CompositionLocalProvider(LocalContentColor provides colors.titleColor().value) {
                title()
            }
        }
        ProvideTextStyle(value = textStyles.contentTextStyle().value) {
            CompositionLocalProvider(LocalContentColor provides colors.contentColor().value) {
                content()
            }
        }
    }
}

object SectionDefaults {

    @Composable
    fun sectionColors(
        titleColor: Color = LocalContentColor.current,
        contentColor: Color = LocalContentColor.current,
    ): SectionColors =
        DefaultSectionColors(
            titleColor = titleColor,
            contentColor = contentColor,
        )

    @Composable
    fun largeTextStyles(
        titleTextStyle: TextStyle = Typography.headlineLarge,
        contentTextStyle: TextStyle = Typography.bodyLarge,
    ): SectionTextStyles =
        DefaultSectionTextStyles(
            titleTextStyle = titleTextStyle,
            contentTextStyle = contentTextStyle,
        )

    @Composable
    fun mediumTextStyles(
        titleTextStyle: TextStyle = Typography.headlineMedium,
        contentTextStyle: TextStyle = Typography.bodyMedium,
    ): SectionTextStyles =
        DefaultSectionTextStyles(
            titleTextStyle = titleTextStyle,
            contentTextStyle = contentTextStyle,
        )

    @Composable
    fun smallTextStyles(
        titleTextStyle: TextStyle = Typography.headlineSmall,
        contentTextStyle: TextStyle = Typography.bodySmall,
    ): SectionTextStyles =
        DefaultSectionTextStyles(
            titleTextStyle = titleTextStyle,
            contentTextStyle = contentTextStyle,
        )
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
        Column(verticalArrangement = Arrangement.spacedBy(space = 16.dp)) {
            Section(
                modifier = Modifier,
                title = { SectionTitle(text = "Large") },
                content = { Text(text = "Content") },
                textStyles = SectionDefaults.largeTextStyles(),
            )
            Section(
                modifier = Modifier,
                title = { SectionTitle(text = "Medium") },
                content = { Text(text = "Content") },
                textStyles = SectionDefaults.largeTextStyles(),
            )
            Section(
                modifier = Modifier,
                title = { SectionTitle(text = "Small") },
                content = { Text(text = "Content") },
                textStyles = SectionDefaults.smallTextStyles(),
            )
        }
    }
}