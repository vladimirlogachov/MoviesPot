package com.vlohachov.shared.core

import com.vlohachov.shared.TestPaginatedData
import com.vlohachov.shared.domain.Result
import kotlin.js.JsName
import kotlin.test.Test
import kotlin.test.expect

class ViewStateTest {

    @Test
    @JsName(name = "loading_result_to_loading_state")
    fun `loading result to loading state`() {
        expect(expected = ViewState.Loading) {
            Result.Loading.toViewState()
        }
    }

    @Test
    @JsName(name = "error_result_to_error_state")
    fun `error result to error state`() {
        val error = IllegalArgumentException()
        expect(expected = ViewState.Error(error = error)) {
            Result.Error(exception = error).toViewState()
        }
    }

    @Test
    @JsName(name = "success_result_to_success_state")
    fun `success result to success state`() {
        val data = "data"
        expect(expected = ViewState.Success(data = data)) {
            Result.Success(value = data).toViewState()
        }
    }

    @Test
    @JsName(name = "loading_result_to_paginated_loading_state")
    fun `loading result to paginated loading state`() {
        expect(expected = ViewState.Loading) {
            Result.Loading.toViewStatePaginated<String>()
        }
    }

    @Test
    @JsName(name = "error_result_to_paginated_error_state")
    fun `error result to paginated error state`() {
        val error = IllegalArgumentException()
        expect(expected = ViewState.Error(error = error)) {
            Result.Error(exception = error).toViewStatePaginated<String>()
        }
    }

    @Test
    @JsName(name = "success_result_to_paginated_success_state")
    fun `success result to paginated success state`() {
        expect(expected = ViewState.Success(data = TestPaginatedData.data)) {
            Result.Success(value = TestPaginatedData).toViewStatePaginated()
        }
    }

}
