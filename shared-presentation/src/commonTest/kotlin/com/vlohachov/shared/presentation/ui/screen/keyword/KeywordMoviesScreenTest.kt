package com.vlohachov.shared.presentation.ui.screen.keyword

import androidx.compose.material3.SnackbarDuration
import androidx.compose.ui.test.ComposeUiTest
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.onChildren
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollToIndex
import androidx.compose.ui.test.runComposeUiTest
import com.vlohachov.shared.domain.model.movie.Movie
import com.vlohachov.shared.domain.repository.DiscoverRepository
import com.vlohachov.shared.domain.usecase.DiscoverMovies
import com.vlohachov.shared.presentation.TestMovies
import com.vlohachov.shared.presentation.TestPaginatedData
import com.vlohachov.shared.presentation.testMovie
import com.vlohachov.shared.presentation.ui.component.bar.AppBarDefaults
import com.vlohachov.shared.presentation.ui.component.bar.ErrorBarDefaults
import com.vlohachov.shared.presentation.ui.component.button.ScrollToTopDefaults
import com.vlohachov.shared.presentation.ui.component.movie.MoviesPaginatedGridDefaults
import com.vlohachov.shared.presentation.ui.theme.MoviesPotTheme
import dev.mokkery.answering.returns
import dev.mokkery.every
import dev.mokkery.matcher.any
import dev.mokkery.mock
import dev.mokkery.verify
import dev.mokkery.verify.VerifyMode
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlin.js.JsName
import kotlin.test.Test

@OptIn(ExperimentalTestApi::class)
class KeywordMoviesScreenTest {

    private val repository = mock<DiscoverRepository> {
        every {
            discoverMovies(
                page = any(),
                year = any(),
                genres = any(),
                keywords = any(),
                language = any()
            )
        } returns emptyFlow()
    }

    private val pager = KeywordMoviesPager(
        keywordId = 1,
        useCase = DiscoverMovies(repository = repository)
    )

