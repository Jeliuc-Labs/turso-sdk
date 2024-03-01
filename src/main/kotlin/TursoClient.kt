/*
 * Copyright 2024 Jeliuc.com S.R.L. and Turso SDK contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
 */

package com.jeliuc.turso.sdk

import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.plugins.HttpRequestRetry
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.serialization.kotlinx.json.json
import io.ktor.util.appendIfNameAbsent
import kotlinx.serialization.json.Json

/**
 * TursoClient
 *
 * Create the instance of the client:
 *
 * ```kotlin
 * val client = TursoClient.using(
 *     engine = CIO.create(),
 *     authToken = "token"
 * )
 * ```
 *
 * Access the resource:
 *
 * ```kotlin
 * val locationsResponse = client.locations.list()
 * ```
 */
class TursoClient(val httpClient: HttpClient) {
    @Suppress("unused")
    fun close() = httpClient.close()

    companion object {
        private const val DEFAULT_MAX_RETRIES = 3
        private const val DEFAULT_BASE_URI = "https://api.turso.tech"

        private val jsonBuilder =
            Json {
                ignoreUnknownKeys = true
                encodeDefaults = true
                isLenient = true
            }

        fun using(
            engine: HttpClientEngine,
            authToken: String,
            baseUri: String = DEFAULT_BASE_URI,
            maxRetries: Int = DEFAULT_MAX_RETRIES,
        ): TursoClient {
            val httpClient =
                HttpClient(engine) {
                    install(ContentNegotiation) {
                        json(jsonBuilder)
                    }

                    install(HttpTimeout) {
                        requestTimeoutMillis = 30000
                    }

                    install(HttpRequestRetry) {
                        retryOnServerErrors(maxRetries = maxRetries)
                        exponentialDelay()
                    }

                    defaultRequest {
                        url(baseUri)
                        headers.appendIfNameAbsent("Authorization", "Bearer $authToken")
                    }
                }

            return TursoClient(httpClient)
        }
    }
}
