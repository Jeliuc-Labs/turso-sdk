package com.jeliuc.turso.sdk.resource

import com.jeliuc.turso.sdk.TursoClient
import com.jeliuc.turso.sdk.model.CreateApiTokenResponse
import com.jeliuc.turso.sdk.model.ListApiTokensResponse
import com.jeliuc.turso.sdk.model.RevokeApiTokenResponse
import com.jeliuc.turso.sdk.model.ValidateTokenResponse
import com.jeliuc.turso.sdk.resources.ResponseHandler
import com.jeliuc.turso.sdk.resources.handleResponse
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.http.ContentType
import io.ktor.http.contentType

val TursoClient.apiTokens: ApiTokens
    get() = ApiTokens(this)

class ApiTokens(private val client: TursoClient) : ResponseHandler {
    suspend fun create(tokenName: String) =
        client.httpClient.post(Resources.tokenPath(tokenName)) {
            contentType(ContentType.Application.Json)
        }.let { response ->
            handleResponse<CreateApiTokenResponse>(response)
        }

    suspend fun validate() =
        client.httpClient.get(Resources.validatePath()) {
            contentType(ContentType.Application.Json)
        }.let { response ->
            handleResponse<ValidateTokenResponse>(response)
        }

    suspend fun list() =
        client.httpClient.get(Resources.basePath()) {
            contentType(ContentType.Application.Json)
        }.let { response ->
            handleResponse<ListApiTokensResponse>(response)
        }

    suspend fun revoke(tokenName: String) =
        client.httpClient.delete(Resources.tokenPath(tokenName)) {
            contentType(ContentType.Application.Json)
        }.let {
            handleResponse<RevokeApiTokenResponse>(it)
        }

    object Resources {
        private const val BASE_PATH = "/v1/auth/api-tokens"

        fun basePath() = BASE_PATH

        fun tokenPath(tokenName: String) = "$BASE_PATH/$tokenName"

        fun validatePath() = "/v1/auth/validate"
    }
}
