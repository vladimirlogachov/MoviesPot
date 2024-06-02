package com.vlohachov.shared.presentation.ui.screen.search

import androidx.paging.testing.asSnapshot
import app.cash.turbine.test
import com.vlohachov.shared.domain.repository.SearchRepository
import com.vlohachov.shared.domain.usecase.SearchMovies
import com.vlohachov.shared.presentation.TestPaginatedData
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
import kotlin.test.assertFails
import kotlin.test.assertIs
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue
import kotlin.test.expect

class MoviesSearchViewModelTest {

    private val repository = mock<SearchRepository> {
        every {
            searchMovies(query = any(), page = any(), language = any())
        } returns flowOf(value = TestPaginatedData)
    }

    private val pager = MoviesSearchPager(useCase = SearchMovies(repository = repository))
    private val viewModel = MoviesSearchViewModel(pager = pager)

    @Test
    @JsName(name = "empty_query_leads_to_empty_result")
    fun `empty query leads to empty result`() = runTest {
        viewModel.search.test {
            assertTrue { awaitItem().isBlank() }
            viewModel.movies.test {
                expect(expected = 0) {
                    flowOf(awaitItem()).asSnapshot().size
                }
            }
        }
    }

    @Test
    @JsName(name = "non-empty_query_leads_to_non-empty_result")
    fun `non-empty query leads to non-empty result`() = runTest {
        viewModel.search.test {
            skipItems(count = 1)
            viewModel.onSearch(search = "query")
            expect(expected = "query") { awaitItem() }
            viewModel.movies.test {
                expect(expected = TestPaginatedData.data.size) {
                    flowOf(awaitItem()).asSnapshot().size
                }
            }
        }
    }

    @Test
    @JsName(name = "on_clear_leads_to_empty_result")
    fun `on clear leads to empty result`() = runTest {
        viewModel.search.test {
            skipItems(count = 1)
            viewModel.onSearch(search = "query")
            expect(expected = "query") { awaitItem() }
            viewModel.onClear()
            expect(expected = "") { awaitItem() }
            viewModel.movies.test {
                expect(expected = 0) {
                    flowOf(awaitItem()).asSnapshot().size
                }
            }
        }
    }

    @Test
    @JsName(name = "on_search_failure_throws_error")
    fun `on search failure throws error`() = runTest {
        resetAnswers(repository)
        every {
            repository.searchMovies(query = any(), page = any(), language = any())
        } returns flow { error(message = "Error") }

        viewModel.search.test {
            skipItems(count = 1)
            viewModel.onSearch(search = "invalid")
            expect(expected = "invalid") { awaitItem() }
            viewModel.movies.test {
                assertIs<IllegalStateException>(
                    value = assertFails {
                        flowOf(value = awaitItem()).asSnapshot()
                    }
                )
            }
        }
    }

    @Test
    @JsName(name = "error_is_set_and_consumed")
    fun `error is set and consumed`() = runTest {
        viewModel.error.test {
            assertNull(actual = awaitItem())
            viewModel.onError(error = Exception())
            assertNotNull(actual = awaitItem())
            viewModel.onErrorConsumed()
            assertNull(actual = awaitItem())
        }
    }

}
