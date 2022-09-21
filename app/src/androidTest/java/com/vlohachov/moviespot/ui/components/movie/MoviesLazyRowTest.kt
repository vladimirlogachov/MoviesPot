package com.vlohachov.moviespot.ui.components.movie

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.unit.dp
import com.google.common.truth.Truth
import com.vlohachov.domain.model.movie.Movie
import com.vlohachov.moviespot.data.TestMovies
import com.vlohachov.moviespot.ui.components.PosterDefaults
import org.junit.Rule
import org.junit.Test

class MoviesLazyRowTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun nonClickableMoviesLazyRowTest(): Unit = with(composeRule) {
        setContent {
            MoviesLazyRow(
                modifier = Modifier
                    .height(height = 120.dp)
                    .fillMaxWidth(),
                movies = TestMovies,
            )
        }

        val childMatcher = SemanticsMatcher(description = "child_matcher") { semanticsNode ->
            semanticsNode.config[SemanticsProperties.TestTag] == PosterDefaults.PosterTestTag
        }

        onNodeWithTag(testTag = MoviesLazyRowDefaults.MoviesLazyRowTestTag, useUnmergedTree = true)
            .assertExists(errorMessageOnFail = "No MoviesLazyRow component found.")
            .assertIsDisplayed()
            .onChildren()
            .assertCountEquals(expectedSize = TestMovies.size)
            .assertAll(matcher = childMatcher)

    }

    @Test
    fun clickableMoviesLazyRowTest(): Unit = with(composeRule) {
        var movie by mutableStateOf<Movie?>(value = null)

        setContent {

            MoviesLazyRow(
                modifier = Modifier
                    .height(height = 120.dp)
                    .fillMaxWidth(),
                movies = TestMovies,
                onClick = { m -> movie = m },
            )
        }

        val childMatcher = SemanticsMatcher(description = "child_matcher") { semanticsNode ->
            semanticsNode.config[SemanticsProperties.TestTag] == PosterDefaults.PosterTestTag &&
                    semanticsNode.config[SemanticsProperties.Role] == Role.Button
        }

        onNodeWithTag(testTag = MoviesLazyRowDefaults.MoviesLazyRowTestTag, useUnmergedTree = true)
            .assertExists(errorMessageOnFail = "No MoviesLazyRow component found.")
            .assertIsDisplayed()
            .onChildren()
            .assertCountEquals(expectedSize = TestMovies.size)
            .assertAll(matcher = childMatcher)
            .onFirst()
            .performClick()

        Truth.assertThat(movie).isEqualTo(TestMovies.first())
    }
}