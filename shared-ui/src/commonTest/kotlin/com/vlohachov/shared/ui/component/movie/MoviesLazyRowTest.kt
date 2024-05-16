package com.vlohachov.shared.ui.component.movie

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertAll
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.onChildren
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.runComposeUiTest
import androidx.compose.ui.unit.dp
import com.vlohachov.shared.domain.model.movie.Movie
import com.vlohachov.shared.ui.component.PosterDefaults
import com.vlohachov.shared.TestMovies
import com.vlohachov.shared.ui.theme.MoviesPotTheme
import kotlin.js.JsName
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalTestApi::class)
class MoviesLazyRowTest {

    @Test
    @JsName(name = "empty_movies_list")
    fun `empty movies list`() = runComposeUiTest {
        setContent {
            MoviesPotTheme {
                MoviesLazyRow(
                    modifier = Modifier
                        .height(height = 120.dp)
                        .fillMaxWidth(),
                    movies = listOf(),
                )
            }
        }

        onNodeWithTag(testTag = MoviesLazyRowDefaults.MoviesLazyRowTestTag, useUnmergedTree = true)
            .assertExists(errorMessageOnFail = "No MoviesLazyRow component found.")
            .assertIsDisplayed()
            .onChildren()
            .assertCountEquals(expectedSize = 0)
    }

    @Test
    @JsName(name = "not_empty_movies_list")
    fun `not empty movies list`() = runComposeUiTest {
        setContent {
            MoviesPotTheme {
                MoviesLazyRow(
                    modifier = Modifier
                        .height(height = 120.dp)
                        .fillMaxWidth(),
                    movies = TestMovies,
                )
            }
        }

        onNodeWithTag(testTag = MoviesLazyRowDefaults.MoviesLazyRowTestTag, useUnmergedTree = true)
            .assertExists(errorMessageOnFail = "No MoviesLazyRow component found.")
            .assertIsDisplayed()
            .onChildren()
            .assertAll(hasTestTag(testTag = PosterDefaults.PosterTestTag))
    }

    @Test
    @JsName(name = "click_on_movie")
    fun `click on movie`() = runComposeUiTest {
        var movie by mutableStateOf<Movie?>(value = null)

        setContent {
            MoviesPotTheme {
                MoviesLazyRow(
                    modifier = Modifier
                        .height(height = 120.dp)
                        .fillMaxWidth(),
                    movies = TestMovies,
                    onClick = { m -> movie = m },
                )
            }
        }

        onNodeWithTag(testTag = MoviesLazyRowDefaults.MoviesLazyRowTestTag, useUnmergedTree = true)
            .assertExists(errorMessageOnFail = "No MoviesLazyRow component found.")
            .assertIsDisplayed()
            .onChildren()
            .assertAll(hasTestTag(testTag = PosterDefaults.PosterTestTag) and hasClickAction())
            .onFirst()
            .performClick()

        assertEquals(expected = TestMovies.first(), actual = movie)
    }

}
