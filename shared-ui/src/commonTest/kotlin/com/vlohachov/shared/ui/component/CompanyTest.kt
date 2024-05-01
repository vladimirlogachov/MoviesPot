package com.vlohachov.shared.ui.component

import androidx.compose.ui.test.ComposeUiTest
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.runComposeUiTest
import com.vlohachov.shared.ui.theme.MoviesPotTheme
import moviespot.shared_ui.generated.resources.Res
import moviespot.shared_ui.generated.resources.ic_launcher_foreground
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import kotlin.js.JsName
import kotlin.test.Test

@OptIn(ExperimentalTestApi::class, ExperimentalResourceApi::class)
class CompanyTest {

    @Test
    @JsName("company_displayed_with_no_error")
    fun `company displayed with no error`() = runComposeUiTest {
        company(error = false)
        verifyCompany(error = false)
    }

    @Test
    @JsName("company_displayed_with_error")
    fun `company displayed with error`() = runComposeUiTest {
        company(error = true)
        verifyCompany(error = true)
    }

    private fun ComposeUiTest.company(error: Boolean) {
        setContent {
            MoviesPotTheme {
                if (error) {
                    Company(
                        painter = painterResource(resource = Res.drawable.ic_launcher_foreground),
                        name = "Company",
                        error = true,
                    )
                } else {
                    Company(
                        painter = painterResource(resource = Res.drawable.ic_launcher_foreground),
                        name = "Company",
                    )
                }
            }
        }
    }

    private fun ComposeUiTest.verifyCompany(error: Boolean) {
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
