package com.vlohachov.moviespot.ui.details

import android.content.Context
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onChildren
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onLast
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.core.app.ApplicationProvider
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.vlohachov.moviespot.R
import com.vlohachov.moviespot.data.TestKeywords
import com.vlohachov.moviespot.data.TestMovieDetails
import com.vlohachov.moviespot.data.TestMovies
import com.vlohachov.shared.core.ViewState
import com.vlohachov.shared.ui.component.PosterDefaults
import com.vlohachov.shared.ui.component.bar.AppBarDefaults
import com.vlohachov.shared.ui.component.movie.MoviesLazyRowDefaults
import com.vlohachov.shared.ui.component.movie.MoviesSectionDefaults
import com.vlohachov.shared.ui.component.section.SectionDefaults
import com.vlohachov.shared.ui.theme.MoviesPotTheme
import io.mockk.every
import io.mockk.justRun
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Rule
import org.junit.Test

class MovieDetailsTest {

    @get:Rule
    val composeRule = createComposeRule()

    private val navigator = mockk<DestinationsNavigator>()
    private val viewModel = mockk<MovieDetailsViewModel>()

    private val context = ApplicationProvider.getApplicationContext<Context>()

    @Test
    fun navigateUpTest(): Unit = with(composeRule) {
        every { viewModel.uiState } returns MutableStateFlow(value = MovieDetailsViewState())
        every { navigator.navigateUp() } returns true

        setContent {
            MoviesPotTheme {
                MovieDetails(
                    navigator = navigator,
                    movieId = 0,
                    movieTitle = "Movie",
                    viewModel = viewModel,
                )
            }
        }

        onNodeWithTag(testTag = AppBarDefaults.BackButtonTestTag)
            .assertExists(errorMessageOnFail = "No Back button component found.")
            .assertIsDisplayed()
            .assertHasClickAction()
            .performClick()

        verify(exactly = 1) { navigator.navigateUp() }
    }

    @Test
    fun errorTest(): Unit = with(composeRule) {
        every { viewModel.uiState } returns MutableStateFlow(
            value = MovieDetailsViewState(error = Exception())
        )
        justRun { viewModel.onErrorConsumed() }

        setContent {
            MoviesPotTheme {
                MovieDetails(
                    navigator = navigator,
                    movieId = 0,
                    movieTitle = "Movie",
                    viewModel = viewModel,
                )
            }
        }

        onNodeWithTag(testTag = MovieDetailsDefaults.ErrorBarTestTag)
            .assertExists(errorMessageOnFail = "No Error component found.")
            .assertIsDisplayed()

        mainClock.advanceTimeBy(milliseconds = 4_000)

        verify(exactly = 1) { viewModel.onErrorConsumed() }
    }

    @Test
    fun detailsLoadingTest(): Unit = with(composeRule) {
        every { viewModel.uiState } returns MutableStateFlow(value = MovieDetailsViewState())

        setContent {
            MoviesPotTheme {
                MovieDetails(
                    navigator = navigator,
                    movieId = 0,
                    movieTitle = "Movie",
                    viewModel = viewModel,
                )
            }
        }

        onNodeWithTag(testTag = MovieDetailsDefaults.DetailsLoadingTestTag)
            .assertExists(errorMessageOnFail = "No Loading component found.")
            .assertIsDisplayed()
        onNodeWithTag(testTag = HeadlineDefaults.TestTag)
            .assertDoesNotExist()
        onNodeWithTag(testTag = MovieDetailsDefaults.TaglineTestTag)
            .assertDoesNotExist()
        onNodeWithTag(testTag = ShortSummaryDefaults.TestTag)
            .assertDoesNotExist()
        onNodeWithTag(testTag = CreditsDefaults.CastButtonTestTag)
            .assertDoesNotExist()
        onNodeWithTag(testTag = CreditsDefaults.CrewButtonTestTag)
            .assertDoesNotExist()
        onNodeWithTag(testTag = OverviewDefaults.TestTag)
            .assertDoesNotExist()
        onNodeWithTag(testTag = ProductionDefaults.TestTag)
            .assertDoesNotExist()
    }

