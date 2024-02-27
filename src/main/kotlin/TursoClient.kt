package com.jeliuc.turso.sdk

import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.plugins.HttpRequestRetry
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.serialization.kotlinx.json.json
import io.ktor.util.appendIfNameAbsent
import kotlinx.serialization.json.Json

class TursoClient(val httpClient: HttpClient) {
    @Suppress("unused")
    fun close() = httpClient.close()

    companion object {
        private const val DEFAULT_MAX_RETRIES = 3
        private const val DEFAULT_BASE_URI = "https://api.turso.tech"

        val jsonBuilder =
            Json {
                ignoreUnknownKeys = true
                encodeDefaults = true
                isLenient = true
            }

        fun of(
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

                    install(HttpRequestRetry) {
                        retryOnServerErrors(maxRetries = maxRetries)
                        exponentialDelay()
                    }

                    defaultRequest {
                        url(baseUri)
                        headers.appendIfNameAbsent("Authorization", "Bearer: $authToken")
                    }
                }

            return TursoClient(httpClient)
        }
    }
}
