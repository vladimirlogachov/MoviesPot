package com.vlohachov.shared.presentation.ui.screen.discover

import app.cash.turbine.test
import com.vlohachov.shared.domain.repository.GenreRepository
import com.vlohachov.shared.domain.usecase.LoadGenres
import com.vlohachov.shared.presentation.TestGenre
import com.vlohachov.shared.presentation.TestGenres
import com.vlohachov.shared.presentation.runViewModelTest
import dev.mokkery.answering.returns
import dev.mokkery.every
import dev.mokkery.matcher.any
import dev.mokkery.mock
import dev.mokkery.resetAnswers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlin.js.JsName
import kotlin.test.Test
import kotlin.test.assertEquals

class DiscoverViewModelTest {

    private val repository = mock<GenreRepository> {
        every {
            getGenres(language = any())
        } returns flow {
            delay(timeMillis = 100) // simulate network delay
            emit(value = TestGenres)
        }
    }

    private val loadGenres = LoadGenres(repository = repository)

    @Test
    @JsName(name = "genres_loading_success")
    fun `genres loading success`() = runViewModelTest {
        val viewModel = DiscoverViewModel(loadGenres = loadGenres)

        viewModel.state.test {
            assertEquals(expected = DiscoverViewState(), actual = awaitItem())
            assertEquals(
                expected = DiscoverViewState(showProgress = true),
                actual = awaitItem(),
            )
            assertEquals(
                expected = DiscoverViewState(genres = TestGenres),
                actual = awaitItem(),
            )
        }
    }

    @Test
    @JsName(name = "genres_loading_error")
    fun `genres loading error`() = runViewModelTest {
        resetAnswers(repository)
        val exception = IllegalStateException("Error")
        every {
            repository.getGenres(language = any())
        } returns flow {
            delay(timeMillis = 100) // simulate network delay
            throw exception
        }

        val viewModel = DiscoverViewModel(loadGenres = loadGenres)

        viewModel.state.test {
            assertEquals(expected = DiscoverViewState(), actual = awaitItem())
            assertEquals(
                expected = DiscoverViewState(showProgress = true),
                actual = awaitItem(),
            )

            val errorState = awaitItem()
            assertEquals(
                expected = DiscoverViewState(error = errorState.error),
                actual = errorState,
            )
            assertEquals(
                expected = exception.message,
                actual = errorState.error?.message,
            )
        }
    }

    @Test
    @JsName(name = "select_and_clear_genre")
    fun `select and clear genre`() = runViewModelTest {
        val viewModel = DiscoverViewModel(loadGenres = loadGenres)

        viewModel.state.test {
            assertEquals(expected = DiscoverViewState(), actual = awaitItem())
            assertEquals(
                expected = DiscoverViewState(showProgress = true),
                actual = awaitItem(),
            )
            assertEquals(
                expected = DiscoverViewState(genres = TestGenres),
                actual = awaitItem(),
            )

            viewModel.onAction(action = DiscoverAction.SelectGenre(genre = TestGenre))
            assertEquals(
                expected = DiscoverViewState(
                    genres = TestGenres,
                    selectedGenres = listOf(TestGenre),
                ),
                actual = awaitItem(),
            )

            viewModel.onAction(action = DiscoverAction.RemoveGenre(genre = TestGenre))
            assertEquals(
                expected = DiscoverViewState(
                    genres = TestGenres,
                    selectedGenres = emptyList(),
                ),
                actual = awaitItem(),
            )
        }
    }

    @Test
    @JsName(name = "year_changed")
    fun `year changed`() = runViewModelTest {
        val viewModel = DiscoverViewModel(loadGenres = loadGenres)

        viewModel.state.test {
            assertEquals(expected = DiscoverViewState(), actual = awaitItem())
            assertEquals(
                expected = DiscoverViewState(showProgress = true),
                actual = awaitItem(),
            )
            assertEquals(
                expected = DiscoverViewState(genres = TestGenres),
                actual = awaitItem(),
            )

            viewModel.onAction(action = DiscoverAction.EnterYear(year = "2022"))
            assertEquals(
                expected = DiscoverViewState(
                    genres = TestGenres,
                    year = "2022",
                ),
                actual = awaitItem(),
            )
        }
    }

    @Test
    @JsName(name = "year_trimmed")
    fun `year trimmed`() = runViewModelTest {
        val viewModel = DiscoverViewModel(loadGenres = loadGenres)

        viewModel.state.test {
            assertEquals(expected = DiscoverViewState(), actual = awaitItem())
            assertEquals(
                expected = DiscoverViewState(showProgress = true),
                actual = awaitItem(),
            )
            assertEquals(
                expected = DiscoverViewState(genres = TestGenres),
                actual = awaitItem(),
            )

            viewModel.onAction(action = DiscoverAction.EnterYear(year = "202220"))
            assertEquals(
                expected = DiscoverViewState(
                    genres = TestGenres,
                    year = "2022",
                ),
                actual = awaitItem(),
            )
        }
    }

    @Test
    @JsName(name = "error_set_and_consumed")
    fun `error set and consumed`() = runViewModelTest {
        resetAnswers(repository)
        val exception = IllegalStateException("Error")
        every {
            repository.getGenres(language = any())
        } returns flow {
            delay(timeMillis = 100) // simulate network delay
            throw exception
        }

        val viewModel = DiscoverViewModel(loadGenres = loadGenres)

        viewModel.state.test {
            assertEquals(expected = DiscoverViewState(), actual = awaitItem())
            assertEquals(
                expected = DiscoverViewState(showProgress = true),
                actual = awaitItem(),
            )

            val errorState = awaitItem()
            assertEquals(
                expected = DiscoverViewState(error = errorState.error),
                actual = errorState,
            )
            assertEquals(
                expected = exception.message,
                actual = errorState.error?.message,
            )

            viewModel.onAction(action = DiscoverAction.HideError)
            assertEquals(
                expected = DiscoverViewState(error = null),
                actual = awaitItem(),
            )
        }
    }

}
