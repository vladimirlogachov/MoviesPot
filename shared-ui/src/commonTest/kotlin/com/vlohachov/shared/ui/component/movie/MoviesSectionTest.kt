package com.vlohachov.shared.ui.component.movie

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.onChildren
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.runComposeUiTest
import com.vlohachov.shared.domain.model.movie.Movie
import com.vlohachov.shared.ui.component.section.SectionTitleDefaults
import com.vlohachov.shared.ui.data.TestMovies
import com.vlohachov.shared.ui.state.ViewState
import com.vlohachov.shared.ui.theme.MoviesPotTheme
import kotlin.js.JsName
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@OptIn(ExperimentalTestApi::class)
class MoviesSectionTest {

    @Test
    @JsName("loading_state")
    fun `loading state`() = runComposeUiTest {
        setContent {
            MoviesPotTheme {
                MoviesSection(
                    modifier = Modifier.fillMaxWidth(),
                    title = "Title",
                    viewState = ViewState.Loading,
                    onMore = {},
                )
            }
        }

        onNodeWithTag(testTag = MoviesSectionDefaults.MoviesSectionTestTag, useUnmergedTree = true)
            .assertExists(errorMessageOnFail = "No MoviesSection component found.")
            .assertIsDisplayed()
        onNodeWithTag(testTag = SectionTitleDefaults.TitleTestTag, useUnmergedTree = true)
            .assertExists(errorMessageOnFail = "No child SectionTitle component found.")
            .assertIsDisplayed()
        onNodeWithTag(testTag = MoviesSectionDefaults.MoreButtonTestTag, useUnmergedTree = true)
            .assertDoesNotExist()
        onNodeWithTag(testTag = MoviesSectionDefaults.ContentTestTag, useUnmergedTree = true)
            .assertExists(errorMessageOnFail = "No child \"content\" component found.")
            .assertIsDisplayed()
        onNodeWithTag(testTag = MoviesSectionDefaults.ProgressTestTag, useUnmergedTree = true)
            .assertExists(errorMessageOnFail = "No child Progress component found.")
            .assertIsDisplayed()
    }

    @Test
    @JsName("empty_state")
    fun `empty state`() = runComposeUiTest {
        setContent {
            MoviesPotTheme {
                MoviesSection(
                    modifier = Modifier.fillMaxWidth(),
                    title = "Title",
                    viewState = ViewState.Success(data = listOf()),
                    onMore = {},
                )
            }
        }

        onNodeWithTag(testTag = MoviesSectionDefaults.MoviesSectionTestTag, useUnmergedTree = true)
            .assertExists(errorMessageOnFail = "No MoviesSection component found.")
            .assertIsDisplayed()
        onNodeWithTag(testTag = SectionTitleDefaults.TitleTestTag, useUnmergedTree = true)
            .assertExists(errorMessageOnFail = "No child SectionTitle component found.")
            .assertIsDisplayed()
        onNodeWithTag(testTag = MoviesSectionDefaults.MoreButtonTestTag, useUnmergedTree = true)
            .assertDoesNotExist()
        onNodeWithTag(testTag = MoviesSectionDefaults.ContentTestTag, useUnmergedTree = true)
            .assertExists(errorMessageOnFail = "No child \"content\" component found.")
            .assertIsDisplayed()
        onNodeWithTag(testTag = MoviesSectionDefaults.EmptyTestTag, useUnmergedTree = true)
            .assertExists(errorMessageOnFail = "No child Text empty component found.")
            .assertIsDisplayed()
            .assertTextEquals("No results.")
    }

