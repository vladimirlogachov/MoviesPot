package com.vlohachov.shared.presentation.ui.component.section

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.vlohachov.shared.presentation.ui.theme.MoviesPotTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
internal fun Section(
    title: @Composable ColumnScope.() -> Unit,
    modifier: Modifier = Modifier,
    verticalArrangement: Arrangement.Vertical = Arrangement.Top,
    horizontalAlignment: Alignment.Horizontal = Alignment.Start,
    textStyles: SectionTextStyles = SectionDefaults.mediumTextStyles(),
    colors: SectionColors = SectionDefaults.sectionColors(),
    content: @Composable ColumnScope.() -> Unit,
) {
    Column(
        modifier = modifier.testTag(tag = SectionDefaults.SectionTestTag),
        verticalArrangement = verticalArrangement,
        horizontalAlignment = horizontalAlignment,
    ) {
        ProvideTextStyle(value = textStyles.titleTextStyle().value) {
            CompositionLocalProvider(LocalContentColor provides colors.titleColor().value) {
                Box(
                    modifier = Modifier.testTag(tag = SectionDefaults.SectionTitleTestTag)
                ) {
                    this@Column.title()
                }
            }
        }
        ProvideTextStyle(value = textStyles.contentTextStyle().value) {
            CompositionLocalProvider(LocalContentColor provides colors.contentColor().value) {
                Box(
                    modifier = Modifier.testTag(tag = SectionDefaults.SectionContentTestTag)
                ) {
                    this@Column.content()
                }
            }
        }
    }
}

internal object SectionDefaults {

    const val SectionTestTag: String = "section"
    const val SectionTitleTestTag: String = "section_title"
    const val SectionContentTestTag: String = "section_content"

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
        titleTextStyle: TextStyle = MaterialTheme.typography.headlineLarge,
        contentTextStyle: TextStyle = MaterialTheme.typography.bodyLarge,
    ): SectionTextStyles =
        DefaultSectionTextStyles(
            titleTextStyle = titleTextStyle,
            contentTextStyle = contentTextStyle,
        )

    @Composable
    fun mediumTextStyles(
        titleTextStyle: TextStyle = MaterialTheme.typography.headlineMedium,
        contentTextStyle: TextStyle = MaterialTheme.typography.bodyMedium,
    ): SectionTextStyles =
        DefaultSectionTextStyles(
            titleTextStyle = titleTextStyle,
            contentTextStyle = contentTextStyle,
        )

    @Composable
    fun smallTextStyles(
        titleTextStyle: TextStyle = MaterialTheme.typography.headlineSmall,
        contentTextStyle: TextStyle = MaterialTheme.typography.bodySmall,
    ): SectionTextStyles =
        DefaultSectionTextStyles(
            titleTextStyle = titleTextStyle,
            contentTextStyle = contentTextStyle,
        )
}

@Preview
@Composable
internal fun SectionPreview() {
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
