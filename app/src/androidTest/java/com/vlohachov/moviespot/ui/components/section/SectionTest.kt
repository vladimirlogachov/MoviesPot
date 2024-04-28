package com.vlohachov.moviespot.ui.components.section

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onChild
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.unit.dp
import com.vlohachov.shared.ui.theme.MoviesPotTheme
import org.junit.Rule
import org.junit.Test

class SectionTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun sectionTest(): Unit = with(composeRule) {
        setContent {
            MoviesPotTheme {
                Section(
                    title = { Text(text = "Text") }
                ) {
                    Text(text = "Content")
                }
            }
        }

        verifySection()
    }

    @Test
    fun sectionCustomTest(): Unit = with(composeRule) {
        setContent {
            MoviesPotTheme {
                Section(
                    modifier = Modifier.padding(all = 16.dp),
                    title = { Text(text = "Text") },
                    verticalArrangement = Arrangement.spacedBy(space = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    textStyles = SectionDefaults.smallTextStyles(),
                    colors = DefaultSectionColors(
                        titleColor = Color.Red,
                        contentColor = Color.Red,
                    ),
                ) {
                    Text(text = "Content")
                }
            }
        }

        verifySection()
    }

    @Test
    fun previewTest(): Unit = with(composeRule) {
        setContent {
            SectionPreview()
        }

        onAllNodes(hasTestTag(testTag = SectionDefaults.SectionTestTag))
            .assertCountEquals(expectedSize = 3)
    }

    private fun ComposeTestRule.verifySection() {
        onNodeWithTag(testTag = SectionDefaults.SectionTestTag, useUnmergedTree = true)
            .assertExists(errorMessageOnFail = "No Section component found.")
            .assertIsDisplayed()
        onNodeWithTag(testTag = SectionDefaults.SectionTitleTestTag, useUnmergedTree = true)
            .assertExists(errorMessageOnFail = "No child title component found")
            .assertIsDisplayed()
            .onChild()
            .assertTextEquals("Text")
        onNodeWithTag(testTag = SectionDefaults.SectionContentTestTag, useUnmergedTree = true)
            .assertExists(errorMessageOnFail = "No child content component found")
            .assertIsDisplayed()
            .onChild()
            .assertTextEquals("Content")
    }
}
