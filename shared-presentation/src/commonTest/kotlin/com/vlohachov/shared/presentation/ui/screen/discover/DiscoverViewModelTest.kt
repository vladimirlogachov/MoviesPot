package com.vlohachov.shared.presentation.ui.screen.discover

import app.cash.turbine.test
import com.vlohachov.shared.domain.model.genre.Genre
import com.vlohachov.shared.domain.repository.GenreRepository
import com.vlohachov.shared.domain.usecase.LoadGenres
import com.vlohachov.shared.presentation.TestGenre
import com.vlohachov.shared.presentation.TestGenres
import com.vlohachov.shared.presentation.core.ViewState
import dev.mokkery.answering.returns
import dev.mokkery.every
import dev.mokkery.matcher.any
import dev.mokkery.mock
import dev.mokkery.resetAnswers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import kotlin.js.JsName
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNull
import kotlin.test.assertTrue

class DiscoverViewModelTest {

    private val repository = mock<GenreRepository> {
        every {
            getGenres(language = any())
        } returns flowOf(value = TestGenres)
    }

    private val loadGenres = LoadGenres(repository = repository)

    private val viewModel = DiscoverViewModel(loadGenres = loadGenres)

    @Test
    @JsName(name = "genres_loading_success")
    fun `genres loading success`() = runTest {
        viewModel.uiState.test {
            skipItems(count = 1)
            with(receiver = awaitItem()) {
                assertIs<ViewState.Success<List<Genre>>>(value = genresViewState)
                assertEquals(expected = TestGenres.size, actual = genresViewState.data.size)
            }
        }
    }

    @Test
    @JsName(name = "genres_loading_error")
    fun `genres loading error`() = runTest {
        resetAnswers(repository)
        every {
            repository.getGenres(language = any())
        } returns flow { error(message = "Error") }

        DiscoverViewModel(loadGenres = loadGenres).uiState.test {
            skipItems(count = 1)
            assertIs<ViewState.Error>(value = awaitItem().genresViewState)
        }
    }

    @Test
    @JsName(name = "select_and_clear_genre")
    fun `select and clear genre`() = runTest {
        viewModel.uiState.test {
            skipItems(count = 2)
            viewModel.onSelect(genre = TestGenre)
            assertContains(iterable = awaitItem().selectedGenres, element = TestGenre)
            viewModel.onClearSelection(genre = TestGenre)
            assertTrue {
                awaitItem().selectedGenres.isEmpty()
            }
        }
    }

    @Test
    @JsName(name = "year_changed")
    fun `year changed`() = runTest {
        viewModel.uiState.test {
            skipItems(count = 2)
            viewModel.onYear(year = "2022")
            assertEquals(expected = "2022", actual = awaitItem().year)
        }
    }

    @Test
    @JsName(name = "year_trimmed")
    fun `year trimmed`() = runTest {
        viewModel.uiState.test {
            skipItems(count = 2)
            viewModel.onYear(year = "202220")
            assertEquals(expected = "2022", actual = awaitItem().year)
        }
    }

    @Test
    @JsName(name = "error_set_and_consumed")
    fun `error set and consumed`() = runTest {
        viewModel.uiState.test {
            skipItems(count = 2)
            with(receiver = IllegalArgumentException()) {
                viewModel.onError(error = this)
                assertEquals(expected = this, actual = awaitItem().error)
                viewModel.onErrorConsumed()
                assertNull(actual = awaitItem().error)
            }
        }
    }

}
