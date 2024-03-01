/*
 * Copyright 2024 Jeliuc.com S.R.L. and Turso SDK contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
 */

package com.jeliuc.turso.sdk.resource

import com.jeliuc.turso.sdk.TursoClient
import com.jeliuc.turso.sdk.model.Authorization
import com.jeliuc.turso.sdk.model.CreateDatabase
import com.jeliuc.turso.sdk.model.CreateDatabaseResponse
import com.jeliuc.turso.sdk.model.DatabaseUsageResponse
import com.jeliuc.turso.sdk.model.DeleteDatabaseResponse
import com.jeliuc.turso.sdk.model.InstanceResponse
import com.jeliuc.turso.sdk.model.ListDatabasesResponse
import com.jeliuc.turso.sdk.model.ListInstancesResponse
import com.jeliuc.turso.sdk.model.RetrieveDatabaseResponse
import com.jeliuc.turso.sdk.model.StatsResponse
import com.jeliuc.turso.sdk.model.TokenResponse
import com.jeliuc.turso.sdk.model.UploadDumpResponse
import io.ktor.client.request.delete
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.forms.formData
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType
import java.io.File
import java.time.LocalDateTime

val TursoClient.databases: Databases
    get() = Databases(this)

/**
 * Turso Databases API Resource
 *
 * Example usage:
 *
 * ```Kotlin
 * // client: TursoClient
 * val databases: ListDatabasesResponse = client.databases.list("organizationName")
 * ```
 */
class Databases(private val client: TursoClient) : ResponseHandler() {
    /**
     * Lists databases
     *
     * @see [https://docs.turso.tech/api-reference/databases/list]
     */
    suspend fun list(organizationName: String) =
        client.httpClient.get(
            Resources.organizationPath(organizationName),
        ) {
            contentType(ContentType.Application.Json)
        }.let { response ->
            handleResponse<ListDatabasesResponse>(response)
        }

    /**
     * Creates a database
     *
     * [https://docs.turso.tech/api-reference/databases/create]
     */
    suspend fun create(
        organizationName: String,
        database: CreateDatabase,
    ) = client.httpClient.post(
        Resources.organizationPath(organizationName),
    ) {
        contentType(ContentType.Application.Json)
        setBody(database)
    }.let { response ->
        handleResponse<CreateDatabaseResponse>(response)
    }

    /**
     * Retrieves a database
     *
     * @see [https://docs.turso.tech/api-reference/databases/retrieve]
     */
    suspend fun retrieve(
        organizationName: String,
        databaseName: String,
    ) = client.httpClient.get(
        Resources.databasePath(organizationName, databaseName),
    ) { contentType(ContentType.Application.Json) }.let {
        handleResponse<RetrieveDatabaseResponse>(it)
    }

    /**
     * Gets database usage
     *
     * @see [https://docs.turso.tech/api-reference/databases/usage]
     *
     * @throws [com.jeliuc.turso.sdk.model.ApiError]
     * @throws [com.jeliuc.turso.sdk.model.UnexpectedResultError]
     */
    suspend fun usage(
        organizationName: String,
        databaseName: String,
        from: LocalDateTime? = null,
        to: LocalDateTime? = null,
    ) = client.httpClient.get(
        Resources.databaseUsagePath(organizationName, databaseName),
    ) {
        contentType(ContentType.Application.Json)
        from?.let { parameter("from", it) }
        to?.let { parameter("to", it) }
    }.let { response ->
        handleResponse<DatabaseUsageResponse>(response)
    }

    /**
     * Gets database stats
     *
     * @see [https://docs.turso.tech/api-reference/databases/stats]
     */
    suspend fun stats(
        organizationName: String,
        databaseName: String,
    ) = client.httpClient.get(
        Resources.databaseStats(organizationName, databaseName),
    ) {
        contentType(ContentType.Application.Json)
    }.let { response ->
        handleResponse<StatsResponse>(response)
    }

    /**
     * Deletes a database
     *
     * @see [https://docs.turso.tech/api-reference/databases/delete]
     */
    suspend fun delete(
        organizationName: String,
        databaseName: String,
    ) = client.httpClient.delete(
        Resources.databasePath(organizationName, databaseName),
    ).let { response ->
        handleResponse<DeleteDatabaseResponse>(response)
    }

