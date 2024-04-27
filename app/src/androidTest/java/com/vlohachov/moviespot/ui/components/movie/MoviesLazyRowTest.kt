package com.vlohachov.moviespot.ui.components.movie

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.assertAll
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onChildren
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.unit.dp
import com.google.common.truth.Truth
import com.vlohachov.domain.model.movie.Movie
import com.vlohachov.moviespot.data.TestMovies
import com.vlohachov.moviespot.ui.components.PosterDefaults
import com.vlohachov.shared.theme.MoviesPotTheme
import org.junit.Rule
import org.junit.Test

class MoviesLazyRowTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun emptyTest(): Unit = with(composeRule) {
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
    fun nonEmptyTest(): Unit = with(composeRule) {
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
    fun clickableTest(): Unit = with(composeRule) {
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

        Truth.assertThat(movie).isEqualTo(TestMovies.first())
    }

    @Test
    fun previewTest(): Unit = with(composeRule) {
        setContent {
            MoviesLazyRowPreview()
        }

        onAllNodes(hasTestTag(testTag = MoviesLazyRowDefaults.MoviesLazyRowTestTag))
            .assertCountEquals(expectedSize = 1)
    }
}
