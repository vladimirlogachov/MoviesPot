package com.vlohachov.shared.ui.screen.details

import androidx.compose.material3.SnackbarDuration
import androidx.compose.ui.test.ComposeUiTest
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onChildren
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onLast
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.test.runComposeUiTest
import com.vlohachov.shared.TestDirector
import com.vlohachov.shared.TestKeywords
import com.vlohachov.shared.TestMovieCredits
import com.vlohachov.shared.TestMovieDetails
import com.vlohachov.shared.TestPaginatedData
import com.vlohachov.shared.domain.model.movie.Movie
import com.vlohachov.shared.domain.model.movie.keyword.Keyword
import com.vlohachov.shared.domain.repository.MovieRepository
import com.vlohachov.shared.domain.usecase.credits.LoadDirector
import com.vlohachov.shared.domain.usecase.movie.LoadDetails
import com.vlohachov.shared.domain.usecase.movie.LoadKeywords
import com.vlohachov.shared.domain.usecase.movie.LoadRecommendations
import com.vlohachov.shared.ui.component.PosterDefaults
import com.vlohachov.shared.ui.component.bar.AppBarDefaults
import com.vlohachov.shared.ui.component.bar.ErrorBarDefaults
import com.vlohachov.shared.ui.component.movie.MoviesLazyRowDefaults
import com.vlohachov.shared.ui.component.movie.MoviesSectionDefaults
import com.vlohachov.shared.ui.component.section.SectionDefaults
import com.vlohachov.shared.ui.theme.MoviesPotTheme
import dev.mokkery.answering.returns
import dev.mokkery.every
import dev.mokkery.matcher.any
import dev.mokkery.mock
import dev.mokkery.verify
import dev.mokkery.verify.VerifyMode
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import moviespot.shared_ui.generated.resources.Res
import moviespot.shared_ui.generated.resources.directed_by
import moviespot.shared_ui.generated.resources.no_results
import org.jetbrains.compose.resources.getString
import kotlin.js.JsName
import kotlin.test.Test

@OptIn(ExperimentalTestApi::class)
class MovieDetailsScreenTest {

    private val repository = mock<MovieRepository> {
        every {
            getMovieDetails(id = any(), language = any())
        } returns emptyFlow()
        every {
            getMovieCredits(id = any(), language = any())
        } returns emptyFlow()
        every {
            getMovieKeywords(id = any())
        } returns emptyFlow()
        every {
            getMovieRecommendations(id = any(), page = any(), language = any())
        } returns emptyFlow()
    }

