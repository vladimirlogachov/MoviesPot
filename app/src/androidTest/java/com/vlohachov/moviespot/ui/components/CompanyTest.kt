package com.vlohachov.moviespot.ui.components

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.junit4.ComposeContentTestRule
import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.unit.dp
import com.vlohachov.moviespot.R
import com.vlohachov.shared.ui.theme.MoviesPotTheme
import org.junit.Rule
import org.junit.Test

class CompanyTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun companyTest() {
        composeRule.company(error = false)
        composeRule.verifyCompany(error = false)
    }

    @Test
    fun companyErrorTest() {
        composeRule.company(error = true)
        composeRule.verifyCompany(error = true)
    }

    @Test
    fun previewTest() {
        composeRule.setContent {
            CompanyPreview()
        }

        composeRule.onAllNodes(hasTestTag(testTag = CompanyDefaults.CompanyTestTag))
            .assertCountEquals(expectedSize = 1)
    }

    private fun ComposeContentTestRule.company(error: Boolean) {
        setContent {
            MoviesPotTheme {
                if (error) {
                    Company(
                        painter = painterResource(id = R.drawable.ic_launcher_foreground),
                        name = "Company",
                        error = true,
                        imageSize = 48.dp,
                        imageShape = RoundedCornerShape(size = 4.dp),
                    )

                } else {
                    Company(
                        painter = painterResource(id = R.drawable.ic_launcher_foreground),
                        name = "Company",
                    )
                }
            }
        }
    }

    private fun ComposeTestRule.verifyCompany(error: Boolean) {
        onNodeWithTag(testTag = CompanyDefaults.CompanyTestTag, useUnmergedTree = true)
            .assertExists(errorMessageOnFail = "No Company component found.")
            .assertIsDisplayed()
        onNodeWithTag(testTag = CompanyDefaults.ImageTestTag, useUnmergedTree = true)
            .assertExists(errorMessageOnFail = "No child Image component found.")
            .assertIsDisplayed()
        with(onNodeWithTag(testTag = CompanyDefaults.ErrorTestTag, useUnmergedTree = true)) {
            if (error) {
                assertExists(errorMessageOnFail = "No child Icon error component found.")
                assertIsDisplayed()
            } else {
                assertDoesNotExist()
            }
        }
        onNodeWithTag(testTag = CompanyDefaults.NameTestTag, useUnmergedTree = true)
            .assertExists(errorMessageOnFail = "No child Text name component found.")
            .assertIsDisplayed()
            .assertTextEquals("Company")
    }
}