    @Test
    fun detailsErrorTest(): Unit = with(composeRule) {
        val error = Exception()

        every { viewModel.uiState } returns MutableStateFlow(
            value = MovieDetailsViewState(detailsViewState = ViewState.Error(error = error))
        )
        justRun { viewModel.onError(error = any()) }

        setContent {
            MoviesPotTheme {
                MovieDetails(
                    navigator = navigator,
                    movieId = 0,
                    movieTitle = "Movie",
                    viewModel = viewModel,
                )
            }
        }

        onNodeWithTag(testTag = MovieDetailsDefaults.DetailsLoadingTestTag)
            .assertDoesNotExist()
        onNodeWithTag(testTag = HeadlineDefaults.TestTag)
            .assertDoesNotExist()
        onNodeWithTag(testTag = MovieDetailsDefaults.TaglineTestTag)
            .assertDoesNotExist()
        onNodeWithTag(testTag = ShortSummaryDefaults.TestTag)
            .assertDoesNotExist()
        onNodeWithTag(testTag = CreditsDefaults.CastButtonTestTag)
            .assertDoesNotExist()
        onNodeWithTag(testTag = CreditsDefaults.CrewButtonTestTag)
            .assertDoesNotExist()
        onNodeWithTag(testTag = OverviewDefaults.TestTag)
            .assertDoesNotExist()
        onNodeWithTag(testTag = ProductionDefaults.TestTag)
            .assertDoesNotExist()

        verify(exactly = 1) { viewModel.onError(error = error) }
    }

    @Test
    fun detailsLoadedTest(): Unit = with(composeRule) {
        every { viewModel.uiState } returns MutableStateFlow(
            value = MovieDetailsViewState(detailsViewState = ViewState.Success(data = TestMovieDetails))
        )

        setContent {
            MoviesPotTheme {
                MovieDetails(
                    navigator = navigator,
                    movieId = 0,
                    movieTitle = "Movie",
                    viewModel = viewModel,
                )
            }
        }

        onNodeWithTag(testTag = MovieDetailsDefaults.DetailsLoadingTestTag)
            .assertDoesNotExist()
        onNodeWithTag(testTag = HeadlineDefaults.TestTag)
            .assertExists(errorMessageOnFail = "No Headline component found.")
            .assertIsDisplayed()
        onNodeWithTag(testTag = MovieDetailsDefaults.TaglineTestTag)
            .assertExists(errorMessageOnFail = "No Tagline component found.")
            .assertIsDisplayed()
        onNodeWithTag(testTag = ShortSummaryDefaults.TestTag)
            .assertExists(errorMessageOnFail = "No BriefInfo component found.")
            .assertIsDisplayed()
        onNodeWithTag(testTag = CreditsDefaults.CastButtonTestTag)
            .assertExists(errorMessageOnFail = "No Cast button component found.")
            .assertIsDisplayed()
        onNodeWithTag(testTag = CreditsDefaults.CrewButtonTestTag)
            .assertExists(errorMessageOnFail = "No Crew button component found.")
            .assertIsDisplayed()
        onNodeWithTag(testTag = OverviewDefaults.TestTag)
            .assertExists(errorMessageOnFail = "No Overview component found.")
            .assertIsDisplayed()
        onNodeWithTag(testTag = ProductionDefaults.TestTag)
            .assertExists(errorMessageOnFail = "No Production component found.")
            .assertIsDisplayed()
    }

    @Test
    fun posterTest(): Unit = with(composeRule) {
        every { viewModel.uiState } returns MutableStateFlow(
            value = MovieDetailsViewState(detailsViewState = ViewState.Success(data = TestMovieDetails))
        )
        justRun { navigator.navigate(direction = any()) }

        setContent {
            MoviesPotTheme {
                MovieDetails(
                    navigator = navigator,
                    movieId = 0,
                    movieTitle = "Movie",
                    viewModel = viewModel,
                )
            }
        }

        onNodeWithTag(testTag = PosterDefaults.PosterTestTag)
            .assertExists(errorMessageOnFail = "No Poster component found.")
            .assertIsDisplayed()
            .assertHasClickAction()
            .performClick()

        verify(exactly = 1) { navigator.navigate(direction = any()) }
    }

    @Test
    fun castButtonTest(): Unit = with(composeRule) {
        every { viewModel.uiState } returns MutableStateFlow(
            value = MovieDetailsViewState(detailsViewState = ViewState.Success(data = TestMovieDetails))
        )
        justRun { navigator.navigate(direction = any()) }

        setContent {
            MoviesPotTheme {
                MovieDetails(
                    navigator = navigator,
                    movieId = 0,
                    movieTitle = "Movie",
                    viewModel = viewModel,
                )
            }
        }

        onNodeWithTag(testTag = CreditsDefaults.CastButtonTestTag)
            .assertExists(errorMessageOnFail = "No Cast component found.")
            .assertIsDisplayed()
            .assertHasClickAction()
            .performClick()

        verify(exactly = 1) { navigator.navigate(direction = any()) }
    }

