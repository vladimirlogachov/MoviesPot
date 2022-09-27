package com.vlohachov.moviespot.ui.image

import android.content.Context
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.core.app.ApplicationProvider
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.vlohachov.moviespot.R
import com.vlohachov.moviespot.ui.theme.MoviesPotTheme
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Rule
import org.junit.Test

class FullscreenImageTest {

    private companion object {
        const val TestImage =
            "https://mobimg.b-cdn.net/v3/fetch/62/62e3ce60fc426fe6f475764cd99779b9.jpeg"
    }

    @get:Rule
    val composeRule = createComposeRule()

    private val navigator = mockk<DestinationsNavigator>()

    private val context = ApplicationProvider.getApplicationContext<Context>()

    @Test
    fun imageLoadedTest(): Unit = with(composeRule) {
        setContent {
            MoviesPotTheme {
                FullscreenImage(
                    navigator = navigator,
                    path = TestImage,
                )
            }
        }

        onAllNodes(matcher = isImage(), useUnmergedTree = true)
            .assertCountEquals(expectedSize = 1)
            .onFirst()
            .assertExists(errorMessageOnFail = "No Image component found.")
            .assertIsDisplayed()
            .assertContentDescriptionEquals(TestImage)
    }

    @Test
    fun imageNotLoadedTest(): Unit = with(composeRule) {
        setContent {
            MoviesPotTheme {
                FullscreenImage(
                    navigator = navigator,
                    path = "",
                )
            }
        }

        onAllNodes(matcher = isImage(), useUnmergedTree = true)
            .assertCountEquals(expectedSize = 2)
            .onLast()
            .assertExists(errorMessageOnFail = "No Image error component found.")
            .assertIsDisplayed()
            .assertContentDescriptionEquals(context.getString(R.string.image_loading_failed))
    }

    @Test
    fun navigateUpTest(): Unit = with(composeRule) {
        every { navigator.navigateUp() } returns true

        setContent {
            MoviesPotTheme {
                FullscreenImage(
                    navigator = navigator,
                    path = TestImage,
                )
            }
        }

        onNode(matcher = hasClickAction(), useUnmergedTree = true)
            .assertExists(errorMessageOnFail = "No Button back component found.")
            .assertIsDisplayed()
            .performClick()

        verify(exactly = 1) { navigator.navigateUp() }
    }

    private fun isImage(): SemanticsMatcher =
        SemanticsMatcher.expectValue(SemanticsProperties.Role, Role.Image)
}