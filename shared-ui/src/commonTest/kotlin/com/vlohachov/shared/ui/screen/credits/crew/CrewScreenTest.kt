package com.vlohachov.shared.ui.screen.credits.crew

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
import com.vlohachov.shared.TestCrewMembers
import com.vlohachov.shared.TestMovieCredits
import com.vlohachov.shared.domain.repository.MovieRepository
import com.vlohachov.shared.domain.usecase.credits.LoadCrew
import com.vlohachov.shared.ui.component.bar.AppBarDefaults
import com.vlohachov.shared.ui.component.bar.ErrorBarDefaults
import com.vlohachov.shared.ui.component.button.ScrollToTopDefaults
import com.vlohachov.shared.ui.theme.MoviesPotTheme
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
import moviespot.shared_ui.generated.resources.Res
import moviespot.shared_ui.generated.resources.crew
import org.jetbrains.compose.resources.getString
import kotlin.js.JsName
import kotlin.test.Ignore
import kotlin.test.Test

@OptIn(ExperimentalTestApi::class)
class CrewScreenTest {

    private val repository = mock<MovieRepository> {
        every {
            getMovieCredits(id = any(), language = any())
        } returns emptyFlow()
    }

    private val loadCrew = LoadCrew(repository = repository)

    @Test
    @JsName(name = "check_app_bar_title")
    fun `check app bar title`() = runComposeUiTest {
        testContent()
        onNodeWithText(text = runBlocking { getString(resource = Res.string.crew) })
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
    @JsName(name = "check_crew_loading")
    fun `check crew loading`() = runComposeUiTest {
        testContent()
        onNodeWithTag(testTag = ErrorBarDefaults.ErrorTestTag)
            .assertExists(errorMessageOnFail = "No Error component found.")
            .assertIsNotDisplayed()
        onNodeWithTag(testTag = CrewDefaults.ContentTestTag)
            .assertExists(errorMessageOnFail = "No Content component found.")
            .assertIsDisplayed()
            .onChildren()
            .assertCountEquals(expectedSize = 1)
        onNodeWithTag(testTag = CrewDefaults.ContentLoadingTestTag)
            .assertExists(errorMessageOnFail = "No Progress component found.")
            .assertIsDisplayed()
    }

    @Test
    @JsName(name = "check_crew_loading_success")
    fun `check crew loading success`() = runComposeUiTest {
        every {
            repository.getMovieCredits(id = any(), language = any())
        } returns flowOf(value = TestMovieCredits)
        testContent()
        onNodeWithTag(testTag = ErrorBarDefaults.ErrorTestTag)
            .assertExists(errorMessageOnFail = "No Error component found.")
            .assertIsNotDisplayed()
        onNodeWithTag(testTag = CrewDefaults.ContentTestTag)
            .assertExists(errorMessageOnFail = "No Content component found.")
            .assertIsDisplayed()
            .onChildren()
            .assertCountEquals(expectedSize = TestCrewMembers.size)
    }

    @Test
    @JsName(name = "check_crew_loading_error")
    fun `check crew loading error`() = runComposeUiTest {
        every {
            repository.getMovieCredits(id = any(), language = any())
        } returns flow { error(message = "Error") }
        testContent()
        onNodeWithTag(testTag = ErrorBarDefaults.ErrorTestTag)
            .assertExists(errorMessageOnFail = "No Error component found.")
            .assertIsDisplayed()
        onNodeWithTag(testTag = CrewDefaults.ContentTestTag)
            .assertExists(errorMessageOnFail = "No Content component found.")
            .assertIsDisplayed()
            .onChildren()
            .assertCountEquals(expectedSize = 0)
    }

    @Test
    @JsName(name = "check_scroll_to_top")
    @Ignore // TODO("remove when https://github.com/coil-kt/coil/issues/1764 fixed")
    fun `check scroll to top`() = runComposeUiTest {
        val largeCrew = buildList {
            repeat(times = 5) { addAll(elements = TestCrewMembers) }
        }
        every {
            repository.getMovieCredits(id = any(), language = any())
        } returns flowOf(value = TestMovieCredits.copy(crew = largeCrew))
        testContent()
        onNodeWithTag(testTag = CrewDefaults.ContentTestTag)
            .assertExists(errorMessageOnFail = "No Content component found.")
            .assertIsDisplayed()
            .performScrollToIndex(index = largeCrew.size - 1)
        onNodeWithTag(testTag = ScrollToTopDefaults.ScrollToTopTestTag)
            .assertExists(errorMessageOnFail = "No ScrollToTop component found.")
            .assertIsDisplayed()
            .performClick()
        onNodeWithTag(testTag = ScrollToTopDefaults.ScrollToTopTestTag)
            .assertDoesNotExist()
    }

    private fun ComposeUiTest.testContent(onBack: () -> Unit = {}) = setContent {
        MoviesPotTheme {
            Crew(
                movieId = 1,
                onBack = onBack,
                viewModel = CrewViewModel(movieId = 1, loadCrew = loadCrew),
                snackbarDuration = SnackbarDuration.Indefinite,
            )
        }
    }

}