    @Test
    fun crewButtonTest(): Unit = with(composeRule) {
        every { viewModel.uiState } returns MutableStateFlow(
            value = MovieDetailsViewState(detailsViewState = ViewState.Success(data = TestMovieDetails))
        )
        justRun { navigator.navigate(direction = any()) }

        setContent {
            MoviesPotTheme {
                MovieDetails(
                    navigator = navigator,
                    movieId = 0,
                    movieTitle = "Movie",
                    viewModel = viewModel,
                )
            }
        }

        onNodeWithTag(testTag = CreditsDefaults.CrewButtonTestTag)
            .assertExists(errorMessageOnFail = "No Crew component found.")
            .assertIsDisplayed()
            .assertHasClickAction()
            .performClick()

        verify(exactly = 1) { navigator.navigate(direction = any()) }
    }

    @Test
    fun emptyDirectorTest(): Unit = with(composeRule) {
        every { viewModel.uiState } returns MutableStateFlow(
            value = MovieDetailsViewState(
                detailsViewState = ViewState.Success(data = TestMovieDetails),
                directorViewState = ViewState.Success(data = "")
            )
        )

        setContent {
            MoviesPotTheme {
                MovieDetails(
                    navigator = navigator,
                    movieId = 0,
                    movieTitle = "Movie",
                    viewModel = viewModel,
                )
            }
        }

        onNodeWithText(text = context.getString(R.string.directed_by, ""))
            .assertDoesNotExist()
    }

    @Test
    fun nonEmptyDirectorTest(): Unit = with(composeRule) {
        val director = "Director"

        every { viewModel.uiState } returns MutableStateFlow(
            value = MovieDetailsViewState(
                detailsViewState = ViewState.Success(data = TestMovieDetails),
                directorViewState = ViewState.Success(data = director)
            )
        )

        setContent {
            MoviesPotTheme {
                MovieDetails(
                    navigator = navigator,
                    movieId = 0,
                    movieTitle = "Movie",
                    viewModel = viewModel,
                )
            }
        }

        onNodeWithText(text = context.getString(R.string.directed_by, director))
            .assertExists(errorMessageOnFail = "No Director component found.")
            .assertIsDisplayed()
    }

    @Test
    fun recommendationsLoadingTest(): Unit = with(composeRule) {
        every { viewModel.uiState } returns MutableStateFlow(value = MovieDetailsViewState())

        setContent {
            MoviesPotTheme {
                MovieDetails(
                    navigator = navigator,
                    movieId = 0,
                    movieTitle = "Movie",
                    viewModel = viewModel,
                )
            }
        }

        onNodeWithTag(testTag = MoviesSectionDefaults.MoviesSectionTestTag)
            .assertExists(errorMessageOnFail = "No Recommendations component found.")
            .assertIsDisplayed()
        onNodeWithTag(testTag = MoviesSectionDefaults.ProgressTestTag)
            .assertExists(errorMessageOnFail = "No Loading component found.")
            .assertIsDisplayed()
    }

    @Test
    fun emptyRecommendationsTest(): Unit = with(composeRule) {
        every { viewModel.uiState } returns MutableStateFlow(
            value = MovieDetailsViewState(recommendationsViewState = ViewState.Success(data = listOf()))
        )

        setContent {
            MoviesPotTheme {
                MovieDetails(
                    navigator = navigator,
                    movieId = 0,
                    movieTitle = "Movie",
                    viewModel = viewModel,
                )
            }
        }

        onNodeWithTag(testTag = MoviesSectionDefaults.MoviesSectionTestTag)
            .assertExists(errorMessageOnFail = "No Recommendations component found.")
            .assertIsDisplayed()
        onNodeWithTag(testTag = MoviesSectionDefaults.ProgressTestTag)
            .assertDoesNotExist()
        onNodeWithTag(testTag = MoviesSectionDefaults.EmptyTestTag)
            .assertExists(errorMessageOnFail = "No Empty component found.")
            .assertIsDisplayed()
    }

    @Test
    fun nonEmptyRecommendationsTest(): Unit = with(composeRule) {
        every { viewModel.uiState } returns MutableStateFlow(
            value = MovieDetailsViewState(recommendationsViewState = ViewState.Success(data = TestMovies))
        )
        justRun { navigator.navigate(direction = any()) }

        setContent {
            MoviesPotTheme {
                MovieDetails(
                    navigator = navigator,
                    movieId = 0,
                    movieTitle = "Movie",
                    viewModel = viewModel,
                )
            }
        }

        onNodeWithTag(testTag = MoviesSectionDefaults.MoviesSectionTestTag)
            .assertExists(errorMessageOnFail = "No Recommendations component found.")
            .assertIsDisplayed()
        onNodeWithTag(testTag = MoviesSectionDefaults.ProgressTestTag)
            .assertDoesNotExist()
        onNodeWithTag(testTag = MoviesSectionDefaults.EmptyTestTag)
            .assertDoesNotExist()
        onNodeWithTag(testTag = MoviesSectionDefaults.MoreButtonTestTag)
            .assertExists(errorMessageOnFail = "No More movies component found.")
            .assertIsDisplayed()
            .assertHasClickAction()
            .performClick()
        onNodeWithTag(testTag = MoviesLazyRowDefaults.MoviesLazyRowTestTag, useUnmergedTree = true)
            .assertExists(errorMessageOnFail = "No Movies component found.")
            .assertIsDisplayed()
            .onChildren()
            .onFirst()
            .assertHasClickAction()
            .performClick()

        verify(exactly = 2) { navigator.navigate(direction = any()) }
    }

