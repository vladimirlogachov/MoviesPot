package com.vlohachov.shared.presentation.ui.screen.discover

import androidx.compose.material3.SnackbarDuration
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import app.cash.turbine.test
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
import kotlin.test.assertEquals

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
        val viewModel = DiscoverViewModel(loadGenres = loadGenres)
        testContent(viewModel = viewModel)

        onNodeWithTag(testTag = AppBarDefaults.BackButtonTestTag)
            .assertExists(errorMessageOnFail = "No Back button component found.")
            .assertIsDisplayed()
            .assertHasClickAction()
            .performClick()

        viewModel.effect.test {
            assertEquals(
                expected = DiscoverEvent.NavigateBack,
                actual = awaitItem(),
            )
        }
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
        waitForIdle()
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
        every { repository.getGenres(language = any()) } returns flowOf(value = TestGenres)

        val viewModel = DiscoverViewModel(loadGenres = loadGenres)

        testContent(viewModel = viewModel)
        onNodeWithTag(testTag = DiscoverDefaults.DiscoverButtonTestTag)
            .assertExists(errorMessageOnFail = "No Button back component found.")
            .assertIsDisplayed()
            .assertIsNotEnabled()

        viewModel.onAction(action = DiscoverAction.SelectGenre(genre = TestGenres.first()))
        viewModel.onAction(action = DiscoverAction.EnterYear(year = "2022"))

        onNodeWithTag(testTag = DiscoverDefaults.DiscoverButtonTestTag)
            .assertExists(errorMessageOnFail = "No Button back component found.")
            .assertIsDisplayed()
            .assertIsEnabled()
            .performClick()

        viewModel.effect.test {
            assertEquals(
                expected = DiscoverEvent.NavigateToResults(
                    year = 2022,
                    genres = listOf(TestGenres.first().id),
                ),
                actual = awaitItem(),
            )
        }
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
        viewModel: DiscoverViewModel = DiscoverViewModel(loadGenres = loadGenres),
    ) = setContent {
        MoviesPotTheme {
            val state by viewModel.state.collectAsState()
            Discover(
                onAction = viewModel::onAction,
                state = state,
                snackbarDuration = SnackbarDuration.Indefinite
            )
        }
    }

}
