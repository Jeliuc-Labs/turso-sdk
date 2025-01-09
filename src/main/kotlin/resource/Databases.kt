/*
 * Copyright 2024 Jeliuc.com S.R.L. and Turso SDK contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
 */

package com.jeliuc.turso.sdk.resource

import com.jeliuc.turso.sdk.TursoClient
import com.jeliuc.turso.sdk.model.Authorization
import com.jeliuc.turso.sdk.model.ConfigurationResponse
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
import com.jeliuc.turso.sdk.model.UpdateConfigurationRequest
import com.jeliuc.turso.sdk.model.UploadDumpResponse
import io.ktor.client.request.delete
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.forms.formData
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.patch
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType
import kotlinx.datetime.LocalDateTime
import java.io.File

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
     * @see <a href="https://docs.turso.tech/api-reference/databases/list">API Reference</a>
     */
    suspend fun list(organizationName: String): ListDatabasesResponse =
        client.httpClient.get(
            Path.organizationPath(organizationName),
        ) {
            contentType(ContentType.Application.Json)
        }.let { response ->
            handleResponse<ListDatabasesResponse>(response)
        }

    /**
     * Creates a database
     *
     * @see <a href="https://docs.turso.tech/api-reference/databases/create">API Reference</a>
     */
    suspend fun create(
        organizationName: String,
        database: CreateDatabase,
    ): CreateDatabaseResponse =
        client.httpClient.post(
            Path.organizationPath(organizationName),
        ) {
            contentType(ContentType.Application.Json)
            setBody(database)
        }.let { response ->
            handleResponse<CreateDatabaseResponse>(response)
        }

    /**
     * Retrieves a database
     *
     * @see <a href="https://docs.turso.tech/api-reference/databases/retrieve">API Reference</a>
     */
    suspend fun retrieve(
        organizationName: String,
        databaseName: String,
    ): RetrieveDatabaseResponse =
        client.httpClient.get(
            Path.databases(organizationName, databaseName),
        ) { contentType(ContentType.Application.Json) }.let {
            handleResponse<RetrieveDatabaseResponse>(it)
        }

    /**
     * Retrieve Database Configuration
     *
     * @see <a href="https://docs.turso.tech/api-reference/databases/configuration">API Reference</a>
     */
    suspend fun retrieveConfiguration(
        organizationName: String,
        databaseName: String,
    ): ConfigurationResponse =
        client.httpClient.get(
            Path.configuration(organizationName, databaseName),
        ) { contentType(ContentType.Application.Json) }.let { response ->
            handleResponse<ConfigurationResponse>(response)
        }

    /**
     * Update Database Configuration
     *
     * @see <a href="https://docs.turso.tech/api-reference/databases/update-configuration">API Reference</a>
     */
    suspend fun updateConfiguration(
        organizationName: String,
        databaseName: String,
        updateConfiguration: UpdateConfigurationRequest,
    ): ConfigurationResponse =
        client.httpClient.patch(
            Path.configuration(organizationName, databaseName),
        ) {
            contentType(ContentType.Application.Json)
            setBody(updateConfiguration)
        }.let { response ->
            handleResponse<ConfigurationResponse>(response)
        }

    /**
     * Gets database usage
     *
     * @see <a href="https://docs.turso.tech/api-reference/databases/usage">API Reference</a>
     *
     * @throws [com.jeliuc.turso.sdk.model.ApiError]
     * @throws [com.jeliuc.turso.sdk.model.UnexpectedResultError]
     */
    suspend fun usage(
        organizationName: String,
        databaseName: String,
        from: LocalDateTime? = null,
        to: LocalDateTime? = null,
    ): DatabaseUsageResponse =
        client.httpClient.get(
            Path.usage(organizationName, databaseName),
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
     * @see <a href="https://docs.turso.tech/api-reference/databases/stats">API Reference</a>
     */
    suspend fun stats(
        organizationName: String,
        databaseName: String,
    ): StatsResponse =
        client.httpClient.get(
            Path.stats(organizationName, databaseName),
        ) {
            contentType(ContentType.Application.Json)
        }.let { response ->
            handleResponse<StatsResponse>(response)
        }

    /**
     * Deletes a database
     *
     * @see <a href="https://docs.turso.tech/api-reference/databases/delete">API Reference</a>
     */
    suspend fun delete(
        organizationName: String,
        databaseName: String,
    ): DeleteDatabaseResponse =
        client.httpClient.delete(
            Path.databases(organizationName, databaseName),
        ).let { response ->
            handleResponse<DeleteDatabaseResponse>(response)
        }

    /**
     * Lists instances
     *
     * @see <a href="https://docs.turso.tech/api-reference/databases/list-instances">API Reference</a>
     */
    suspend fun listInstances(
        organizationName: String,
        databaseName: String,
    ): ListInstancesResponse =
        client.httpClient.get(
            Path.instances(organizationName, databaseName),
        ) {
            contentType(ContentType.Application.Json)
        }.let { response ->
            handleResponse<ListInstancesResponse>(response)
        }

    /**
     * Retrieves an instance of the database
     *
     * @see <a href="https://docs.turso.tech/api-reference/databases/retrieve-instance">API Reference</a>
     */
    suspend fun retrieveInstance(
        organizationName: String,
        databaseName: String,
        instanceName: String,
    ): InstanceResponse =
        client.httpClient.get(
            Path.instances(organizationName, databaseName, instanceName),
        ) { contentType(ContentType.Application.Json) }.let { response ->
            handleResponse<InstanceResponse>(response)
        }

    /**
     * Creates a token
     *
     * @see <a href="https://docs.turso.tech/api-reference/databases/create-token">API Reference</a>
     */
    suspend fun createToken(
        organizationName: String,
        databaseName: String,
        expiration: String = "never",
        authorization: Authorization = Authorization.FULL_ACCESS,
    ): TokenResponse =
        client.httpClient.post(
            Path.tokens(organizationName, databaseName),
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
     * @see <a href="https://docs.turso.tech/api-reference/databases/invalidate-tokens">API Reference</a>
     */
    suspend fun invalidateTokens(
        organizationName: String,
        databaseName: String,
    ): Unit =
        client.httpClient.post(
            Path.invalidateTokens(organizationName, databaseName),
        ) { contentType(ContentType.Application.Json) }.let { response ->
            handleResponse<Unit>(response)
        }

    /**
     * Uploads a database dump
     *
     * @see <a href="https://docs.turso.tech/api-reference/databases/upload-dump">API Reference</a>
     */
    suspend fun uploadDump(
        organizationName: String,
        file: File,
    ): UploadDumpResponse =
        client.httpClient.post(
            Path.dumps(organizationName),
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

    internal object Path {
        private const val RESOURCE_PATH = "/v1/organizations/{organizationName}/databases"

        fun organizationPath(organizationName: String) = RESOURCE_PATH.replace("{organizationName}", organizationName)

        fun databases(
            organizationName: String,
            databaseName: String,
        ) = RESOURCE_PATH.replace("{organizationName}", organizationName) + "/$databaseName"

        fun configuration(
            organizationName: String,
            databaseName: String,
        ) = databases(organizationName, databaseName) + "/configuration"

        fun usage(
            organizationName: String,
            databaseName: String,
        ) = databases(organizationName, databaseName) + "/usage"

        fun stats(
            organizationName: String,
            databaseName: String,
        ) = databases(organizationName, databaseName) + "/stats"

        fun instances(
            organizationName: String,
            databaseName: String,
        ) = databases(organizationName, databaseName) + "/instances"

        fun instances(
            organizationName: String,
            databaseName: String,
            instanceName: String,
        ) = instances(organizationName, databaseName) + "/$instanceName"

        fun tokens(
            organizationName: String,
            databaseName: String,
        ) = databases(organizationName, databaseName) + "/auth/tokens"

        fun invalidateTokens(
            organizationName: String,
            databaseName: String,
        ) = databases(organizationName, databaseName) + "/auth/rotate"

        fun dumps(organizationName: String) = RESOURCE_PATH.replace("{organizationName}", organizationName) + "/dumps"
    }
}
