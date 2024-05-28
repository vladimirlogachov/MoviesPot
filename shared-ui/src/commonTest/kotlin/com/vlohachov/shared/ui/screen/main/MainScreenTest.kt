package com.vlohachov.shared.ui.screen.main

import androidx.compose.material3.SnackbarDuration
import androidx.compose.ui.test.ComposeUiTest
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.runComposeUiTest
import com.vlohachov.shared.TestPaginatedData
import com.vlohachov.shared.domain.model.movie.Movie
import com.vlohachov.shared.domain.model.movie.MovieCategory
import com.vlohachov.shared.domain.repository.MovieRepository
import com.vlohachov.shared.domain.usecase.movie.LoadMoviesByCategory
import com.vlohachov.shared.ui.component.PosterDefaults
import com.vlohachov.shared.ui.component.bar.ErrorBarDefaults
import com.vlohachov.shared.ui.component.movie.MoviesLazyRowDefaults
import com.vlohachov.shared.ui.component.movie.MoviesSectionDefaults
import com.vlohachov.shared.ui.theme.MoviesPotTheme
import dev.mokkery.answering.returns
import dev.mokkery.every
import dev.mokkery.matcher.any
import dev.mokkery.mock
import dev.mokkery.verify
import dev.mokkery.verify.VerifyMode.Companion.atMost
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import moviespot.shared_ui.generated.resources.Res
import moviespot.shared_ui.generated.resources.app_name
import org.jetbrains.compose.resources.getString
import kotlin.js.JsName
import kotlin.test.Test

@OptIn(ExperimentalTestApi::class)
class MainScreenTest {

    private val repository = mock<MovieRepository> {
        every {
            getMoviesByCategory(category = any(), page = any(), language = any(), region = any())
        } returns emptyFlow()
    }

    private val loadMoviesByCategory = LoadMoviesByCategory(repository = repository)

    @Test
    @JsName(name = "check_app_bar_title")
    fun `check app bar title`() = runComposeUiTest {
        testContent()
        onNodeWithText(text = runBlocking { getString(resource = Res.string.app_name) })
            .assertExists(errorMessageOnFail = "No Title component found.")
            .assertIsDisplayed()
    }

    @Test
    @JsName(name = "check_search_button")
    fun `check search button`() = runComposeUiTest {
        val onSearch = mock<() -> Unit> {
            every { invoke() } returns Unit
        }
        testContent(onSearch = onSearch)
        onNodeWithTag(testTag = MainScreenDefaults.SearchButtonTestTag)
            .assertExists(errorMessageOnFail = "No Search button component found.")
            .assertIsDisplayed()
            .assertHasClickAction()
            .performClick()
        verify(mode = atMost(n = 1)) { onSearch() }
    }

    @Test
    @JsName(name = "check_settings_button")
    fun `check settings button`() = runComposeUiTest {
        val onSettings = mock<() -> Unit> {
            every { invoke() } returns Unit
        }
        testContent(onSettings = onSettings)
        onNodeWithTag(testTag = MainScreenDefaults.SettingsButtonTestTag)
            .assertExists(errorMessageOnFail = "No Settings button component found.")
            .assertIsDisplayed()
            .assertHasClickAction()
            .performClick()
        verify(mode = atMost(n = 1)) { onSettings() }
    }

    @Test
    @JsName(name = "check_discover_button")
    fun `check discover button`() = runComposeUiTest {
        val onDiscover = mock<() -> Unit> {
            every { invoke() } returns Unit
        }
        testContent(onDiscover = onDiscover)
        onNodeWithTag(testTag = MainScreenDefaults.DiscoverButtonTestTag)
            .assertExists(errorMessageOnFail = "No Discover button component found.")
            .assertIsDisplayed()
            .assertHasClickAction()
            .performClick()
        verify(mode = atMost(n = 1)) { onDiscover() }
    }

    @Test
    @JsName(name = "check_loading_state")
    fun `check loading state`() = runComposeUiTest {
        testContent()
        onAllNodesWithTag(testTag = MoviesSectionDefaults.ProgressTestTag)
            .assertCountEquals(expectedSize = 4)
    }

    @Test
    @JsName(name = "check_success_state")
    fun `check success state`() = runComposeUiTest {
        every {
            repository.getMoviesByCategory(
                category = any(),
                page = any(),
                language = any(),
                region = any()
            )
        } returns flowOf(value = TestPaginatedData)
        testContent()
        onAllNodesWithTag(testTag = MoviesLazyRowDefaults.MoviesLazyRowTestTag)
            .assertCountEquals(expectedSize = 4)
        onAllNodesWithTag(testTag = MoviesSectionDefaults.MoreButtonTestTag)
            .assertCountEquals(expectedSize = 4)
    }

    @Test
    @JsName(name = "check_error_state")
    fun `check error state`() = runComposeUiTest {
        every {
            repository.getMoviesByCategory(
                category = any(),
                page = any(),
                language = any(),
                region = any()
            )
        } returns flow { error(message = "Error") }
        testContent()
        onAllNodesWithTag(testTag = MoviesSectionDefaults.ErrorTestTag)
            .assertCountEquals(expectedSize = 4)
        onNodeWithTag(testTag = ErrorBarDefaults.ErrorTestTag)
            .assertExists(errorMessageOnFail = "No Error component found")
            .assertIsDisplayed()
    }

    @Test
    @JsName(name = "check_more_button")
    fun `check more button`() = runComposeUiTest {
        val onMore = mock<(category: MovieCategory) -> Unit> {
            every { invoke(any()) } returns Unit
        }
        every {
            repository.getMoviesByCategory(
                category = any(),
                page = any(),
                language = any(),
                region = any()
            )
        } returns flowOf(value = TestPaginatedData)
        testContent(onMore = onMore)
        onAllNodesWithTag(testTag = MoviesSectionDefaults.MoreButtonTestTag)
            .onFirst()
            .assertIsDisplayed()
            .assertHasClickAction()
            .performClick()
        verify(mode = atMost(n = 1)) { onMore(any()) }
    }

    @Test
    @JsName(name = "check_movie_details")
    fun `check movie details`() = runComposeUiTest {
        val onMovieDetails = mock<(movie: Movie) -> Unit> {
            every { invoke(any()) } returns Unit
        }
        every {
            repository.getMoviesByCategory(
                category = any(),
                page = any(),
                language = any(),
                region = any()
            )
        } returns flowOf(value = TestPaginatedData)
        testContent(onMovieDetails = onMovieDetails)
        onAllNodesWithTag(testTag = PosterDefaults.PosterTestTag, useUnmergedTree = true)
            .onFirst()
            .assertHasClickAction()
            .performClick()
        verify(mode = atMost(n = 1)) { onMovieDetails(any()) }
    }

    private fun ComposeUiTest.testContent(
        onSearch: () -> Unit = {},
        onSettings: () -> Unit = {},
        onMovieDetails: (movie: Movie) -> Unit = {},
        onMore: (category: MovieCategory) -> Unit = {},
        onDiscover: () -> Unit = {},
    ) = setContent {
        MoviesPotTheme {
            Main(
                onSearch = onSearch,
                onSettings = onSettings,
                onMovieDetails = onMovieDetails,
                onMore = onMore,
                onDiscover = onDiscover,
                viewModel = MainViewModel(loadMoviesByCategory = loadMoviesByCategory),
                snackbarDuration = SnackbarDuration.Indefinite,
            )
        }
    }

}
