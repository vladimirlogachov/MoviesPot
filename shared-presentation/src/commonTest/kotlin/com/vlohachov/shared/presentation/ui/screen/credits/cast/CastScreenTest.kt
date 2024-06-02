package com.vlohachov.shared.presentation.ui.screen.credits.cast

import androidx.compose.material3.SnackbarDuration
import androidx.compose.ui.test.ComposeUiTest
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.onChildren
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollToIndex
import androidx.compose.ui.test.runComposeUiTest
import com.vlohachov.shared.domain.repository.MovieRepository
import com.vlohachov.shared.domain.usecase.credits.LoadCast
import com.vlohachov.shared.presentation.TestCastMembers
import com.vlohachov.shared.presentation.TestMovieCredits
import com.vlohachov.shared.presentation.testCastMember
import com.vlohachov.shared.presentation.ui.component.bar.AppBarDefaults
import com.vlohachov.shared.presentation.ui.component.bar.ErrorBarDefaults
import com.vlohachov.shared.presentation.ui.component.button.ScrollToTopDefaults
import com.vlohachov.shared.presentation.ui.theme.MoviesPotTheme
import dev.mokkery.answering.returns
import dev.mokkery.every
import dev.mokkery.matcher.any
import dev.mokkery.mock
import dev.mokkery.verify
import dev.mokkery.verify.VerifyMode
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import moviespot.shared_presentation.generated.resources.Res
import moviespot.shared_presentation.generated.resources.cast
import org.jetbrains.compose.resources.getString
import kotlin.js.JsName
import kotlin.test.Ignore
import kotlin.test.Test

@OptIn(ExperimentalTestApi::class)
class CastScreenTest {

    private val repository = mock<MovieRepository> {
        every {
            getMovieCredits(id = any(), language = any())
        } returns emptyFlow()
    }

    private val loadCast = LoadCast(repository = repository)

    @Test
    @JsName(name = "check_app_bar_title")
    fun `check app bar title`() = runComposeUiTest {
        testContent()
        onNodeWithText(text = runBlocking { getString(resource = Res.string.cast) })
            .assertExists(errorMessageOnFail = "No Title component found.")
            .assertIsDisplayed()
    }

    @Test
    @JsName(name = "check_back_button")
    fun `check back button`() = runComposeUiTest {
        val onBack = mock<() -> Unit> {
            every { invoke() } returns Unit
        }
        testContent(onBack = onBack)
        onNodeWithTag(testTag = AppBarDefaults.BackButtonTestTag)
            .assertExists(errorMessageOnFail = "No Back button component found.")
            .assertIsDisplayed()
            .assertHasClickAction()
            .performClick()
        verify(mode = VerifyMode.atMost(n = 1)) { onBack() }
    }

    @Test
    @JsName(name = "check_cast_loading")
    fun `check cast loading`() = runComposeUiTest {
        testContent()
        onNodeWithTag(testTag = ErrorBarDefaults.ErrorTestTag)
            .assertExists(errorMessageOnFail = "No Error component found.")
            .assertIsNotDisplayed()
        onNodeWithTag(testTag = CastDefaults.ContentTestTag)
            .assertExists(errorMessageOnFail = "No Content component found.")
            .assertIsDisplayed()
            .onChildren()
            .assertCountEquals(expectedSize = 1)
        onNodeWithTag(testTag = CastDefaults.ContentLoadingTestTag)
            .assertExists(errorMessageOnFail = "No Progress component found.")
            .assertIsDisplayed()
    }

    @Test
    @JsName(name = "check_cast_loading_success")
    fun `check cast loading success`() = runComposeUiTest {
        every {
            repository.getMovieCredits(id = any(), language = any())
        } returns flowOf(value = TestMovieCredits)
        testContent()
        onNodeWithTag(testTag = ErrorBarDefaults.ErrorTestTag)
            .assertExists(errorMessageOnFail = "No Error component found.")
            .assertIsNotDisplayed()
        onNodeWithTag(testTag = CastDefaults.ContentTestTag)
            .assertExists(errorMessageOnFail = "No Content component found.")
            .assertIsDisplayed()
            .onChildren()
            .assertCountEquals(expectedSize = TestCastMembers.size)
    }

    @Test
    @JsName(name = "check_cast_loading_error")
    fun `check cast loading error`() = runComposeUiTest {
        every {
            repository.getMovieCredits(id = any(), language = any())
        } returns flow { error(message = "Error") }
        testContent()
        onNodeWithTag(testTag = ErrorBarDefaults.ErrorTestTag)
            .assertExists(errorMessageOnFail = "No Error component found.")
            .assertIsDisplayed()
        onNodeWithTag(testTag = CastDefaults.ContentTestTag)
            .assertExists(errorMessageOnFail = "No Content component found.")
            .assertIsDisplayed()
            .onChildren()
            .assertCountEquals(expectedSize = 0)
    }

    @Test
    @JsName(name = "check_scroll_to_top")
    @Ignore // TODO("remove when https://github.com/coil-kt/coil/issues/1764 fixed")
    fun `check scroll to top`() = runComposeUiTest {
        val largeCast = buildList {
            repeat(times = 15) { id -> add(element = testCastMember(id = id.toLong())) }
        }
        every {
            repository.getMovieCredits(id = any(), language = any())
        } returns flowOf(value = TestMovieCredits.copy(cast = largeCast))
        testContent()
        onNodeWithTag(testTag = CastDefaults.ContentTestTag)
            .assertExists(errorMessageOnFail = "No Content component found.")
            .assertIsDisplayed()
            .performScrollToIndex(index = largeCast.size - 1)
        onNodeWithTag(testTag = ScrollToTopDefaults.ScrollToTopTestTag)
            .assertExists(errorMessageOnFail = "No ScrollToTop component found.")
            .assertIsDisplayed()
            .performClick()
        onNodeWithTag(testTag = ScrollToTopDefaults.ScrollToTopTestTag)
            .assertDoesNotExist()
    }

    private fun ComposeUiTest.testContent(onBack: () -> Unit = {}) = setContent {
        MoviesPotTheme {
            Cast(
                movieId = 1,
                onBack = onBack,
                viewModel = CastViewModel(movieId = 1, loadCast = loadCast),
                snackbarDuration = SnackbarDuration.Indefinite,
            )
        }
    }

}
