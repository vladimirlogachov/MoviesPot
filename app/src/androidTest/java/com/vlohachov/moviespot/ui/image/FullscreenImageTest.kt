package com.vlohachov.moviespot.ui.image

import android.content.Context
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.performClick
import androidx.test.core.app.ApplicationProvider
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.vlohachov.moviespot.R
import com.vlohachov.shared.theme.MoviesPotTheme
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
                FullscreenImage(navigator = navigator, path = TestImage)
            }
        }

        onNodeWithContentDescription(label = TestImage, useUnmergedTree = true)
            .assertExists(errorMessageOnFail = "No Image component found.")
            .assertIsDisplayed()
    }

    @Test
    fun imageNotLoadedTest(): Unit = with(composeRule) {
        setContent {
            MoviesPotTheme {
                FullscreenImage(navigator = navigator, path = "")
            }
        }

        onNodeWithContentDescription(
            label = context.getString(R.string.image_loading_failed),
            useUnmergedTree = true
        ).assertExists(errorMessageOnFail = "No Image error component found.")
            .assertIsDisplayed()
    }

    @Test
    fun navigateUpTest(): Unit = with(composeRule) {
        every { navigator.navigateUp() } returns true

        setContent {
            MoviesPotTheme {
                FullscreenImage(navigator = navigator, path = TestImage)
            }
        }

        onNode(matcher = hasClickAction(), useUnmergedTree = true)
            .assertExists(errorMessageOnFail = "No Button back component found.")
            .assertIsDisplayed()
            .performClick()

        verify(exactly = 1) { navigator.navigateUp() }
    }

}
