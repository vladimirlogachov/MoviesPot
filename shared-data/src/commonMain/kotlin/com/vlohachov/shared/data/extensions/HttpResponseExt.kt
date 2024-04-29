package com.vlohachov.shared.data.extensions

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.statement.HttpStatement
import io.ktor.http.HttpMethod
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

private inline fun <reified T> HttpClient.requestFlow(
    builder: HttpRequestBuilder = HttpRequestBuilder()
) = flow {
    HttpStatement(builder = builder, client = this@requestFlow)
        .execute()
        .body<T>()
        .also { response -> emit(value = response) }
}

internal inline fun <reified T> HttpClient.getFlow(builder: HttpRequestBuilder) =
    requestFlow<T>(builder = builder.apply { method = HttpMethod.Get })

internal inline fun <reified T> HttpClient.getFlow(
    block: HttpRequestBuilder.() -> Unit = {}
): Flow<T> = getFlow(builder = HttpRequestBuilder().apply(block = block))