    @Test
    @JsName("not_empty_state")
    fun `not empty state`() = runComposeUiTest {
        setContent {
            MoviesPotTheme {
                MoviesSection(
                    modifier = Modifier.fillMaxWidth(),
                    title = "Title",
                    viewState = ViewState.Success(data = TestMovies),
                    onMore = {},
                )
            }
        }

        onNodeWithTag(testTag = MoviesSectionDefaults.MoviesSectionTestTag, useUnmergedTree = true)
            .assertExists(errorMessageOnFail = "No MoviesSection component found.")
            .assertIsDisplayed()
        onNodeWithTag(testTag = SectionTitleDefaults.TitleTestTag, useUnmergedTree = true)
            .assertExists(errorMessageOnFail = "No child SectionTitle component found.")
            .assertIsDisplayed()
        onNodeWithTag(testTag = MoviesSectionDefaults.MoreButtonTestTag, useUnmergedTree = true)
            .assertExists(errorMessageOnFail = "No child \"MoreButton\" component found.")
            .assertIsDisplayed()
        onNodeWithTag(testTag = MoviesSectionDefaults.ContentTestTag, useUnmergedTree = true)
            .assertExists(errorMessageOnFail = "No child \"content\" component found.")
            .assertIsDisplayed()
        onNodeWithTag(testTag = MoviesLazyRowDefaults.MoviesLazyRowTestTag, useUnmergedTree = true)
            .assertExists(errorMessageOnFail = "No child MoviesLazyRow component found.")
            .assertIsDisplayed()
    }

    @Test
    @JsName("error_state")
    fun `error state`() = runComposeUiTest {
        val errorText = "Loading failed"

        setContent {
            MoviesPotTheme {
                MoviesSection(
                    modifier = Modifier.fillMaxWidth(),
                    title = "Title",
                    viewState = ViewState.Error(error = Exception(errorText)),
                    onMore = {},
                )
            }
        }

        onNodeWithTag(testTag = MoviesSectionDefaults.MoviesSectionTestTag, useUnmergedTree = true)
            .assertExists(errorMessageOnFail = "No MoviesSection component found.")
            .assertIsDisplayed()
        onNodeWithTag(testTag = SectionTitleDefaults.TitleTestTag, useUnmergedTree = true)
            .assertExists(errorMessageOnFail = "No child SectionTitle component found.")
            .assertIsDisplayed()
        onNodeWithTag(testTag = MoviesSectionDefaults.MoreButtonTestTag, useUnmergedTree = true)
            .assertDoesNotExist()
        onNodeWithTag(testTag = MoviesSectionDefaults.ContentTestTag, useUnmergedTree = true)
            .assertExists(errorMessageOnFail = "No child \"content\" component found.")
            .assertIsDisplayed()
        onNodeWithTag(testTag = MoviesSectionDefaults.ErrorTestTag, useUnmergedTree = true)
            .assertExists(errorMessageOnFail = "No child Text error component found.")
            .assertIsDisplayed()
            .assertTextEquals(errorText)
    }

    @Test
    @JsName("more_button_click")
    fun `more click`() = runComposeUiTest {
        var clicked by mutableStateOf(value = false)

        setContent {
            MoviesPotTheme {
                MoviesSection(
                    modifier = Modifier.fillMaxWidth(),
                    title = "Title",
                    viewState = ViewState.Success(data = TestMovies),
                    onMore = { clicked = true },
                )
            }
        }

        onNodeWithTag(testTag = MoviesSectionDefaults.MoreButtonTestTag, useUnmergedTree = true)
            .assertExists(errorMessageOnFail = "No child \"MoreButton\" component found.")
            .assertIsDisplayed()
            .assertHasClickAction()
            .performClick()

        assertTrue(actual = clicked)
    }

    @Test
    @JsName("movie_click")
    fun `movie click`() = runComposeUiTest {
        var movie by mutableStateOf<Movie?>(value = null)

        setContent {
            MoviesPotTheme {
                MoviesSection(
                    modifier = Modifier.fillMaxWidth(),
                    title = "Title",
                    viewState = ViewState.Success(data = TestMovies),
                    onMore = {},
                    onMovieClick = { m -> movie = m },
                )
            }
        }

        onNodeWithTag(testTag = MoviesLazyRowDefaults.MoviesLazyRowTestTag, useUnmergedTree = true)
            .assertExists(errorMessageOnFail = "No child \"MoviesLazyRow\" component found.")
            .assertIsDisplayed()
            .onChildren()
            .onFirst()
            .performClick()

        assertEquals(expected = TestMovies.first(), actual = movie)
    }

}
