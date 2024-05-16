package com.vlohachov.shared.ui.component.section

import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.runComposeUiTest
import com.vlohachov.shared.ui.theme.MoviesPotTheme
import kotlin.js.JsName
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalTestApi::class)
class SectionTextStylesTest {

    @Test
    @JsName(name = "default_section_text_styles")
    fun `default section text styles`() = runComposeUiTest {
        setContent {
            MoviesPotTheme {
                val textStyles = DefaultSectionTextStyles(
                    titleTextStyle = MaterialTheme.typography.bodyMedium,
                    contentTextStyle = MaterialTheme.typography.labelMedium,
                )

                assertEquals(
                    expected = MaterialTheme.typography.bodyMedium,
                    actual = textStyles.titleTextStyle().value,
                )
                assertEquals(
                    expected = MaterialTheme.typography.labelMedium,
                    actual = textStyles.contentTextStyle().value,
                )
            }
        }
    }

}