    @Test
    @JsName(name = "check_app_bar_title")
    fun `check app bar title`() = runComposeUiTest {
        testContent()
        onNodeWithText(text = "keyword")
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
    @JsName(name = "check_movies_loading")
    fun `check movies loading`() = runComposeUiTest {
        every {
            repository.discoverMovies(
                page = any(),
                year = any(),
                genres = any(),
                keywords = any(),
                language = any()
            )
        } returns flow { delay(timeMillis = 200) }
        testContent()
        onNodeWithTag(testTag = ErrorBarDefaults.ErrorTestTag)
            .assertExists(errorMessageOnFail = "No Error component found.")
            .assertIsNotDisplayed()
        onNodeWithTag(testTag = MoviesPaginatedGridDefaults.MoviesPaginatedGridTestTag)
            .assertExists(errorMessageOnFail = "No Content component found.")
            .assertIsDisplayed()
        onNodeWithTag(testTag = MoviesPaginatedGridDefaults.LoadingTestTag)
            .assertExists(errorMessageOnFail = "No Progress component found.")
            .assertIsDisplayed()
    }

    @Test
    @JsName(name = "check_movies_loading_success")
    fun `check movies loading success`() = runComposeUiTest {
        every {
            repository.discoverMovies(
                page = any(),
                year = any(),
                genres = any(),
                keywords = any(),
                language = any()
            )
        } returns flowOf(value = TestPaginatedData)
        testContent()
        onNodeWithTag(testTag = ErrorBarDefaults.ErrorTestTag)
            .assertExists(errorMessageOnFail = "No Error component found.")
            .assertIsNotDisplayed()
        onNodeWithTag(testTag = MoviesPaginatedGridDefaults.LoadingTestTag)
            .assertDoesNotExist()
        onNodeWithTag(testTag = MoviesPaginatedGridDefaults.MoviesPaginatedGridTestTag)
            .assertExists(errorMessageOnFail = "No Content component found.")
            .assertIsDisplayed()
            .onChildren()
            .assertCountEquals(expectedSize = TestMovies.size)
    }

    @Test
    @JsName(name = "check_movies_loading_error")
    fun `check movies loading error`() = runComposeUiTest {
        every {
            repository.discoverMovies(
                page = any(),
                year = any(),
                genres = any(),
                keywords = any(),
                language = any()
            )
        } returns flow { error(message = "Error") }
        testContent()
        waitForIdle()
        onNodeWithTag(testTag = ErrorBarDefaults.ErrorTestTag)
            .assertExists(errorMessageOnFail = "No Error component found.")
            .assertIsDisplayed()
        onNodeWithTag(testTag = MoviesPaginatedGridDefaults.LoadingTestTag)
            .assertDoesNotExist()
    }

    @Test
    @JsName(name = "check_on_movie_details")
    fun `check on movie details`() = runComposeUiTest {
        val onMovieDetails = mock<(movie: Movie) -> Unit> {
            every { invoke(any()) } returns Unit
        }
        every {
            repository.discoverMovies(
                page = any(),
                year = any(),
                genres = any(),
                keywords = any(),
                language = any()
            )
        } returns flowOf(value = TestPaginatedData)
        testContent(onMovieDetails = onMovieDetails)
        onNodeWithTag(testTag = ErrorBarDefaults.ErrorTestTag)
            .assertExists(errorMessageOnFail = "No Error component found.")
            .assertIsNotDisplayed()
        onNodeWithTag(testTag = MoviesPaginatedGridDefaults.MoviesPaginatedGridTestTag)
            .assertExists(errorMessageOnFail = "No Content component found.")
            .assertIsDisplayed()
            .onChildren()
            .assertCountEquals(expectedSize = TestMovies.size)
            .onFirst()
            .assertHasClickAction()
            .performClick()
        verify(mode = VerifyMode.atMost(n = 1)) { onMovieDetails(any()) }
    }

    @Test
    @JsName(name = "check_scroll_to_top")
    fun `check scroll to top`() = runComposeUiTest {
        val largeMovies = buildList {
            repeat(times = 20) { id -> add(testMovie(id = id.toLong())) }
        }
        every {
            repository.discoverMovies(
                page = any(),
                year = any(),
                genres = any(),
                keywords = any(),
                language = any()
            )
        } returns flowOf(value = TestPaginatedData.copy(data = largeMovies))
        testContent()
        onNodeWithTag(testTag = ErrorBarDefaults.ErrorTestTag)
            .assertExists(errorMessageOnFail = "No Error component found.")
            .assertIsNotDisplayed()
        onNodeWithTag(testTag = MoviesPaginatedGridDefaults.MoviesPaginatedGridTestTag)
            .assertExists(errorMessageOnFail = "No Content component found.")
            .assertIsDisplayed()
            .performScrollToIndex(index = largeMovies.lastIndex)
        onNodeWithTag(testTag = MoviesPaginatedGridDefaults.LoadingTestTag)
            .assertDoesNotExist()
        onNodeWithTag(testTag = ScrollToTopDefaults.ScrollToTopTestTag)
            .assertExists(errorMessageOnFail = "No ScrollToTop component found.")
            .assertIsDisplayed()
            .performClick()
        onNodeWithTag(testTag = ScrollToTopDefaults.ScrollToTopTestTag)
            .assertDoesNotExist()
    }

    private fun ComposeUiTest.testContent(
        onBack: () -> Unit = {},
        onMovieDetails: (movie: Movie) -> Unit = {},
    ) = setContent {
        MoviesPotTheme {
            KeywordMovies(
                keyword = "keyword",
                keywordId = 1,
                onBack = onBack,
                onMovieDetails = onMovieDetails,
                viewModel = KeywordMoviesViewModel(pager = pager),
                snackbarDuration = SnackbarDuration.Indefinite,
            )
        }
    }

}