    @Test
    fun keywordsLoadingTest(): Unit = with(composeRule) {
        every { viewModel.uiState } returns MutableStateFlow(value = MovieDetailsViewState())

        setContent {
            MoviesPotTheme {
                MovieDetails(
                    navigator = navigator,
                    movieId = 0,
                    movieTitle = "Movie",
                    viewModel = viewModel,
                )
            }
        }

        onNodeWithTag(testTag = MovieDetailsDefaults.KeywordsTestTag)
            .assertDoesNotExist()
    }

    @Test
    fun emptyKeywordsTest(): Unit = with(composeRule) {
        every { viewModel.uiState } returns MutableStateFlow(
            value = MovieDetailsViewState(keywordsViewState = ViewState.Success(data = listOf()))
        )

        setContent {
            MoviesPotTheme {
                MovieDetails(
                    navigator = navigator,
                    movieId = 0,
                    movieTitle = "Movie",
                    viewModel = viewModel,
                )
            }
        }

        onNodeWithTag(testTag = MovieDetailsDefaults.KeywordsTestTag)
            .assertExists(errorMessageOnFail = "No Keywords component found.")
            .assertIsDisplayed()
        onNodeWithText(text = context.getString(R.string.no_results))
            .assertExists(errorMessageOnFail = "No Empty text component found.")
            .assertIsDisplayed()
    }

    @Test
    fun keywordsTest(): Unit = with(composeRule) {
        every { viewModel.uiState } returns MutableStateFlow(
            value = MovieDetailsViewState(keywordsViewState = ViewState.Success(data = TestKeywords))
        )

        setContent {
            MoviesPotTheme {
                MovieDetails(
                    navigator = navigator,
                    movieId = 0,
                    movieTitle = "Movie",
                    viewModel = viewModel,
                )
            }
        }

        onNodeWithTag(testTag = MovieDetailsDefaults.KeywordsTestTag)
            .assertExists(errorMessageOnFail = "No Keywords component found.")
            .assertIsDisplayed()
        onNodeWithText(text = context.getString(R.string.no_results))
            .assertDoesNotExist()
        onAllNodesWithTag(testTag = SectionDefaults.SectionContentTestTag)
            .onLast()
            .onChildren()
            .assertCountEquals(expectedSize = TestKeywords.size)
    }

    @Test
    fun keywordClickTest(): Unit = with(composeRule) {
        every { viewModel.uiState } returns MutableStateFlow(
            value = MovieDetailsViewState(keywordsViewState = ViewState.Success(data = TestKeywords))
        )
        justRun { navigator.navigate(direction = any()) }

        setContent {
            MoviesPotTheme {
                MovieDetails(
                    navigator = navigator,
                    movieId = 0,
                    movieTitle = "Movie",
                    viewModel = viewModel,
                )
            }
        }

        onNodeWithTag(testTag = MovieDetailsDefaults.KeywordsTestTag)
            .assertExists(errorMessageOnFail = "No Keywords component found.")
            .assertIsDisplayed()
        onNodeWithText(text = context.getString(R.string.no_results))
            .assertDoesNotExist()
        onAllNodesWithTag(testTag = SectionDefaults.SectionContentTestTag)
            .onLast()
            .onChildren()
            .onFirst()
            .assertHasClickAction()
            .performClick()

        verify(exactly = 1) { navigator.navigate(direction = any()) }
    }

    @Test
    fun headlinePreviewTest(): Unit = with(composeRule) {
        setContent {
            HeadlinePreview()
        }

        onNodeWithTag(testTag = HeadlineDefaults.TestTag)
            .assertExists(errorMessageOnFail = "No Headline component found.")
            .assertIsDisplayed()
    }

    @Test
    fun shortSummaryPreviewTest(): Unit = with(composeRule) {
        setContent {
            ShortSummaryPreview()
        }

        onNodeWithTag(testTag = ShortSummaryDefaults.TestTag)
            .assertExists(errorMessageOnFail = "No Headline component found.")
            .assertIsDisplayed()
    }

    @Test
    fun overviewPreviewTest(): Unit = with(composeRule) {
        setContent {
            OverviewPreview()
        }

        onNodeWithTag(testTag = OverviewDefaults.TestTag)
            .assertExists(errorMessageOnFail = "No Headline component found.")
            .assertIsDisplayed()
    }
}