    private val loadDetails = LoadDetails(repository = repository)
    private val loadDirector = LoadDirector(repository = repository)
    private val loadKeywords = LoadKeywords(repository = repository)
    private val loadRecommendations = LoadRecommendations(repository = repository)

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
    @JsName(name = "check_details_loading")
    fun `check details loading`() = runComposeUiTest {
        testContent()
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
    @JsName(name = "check_details_loading_success")
    fun `check details loading success`() = runComposeUiTest {
        every {
            repository.getMovieDetails(id = any(), language = any())
        } returns flowOf(value = TestMovieDetails)
        testContent()
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
            .performScrollTo()
            .assertIsDisplayed()
    }

    @Test
    @JsName(name = "check_details_loading_error")
    fun `check details loading error`() = runComposeUiTest {
        every {
            repository.getMovieDetails(id = any(), language = any())
        } returns flow { error(message = "Error") }
        testContent()
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
        onNodeWithTag(testTag = ErrorBarDefaults.ErrorTestTag)
            .assertExists(errorMessageOnFail = "No Error component found.")
            .assertIsDisplayed()
    }

    @Test
    @JsName(name = "check_poster")
    fun `check poster`() = runComposeUiTest {
        val onFullscreenImage = mock<(path: String) -> Unit> {
            every { invoke(any()) } returns Unit
        }
        every {
            repository.getMovieDetails(id = any(), language = any())
        } returns flowOf(value = TestMovieDetails)
        testContent(onFullscreenImage = onFullscreenImage)
        onNodeWithTag(testTag = PosterDefaults.PosterTestTag)
            .assertExists(errorMessageOnFail = "No Poster component found.")
            .assertIsDisplayed()
            .assertHasClickAction()
            .performClick()
        verify(mode = VerifyMode.atMost(n = 1)) { onFullscreenImage(any()) }
    }

    @Test
    @JsName(name = "check_cast_button")
    fun `check cast button`() = runComposeUiTest {
        val onCast = mock<() -> Unit> {
            every { invoke() } returns Unit
        }
        every {
            repository.getMovieDetails(id = any(), language = any())
        } returns flowOf(value = TestMovieDetails)
        testContent(onCast = onCast)
        onNodeWithTag(testTag = CreditsDefaults.CastButtonTestTag)
            .assertExists(errorMessageOnFail = "No Cast component found.")
            .assertIsDisplayed()
            .assertHasClickAction()
            .performClick()
        verify(mode = VerifyMode.atMost(n = 1)) { onCast() }
    }

    @Test
    @JsName(name = "check_crew_button")
    fun `check crew button`() = runComposeUiTest {
        val onCrew = mock<() -> Unit> {
            every { invoke() } returns Unit
        }
        every {
            repository.getMovieDetails(id = any(), language = any())
        } returns flowOf(value = TestMovieDetails)
        testContent(onCrew = onCrew)
        onNodeWithTag(testTag = CreditsDefaults.CrewButtonTestTag)
            .assertExists(errorMessageOnFail = "No Cast component found.")
            .assertIsDisplayed()
            .assertHasClickAction()
            .performClick()
        verify(mode = VerifyMode.atMost(n = 1)) { onCrew() }
    }

    @Test
    @JsName(name = "check_empty_director")
    fun `check empty director`() = runComposeUiTest {
        every {
            repository.getMovieDetails(id = any(), language = any())
        } returns flowOf(value = TestMovieDetails)
        every {
            repository.getMovieCredits(id = any(), language = any())
        } returns flowOf(value = TestMovieCredits)
        testContent()
        onNodeWithText(text = runBlocking { getString(Res.string.directed_by, "") })
            .assertDoesNotExist()
    }

    @Test
    @JsName(name = "check_non_empty_director")
    fun `check non empty director`() = runComposeUiTest {
        every {
            repository.getMovieDetails(id = any(), language = any())
        } returns flowOf(value = TestMovieDetails)
        every {
            repository.getMovieCredits(id = any(), language = any())
        } returns flowOf(value = TestMovieCredits.copy(crew = listOf(element = TestDirector)))
        testContent()
        onNodeWithText(text = runBlocking { getString(Res.string.directed_by, TestDirector.name) })
            .assertExists(errorMessageOnFail = "No Director component found")
            .assertIsDisplayed()
    }

    @Test
    @JsName(name = "check_recommendations_loading")
    fun `check recommendations loading`() = runComposeUiTest {
        testContent()
        onNodeWithTag(testTag = MoviesSectionDefaults.MoviesSectionTestTag)
            .assertExists(errorMessageOnFail = "No Recommendations component found.")
            .assertIsDisplayed()
        onNodeWithTag(testTag = MoviesSectionDefaults.ProgressTestTag)
            .assertExists(errorMessageOnFail = "No Loading component found.")
            .assertIsDisplayed()
    }

    @Test
    @JsName(name = "check_empty_recommendations")
    fun `check empty recommendations`() = runComposeUiTest {
        every {
            repository.getMovieRecommendations(id = any(), page = any(), language = any())
        } returns flowOf(value = TestPaginatedData.copy(data = emptyList()))
        testContent()
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
    @JsName(name = "check_non_empty_recommendations")
    fun `check non empty recommendations`() = runComposeUiTest {
        val onSimilar = mock<() -> Unit> {
            every { invoke() } returns Unit
        }
        val onMovieDetails = mock<(movie: Movie) -> Unit> {
            every { invoke(any()) } returns Unit
        }
        every {
            repository.getMovieRecommendations(id = any(), page = any(), language = any())
        } returns flowOf(value = TestPaginatedData)
        testContent(onSimilar = onSimilar, onMovieDetails = onMovieDetails)
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
        onNodeWithTag(testTag = MoviesLazyRowDefaults.MoviesLazyRowTestTag)
            .assertExists(errorMessageOnFail = "No Movies component found.")
            .assertIsDisplayed()
            .onChildren()
            .onFirst()
            .assertHasClickAction()
            .performClick()
        verify(mode = VerifyMode.atMost(n = 1)) {
            onSimilar()
            onMovieDetails(any())
        }
    }

    @Test
    @JsName(name = "check_keywords_loading")
    fun `check keywords loading`() = runComposeUiTest {
        testContent()
        onNodeWithTag(testTag = MovieDetailsDefaults.KeywordsTestTag)
            .assertDoesNotExist()
    }

    @Test
    @JsName(name = "check_empty_keywords")
    fun `check empty keywords`() = runComposeUiTest {
        every {
            repository.getMovieKeywords(id = any())
        } returns flowOf(value = emptyList())
        testContent()
        onNodeWithTag(testTag = MovieDetailsDefaults.KeywordsTestTag)
            .assertExists(errorMessageOnFail = "No Keywords component found.")
            .assertIsDisplayed()
        onNodeWithText(text = runBlocking { getString(resource = Res.string.no_results) })
            .assertExists(errorMessageOnFail = "No Empty text component found.")
            .assertIsDisplayed()
    }

    @Test
    @JsName(name = "check_non_empty_keywords")
    fun `check non empty keywords`() = runComposeUiTest {
        val onKeywordMovies = mock<(keyword: Keyword) -> Unit> {
            every { invoke(any()) } returns Unit
        }
        every {
            repository.getMovieKeywords(id = any())
        } returns flowOf(value = TestKeywords)
        testContent(onKeywordMovies = onKeywordMovies)
        onNodeWithTag(testTag = MovieDetailsDefaults.KeywordsTestTag)
            .assertExists(errorMessageOnFail = "No Keywords component found.")
            .assertIsDisplayed()
        onNodeWithText(text = runBlocking { getString(resource = Res.string.no_results) })
            .assertDoesNotExist()
        onAllNodesWithTag(testTag = SectionDefaults.SectionContentTestTag)
            .onLast()
            .onChildren()
            .assertCountEquals(expectedSize = TestKeywords.size)
            .onFirst()
            .assertHasClickAction()
            .performClick()
        verify(mode = VerifyMode.atMost(n = 1)) { onKeywordMovies(any()) }
    }

    private fun ComposeUiTest.testContent(
        onBack: () -> Unit = {},
        onFullscreenImage: (path: String) -> Unit = {},
        onCast: () -> Unit = {},
        onCrew: () -> Unit = {},
        onSimilar: () -> Unit = {},
        onMovieDetails: (movie: Movie) -> Unit = {},
        onKeywordMovies: (keyword: Keyword) -> Unit = {},
    ) = setContent {
        MoviesPotTheme {
            MovieDetails(
                movieId = 1,
                onBack = onBack,
                onFullscreenImage = onFullscreenImage,
                onCast = onCast,
                onCrew = onCrew,
                onSimilar = onSimilar,
                onMovieDetails = onMovieDetails,
                onKeywordMovies = onKeywordMovies,
                viewModel = MovieDetailsViewModel(
                    movieId = 1,
                    loadDetails = loadDetails,
                    loadDirector = loadDirector,
                    loadKeywords = loadKeywords,
                    loadRecommendations = loadRecommendations
                ),
                snackbarDuration = SnackbarDuration.Indefinite,
            )
        }
    }

}