    /**
     * Lists instances
     *
     * @see [https://docs.turso.tech/api-reference/databases/list-instances]
     */
    suspend fun listInstances(
        organizationName: String,
        databaseName: String,
    ) = client.httpClient.get(
        Resources.baseInstancesPath(organizationName, databaseName),
    ) {
        contentType(ContentType.Application.Json)
    }.let { response ->
        handleResponse<ListInstancesResponse>(response)
    }

    /**
     * Retrieves an instance of the database
     *
     * @see [https://docs.turso.tech/api-reference/databases/retrieve-instance]
     */
    suspend fun retrieveInstance(
        organizationName: String,
        databaseName: String,
        instanceName: String,
    ) = client.httpClient.get(
        Resources.instancePath(organizationName, databaseName, instanceName),
    ) { contentType(ContentType.Application.Json) }.let { response ->
        handleResponse<InstanceResponse>(response)
    }

    /**
     * Creates a token
     *
     * @see [https://docs.turso.tech/api-reference/databases/create-token]
     */
    suspend fun createToken(
        organizationName: String,
        databaseName: String,
        expiration: String = "never",
        authorization: Authorization = Authorization.FULL_ACCESS,
    ) = client.httpClient.post(
        Resources.createTokenPath(organizationName, databaseName),
    ) {
        contentType(ContentType.Application.Json)
        parameter("expiration", expiration)
        parameter("authorization", authorization.value)
    }.let { response ->
        handleResponse<TokenResponse>(response)
    }

    /**
     * Invalidates all tokens
     *
     * @see [https://docs.turso.tech/api-reference/databases/invalidate-tokens]
     */
    suspend fun invalidateTokens(
        organizationName: String,
        databaseName: String,
    ) = client.httpClient.post(
        Resources.invalidateAuthTokensPath(organizationName, databaseName),
    ) { contentType(ContentType.Application.Json) }.let { response ->
        handleResponse<Unit>(response)
    }

    /**
     * Uploads a database dump
     *
     * @see [https://docs.turso.tech/api-reference/databases/upload-dump]
     */
    suspend fun uploadDump(
        organizationName: String,
        file: File,
    ) = client.httpClient.post(
        Resources.uploadDumpPath(organizationName),
    ) {
        contentType(ContentType.MultiPart.FormData)
        setBody(
            body =
                MultiPartFormDataContent(
                    formData {
                        append(
                            "file",
                            file.readBytes(),
                            Headers.build {
                                append(HttpHeaders.ContentType, ContentType.Application.OctetStream.toString())
                                append(HttpHeaders.ContentDisposition, "form-data; name=file; filename=${file.name}")
                            },
                        )
                    },
                ),
        )
    }.let { response ->
        handleResponse<UploadDumpResponse>(response)
    }

    internal object Resources {
        private const val RESOURCE_PATH = "/v1/organizations/{organizationName}/databases"

        fun organizationPath(organizationName: String) = RESOURCE_PATH.replace("{organizationName}", organizationName)

        fun databasePath(
            organizationName: String,
            databaseName: String,
        ) = RESOURCE_PATH.replace("{organizationName}", organizationName) + "/$databaseName"

        fun databaseUsagePath(
            organizationName: String,
            databaseName: String,
        ) = databasePath(organizationName, databaseName) + "/usage"

        fun databaseStats(
            organizationName: String,
            databaseName: String,
        ) = databasePath(organizationName, databaseName) + "/stats"

        fun baseInstancesPath(
            organizationName: String,
            databaseName: String,
        ) = databasePath(organizationName, databaseName) + "/instances"

        fun instancePath(
            organizationName: String,
            databaseName: String,
            instanceName: String,
        ) = baseInstancesPath(organizationName, databaseName) + "/$instanceName"

        fun createTokenPath(
            organizationName: String,
            databaseName: String,
        ) = databasePath(organizationName, databaseName) + "/auth/tokens"

        fun invalidateAuthTokensPath(
            organizationName: String,
            databaseName: String,
        ) = databasePath(organizationName, databaseName) + "/auth/rotate"

        fun uploadDumpPath(organizationName: String) = RESOURCE_PATH.replace("{organizationName}", organizationName) + "/dumps"
    }
}
