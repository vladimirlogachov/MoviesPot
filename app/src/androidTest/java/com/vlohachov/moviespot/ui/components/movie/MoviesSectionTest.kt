package com.vlohachov.moviespot.ui.components.movie

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.assertAny
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onChild
import androidx.compose.ui.test.onChildren
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onLast
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import com.google.common.truth.Truth
import com.vlohachov.domain.model.movie.Movie
import com.vlohachov.moviespot.core.ViewState
import com.vlohachov.moviespot.data.TestMovies
import com.vlohachov.moviespot.ui.components.section.SectionTitleDefaults
import com.vlohachov.shared.ui.theme.MoviesPotTheme
import org.junit.Rule
import org.junit.Test

class MoviesSectionTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun loadingTest(): Unit = with(composeRule) {
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
    fun emptyTest(): Unit = with(composeRule) {
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
    fun nonEmptyTest(): Unit = with(composeRule) {
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
    fun errorTest(): Unit = with(composeRule) {
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
    fun moreButtonClickTest(): Unit = with(composeRule) {
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

        Truth.assertThat(clicked).isTrue()
    }

    @Test
    fun movieClickTest(): Unit = with(composeRule) {
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

        Truth.assertThat(movie).isEqualTo(TestMovies.first())
    }

    @Test
    fun previewTest(): Unit = with(composeRule) {
        setContent {
            MoviesSectionPreview()
        }

        val sections = onAllNodes(hasTestTag(testTag = MoviesSectionDefaults.MoviesSectionTestTag))
            .assertCountEquals(expectedSize = 4)

        sections[0]
            .onChildren()
            .onLast()
            .onChild()
            .onChildren()
            .assertAny(hasTestTag(testTag = MoviesSectionDefaults.ProgressTestTag))
        sections[1]
            .onChildren()
            .onLast()
            .onChild()
            .onChildren()
            .assertAny(hasTestTag(testTag = MoviesSectionDefaults.ErrorTestTag))
        sections[2]
            .onChildren()
            .onLast()
            .onChild()
            .onChildren()
            .assertAny(hasTestTag(testTag = MoviesLazyRowDefaults.MoviesLazyRowTestTag))
        sections[3]
            .onChildren()
            .onFirst()
            .onChild()
            .onChildren()
            .onLast()
            .onChildren()
            .assertAny(hasTestTag(testTag = MoviesSectionDefaults.MoreButtonTestTag))
    }
}
