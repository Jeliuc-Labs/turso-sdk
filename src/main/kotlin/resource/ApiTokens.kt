/*
 * Copyright 2024 Jeliuc.com S.R.L. and Turso SDK contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
 */

package com.jeliuc.turso.sdk.resource

import com.jeliuc.turso.sdk.TursoClient
import com.jeliuc.turso.sdk.model.CreateApiTokenResponse
import com.jeliuc.turso.sdk.model.ListApiTokensResponse
import com.jeliuc.turso.sdk.model.RevokeApiTokenResponse
import com.jeliuc.turso.sdk.model.ValidateTokenResponse
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.http.ContentType
import io.ktor.http.contentType

val TursoClient.apiTokens: ApiTokens
    get() = ApiTokens(this)

/**
 * Turso API Tokens API Resource
 *
 * ```kotlin
 * // client: TursoClient
 * val apiTokens = client.apiTokens.list()
 * ```
 */
class ApiTokens(private val client: TursoClient) : ResponseHandler() {
    /**
     * Creates API token
     *
     * @see <a href="https://docs.turso.tech/api-reference/tokens/create">API Reference</a>
     */
    suspend fun create(tokenName: String) =
        client.httpClient.post(Path.tokens(tokenName)) {
            contentType(ContentType.Application.Json)
        }.let { response ->
            handleResponse<CreateApiTokenResponse>(response)
        }

    /**
     * Validates the current API token
     *
     * @see <a href="https://docs.turso.tech/api-reference/tokens/validate">API Reference</a>
     */
    suspend fun validate() =
        client.httpClient.get(Path.validate()) {
            contentType(ContentType.Application.Json)
        }.let { response ->
            handleResponse<ValidateTokenResponse>(response)
        }

    /**
     * Lists all API tokens
     *
     * @see <a href="https://docs.turso.tech/api-reference/tokens/list">API Reference</a>
     */
    suspend fun list() =
        client.httpClient.get(Path.tokens()) {
            contentType(ContentType.Application.Json)
        }.let { response ->
            handleResponse<ListApiTokensResponse>(response)
        }

    /**
     * Revokes an API token
     *
     * @see <a href="https://docs.turso.tech/api-reference/tokens/revoke">API Reference</a>
     */
    suspend fun revoke(tokenName: String) =
        client.httpClient.delete(Path.tokens(tokenName)) {
            contentType(ContentType.Application.Json)
        }.let {
            handleResponse<RevokeApiTokenResponse>(it)
        }

    internal object Path {
        private const val BASE_PATH = "/v1/auth/api-tokens"

        fun tokens() = BASE_PATH

        fun tokens(tokenName: String) = "$BASE_PATH/$tokenName"

        fun validate() = "/v1/auth/validate"
    }
}
