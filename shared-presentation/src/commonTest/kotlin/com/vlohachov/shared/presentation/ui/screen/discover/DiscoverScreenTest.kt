package com.vlohachov.shared.presentation.ui.screen.discover

import androidx.compose.material3.SnackbarDuration
import androidx.compose.ui.test.ComposeUiTest
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.assertIsNotSelected
import androidx.compose.ui.test.assertIsSelected
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.onChildren
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.runComposeUiTest
import com.vlohachov.shared.domain.repository.GenreRepository
import com.vlohachov.shared.domain.usecase.LoadGenres
import com.vlohachov.shared.presentation.TestGenres
import com.vlohachov.shared.presentation.ui.component.bar.AppBarDefaults
import com.vlohachov.shared.presentation.ui.component.bar.ErrorBarDefaults
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
import moviespot.shared_presentation.generated.resources.discover
import moviespot.shared_presentation.generated.resources.year
import org.jetbrains.compose.resources.getString
import kotlin.js.JsName
import kotlin.test.Test

@OptIn(ExperimentalTestApi::class)
class DiscoverScreenTest {

    private val repository = mock<GenreRepository> {
        every { getGenres(language = any()) } returns emptyFlow()
    }

    private val loadGenres = LoadGenres(repository = repository)

    @Test
    @JsName(name = "check_app_bar_title")
    fun `check app bar title`() = runComposeUiTest {
        testContent()
        onNodeWithText(text = runBlocking { getString(resource = Res.string.discover) })
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
    @JsName(name = "check_genres_initial_loading")
    fun `check genres initial loading`() = runComposeUiTest {
        testContent()
        onNodeWithTag(testTag = DiscoverDefaults.GenresLoadingTestTag)
            .assertExists(errorMessageOnFail = "No Progress component found.")
            .assertIsDisplayed()
    }

    @Test
    @JsName(name = "check_genres_loading_success")
    fun `check genres loading success`() = runComposeUiTest {
        every { repository.getGenres(language = any()) } returns flowOf(value = TestGenres)
        testContent()
        onNodeWithTag(testTag = DiscoverDefaults.GenresTestTag)
            .assertExists(errorMessageOnFail = "No Genres component found.")
            .assertIsDisplayed()
            .onChildren()
            .assertCountEquals(expectedSize = TestGenres.size)
    }

    @Test
    @JsName(name = "check_genres_loading_error")
    fun `check genres loading error`() = runComposeUiTest {
        every { repository.getGenres(language = any()) } returns flow { error(message = "Error") }
        testContent()
        onNodeWithTag(testTag = DiscoverDefaults.GenresTestTag)
            .assertDoesNotExist()
        onNodeWithTag(testTag = ErrorBarDefaults.ErrorTestTag)
            .assertExists(errorMessageOnFail = "No Error component found.")
            .assertIsDisplayed()
    }

    @Test
    @JsName(name = "check_genres_selection")
    fun `check genres selection`() = runComposeUiTest {
        every { repository.getGenres(language = any()) } returns flowOf(value = TestGenres)
        testContent()
        onNodeWithTag(testTag = DiscoverDefaults.GenresTestTag, useUnmergedTree = true)
            .assertExists(errorMessageOnFail = "No Genres component found.")
            .assertIsDisplayed()
            .onChildren()
            .onFirst()
            .assertIsNotSelected()
            .performClick()
            .assertIsSelected()
            .performClick()
            .assertIsNotSelected()
    }

    @Test
    @JsName(name = "check_discover_button")
    fun `check discover button`() = runComposeUiTest {
        val onDiscover = mock<(year: Int?, genres: List<Int>?) -> Unit> {
            every { invoke(any(), any()) } returns Unit
        }
        every { repository.getGenres(language = any()) } returns flowOf(value = TestGenres)

        val viewModel = DiscoverViewModel(loadGenres = loadGenres)

        testContent(onDiscover = onDiscover, viewModel = viewModel)
        onNodeWithTag(testTag = DiscoverDefaults.DiscoverButtonTestTag)
            .assertExists(errorMessageOnFail = "No Button back component found.")
            .assertIsDisplayed()
            .assertIsNotEnabled()

        viewModel.onSelect(genre = TestGenres.first())
        viewModel.onYear(year = "2022")

        onNodeWithTag(testTag = DiscoverDefaults.DiscoverButtonTestTag)
            .assertExists(errorMessageOnFail = "No Button back component found.")
            .assertIsDisplayed()
            .assertIsEnabled()
            .performClick()

        verify(mode = VerifyMode.atMost(n = 1)) { onDiscover(any(), any()) }
    }

    @Test
    @JsName(name = "check_year_input")
    fun `check year input`() = runComposeUiTest {
        val hint = runBlocking { getString(resource = Res.string.year) }
        testContent()
        onNodeWithTag(testTag = DiscoverDefaults.YearTestTag)
            .assertExists(errorMessageOnFail = "No Year input component found.")
            .assertIsDisplayed()
        onNodeWithTag(testTag = DiscoverDefaults.YearClearTestTag)
            .assertDoesNotExist()
        onNodeWithTag(testTag = DiscoverDefaults.YearTestTag)
            .assertExists(errorMessageOnFail = "No Year input component found")
            .assertIsDisplayed()
            .assertTextEquals(hint, "")
            .performTextInput(text = "20222")
        onNodeWithTag(testTag = DiscoverDefaults.YearTestTag)
            .assertExists(errorMessageOnFail = "No Year input component found")
            .assertIsDisplayed()
            .assertTextEquals(hint, "2022")
        onNodeWithTag(testTag = DiscoverDefaults.YearClearTestTag)
            .assertExists(errorMessageOnFail = "No Clear input component found")
            .assertIsDisplayed()
            .assertHasClickAction()
            .performClick()
        onNodeWithTag(testTag = DiscoverDefaults.YearTestTag)
            .assertExists(errorMessageOnFail = "No Year input component found")
            .assertIsDisplayed()
            .assertTextEquals(hint, "")
    }

    private fun ComposeUiTest.testContent(
        onBack: () -> Unit = {},
        onDiscover: (year: Int?, genres: List<Int>?) -> Unit = { _, _ -> },
        viewModel: DiscoverViewModel = DiscoverViewModel(loadGenres = loadGenres),
    ) = setContent {
        MoviesPotTheme {
            Discover(
                onBack = onBack,
                onDiscover = onDiscover,
                viewModel = viewModel,
                snackbarDuration = SnackbarDuration.Indefinite
            )
        }
    }

}
