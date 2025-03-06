package com.vlohachov.shared.presentation.ui.screen.search

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SnackbarDuration
import androidx.compose.ui.test.ComposeUiTest
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.hasImeAction
import androidx.compose.ui.test.onChildren
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollToIndex
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.runComposeUiTest
import androidx.compose.ui.text.input.ImeAction
import com.vlohachov.shared.domain.model.movie.Movie
import com.vlohachov.shared.domain.repository.SearchRepository
import com.vlohachov.shared.domain.usecase.SearchMovies
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
import kotlinx.coroutines.runBlocking
import moviespot.shared_presentation.generated.resources.Res
import moviespot.shared_presentation.generated.resources.search
import org.jetbrains.compose.resources.getString
import kotlin.js.JsName
import kotlin.test.Test

@OptIn(ExperimentalTestApi::class)
class MoviesSearchScreenTest {

    private val repository = mock<SearchRepository> {
        every {
            searchMovies(query = any(), page = any(), language = any())
        } returns emptyFlow()
    }

    private val searchMovies = SearchMovies(repository = repository)

    @Test
    @JsName(name = "check_search_bar")
    fun `check search bar`() = runComposeUiTest {
        val onBack = mock<() -> Unit> {
            every { invoke() } returns Unit
        }
        testContent(onBack = onBack)
        onNodeWithTag(testTag = SearchMoviesDefaults.ClearSearchBarTestTag)
            .assertDoesNotExist()
        onNode(matcher = hasImeAction(actionType = ImeAction.Search))
            .assertExists(errorMessageOnFail = "No Search input component found.")
            .assertIsDisplayed()
            .performTextInput(text = "search")
        onNodeWithTag(testTag = SearchMoviesDefaults.ClearSearchBarTestTag)
            .assertExists(errorMessageOnFail = "No Search input component found.")
            .assertIsDisplayed()
            .assertHasClickAction()
            .performClick()
            .assertDoesNotExist()
        onNode(matcher = hasImeAction(actionType = ImeAction.Search))
            .assertExists(errorMessageOnFail = "No Search input component found.")
            .assertIsDisplayed()
            .assertTextEquals(
                runBlocking { getString(resource = Res.string.search) },
                includeEditableText = false,
            )
        onNodeWithTag(testTag = AppBarDefaults.BackButtonTestTag)
            .assertExists(errorMessageOnFail = "No Back button component found.")
            .assertIsDisplayed()
            .assertHasClickAction()
            .performClick()
        verify(mode = VerifyMode.atMost(n = 1)) { onBack() }
    }

    @Test
    @JsName(name = "check_results_loading")
    fun `check results loading`() = runComposeUiTest {
        every {
            repository.searchMovies(query = any(), page = any(), language = any())
        } returns flow { delay(timeMillis = 200) }
        testContent()
        onNodeWithTag(testTag = ErrorBarDefaults.ErrorTestTag)
            .assertExists(errorMessageOnFail = "No Error component found.")
            .assertIsNotDisplayed()
        onNode(matcher = hasImeAction(actionType = ImeAction.Search))
            .assertExists(errorMessageOnFail = "No Search input component found.")
            .assertIsDisplayed()
            .performTextInput(text = "search")
        onNodeWithTag(testTag = MoviesPaginatedGridDefaults.LoadingTestTag)
            .assertExists(errorMessageOnFail = "No Progress component found.")
            .assertIsDisplayed()
    }

    @Test
    @JsName(name = "check_results_loading_success")
    fun `check results loading success`() = runComposeUiTest {
        every {
            repository.searchMovies(query = any(), page = any(), language = any())
        } returns flowOf(value = TestPaginatedData)
        testContent()
        onNodeWithTag(testTag = ErrorBarDefaults.ErrorTestTag)
            .assertExists(errorMessageOnFail = "No Error component found.")
            .assertIsNotDisplayed()
        onNode(matcher = hasImeAction(actionType = ImeAction.Search))
            .assertExists(errorMessageOnFail = "No Search input component found.")
            .assertIsDisplayed()
            .performTextInput(text = "search")
        onNodeWithTag(testTag = MoviesPaginatedGridDefaults.LoadingTestTag)
            .assertDoesNotExist()
        onNodeWithTag(testTag = MoviesPaginatedGridDefaults.MoviesPaginatedGridTestTag)
            .assertExists(errorMessageOnFail = "No Content component found.")
            .assertIsDisplayed()
            .onChildren()
            .assertCountEquals(expectedSize = TestMovies.size)
    }

    @Test
    @JsName(name = "check_results_loading_error")
    fun `check results loading error`() = runComposeUiTest {
        every {
            repository.searchMovies(query = any(), page = any(), language = any())
        } returns flow { error(message = "Error") }
        testContent()
        onNode(matcher = hasImeAction(actionType = ImeAction.Search))
            .assertExists(errorMessageOnFail = "No Search input component found.")
            .assertIsDisplayed()
            .performTextInput(text = "search")
        onNodeWithTag(testTag = MoviesPaginatedGridDefaults.LoadingTestTag)
            .assertDoesNotExist()
        onNodeWithTag(testTag = ErrorBarDefaults.ErrorTestTag)
            .assertExists(errorMessageOnFail = "No Error component found.")
            .assertIsDisplayed()
    }

    @Test
    @JsName(name = "check_on_movie_details")
    fun `check on movie details`() = runComposeUiTest {
        val onMovieDetails = mock<(movie: Movie) -> Unit> {
            every { invoke(any()) } returns Unit
        }
        every {
            repository.searchMovies(query = any(), page = any(), language = any())
        } returns flowOf(value = TestPaginatedData)
        testContent(onMovieDetails = onMovieDetails)
        onNodeWithTag(testTag = ErrorBarDefaults.ErrorTestTag)
            .assertExists(errorMessageOnFail = "No Error component found.")
            .assertIsNotDisplayed()
        onNode(matcher = hasImeAction(actionType = ImeAction.Search))
            .assertExists(errorMessageOnFail = "No Search input component found.")
            .assertIsDisplayed()
            .performTextInput(text = "search")
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
            repository.searchMovies(query = any(), page = any(), language = any())
        } returns flowOf(value = TestPaginatedData.copy(data = largeMovies))
        testContent()
        onNodeWithTag(testTag = ErrorBarDefaults.ErrorTestTag)
            .assertExists(errorMessageOnFail = "No Error component found.")
            .assertIsNotDisplayed()
        onNode(matcher = hasImeAction(actionType = ImeAction.Search))
            .assertExists(errorMessageOnFail = "No Search input component found.")
            .assertIsDisplayed()
            .performTextInput(text = "search")
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

    @OptIn(ExperimentalMaterial3Api::class)
    private fun ComposeUiTest.testContent(
        onBack: () -> Unit = {},
        onMovieDetails: (movie: Movie) -> Unit = {},
    ) = setContent {
        MoviesPotTheme {
            MoviesSearch(
                onBack = onBack,
                onMovieDetails = onMovieDetails,
                viewModel = MoviesSearchViewModel(
                    pager = MoviesSearchPager(useCase = searchMovies, debounce = 0)
                ),
                snackbarDuration = SnackbarDuration.Indefinite,
            )
        }
    }

}
