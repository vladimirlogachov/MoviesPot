package com.vlohachov.shared.data.utils

import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.MockRequestHandleScope
import io.ktor.client.engine.mock.respond
import io.ktor.client.engine.mock.respondBadRequest
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.HttpResponseData
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer

private inline fun <reified T> MockRequestHandleScope.respondOk(data: T): HttpResponseData =
    respond(
        status = HttpStatusCode.OK,
        headers = headersOf(name = HttpHeaders.ContentType, value = "application/json"),
        content = Json.encodeToString(serializer = serializer(), value = data)
    )

internal inline fun <reified T> mockClientSuccess(data: T): HttpClient =
    HttpClient(engine = MockEngine { respondOk(data) }) {
        install(ContentNegotiation) { json() }
    }

internal fun mockClientFailure(): HttpClient =
    HttpClient(engine = MockEngine { respondBadRequest() }) {
        install(ContentNegotiation) { json() }
    }
