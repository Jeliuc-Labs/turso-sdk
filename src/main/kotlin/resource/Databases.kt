package com.jeliuc.turso.sdk.resources

import com.jeliuc.turso.sdk.TursoClient
import com.jeliuc.turso.sdk.models.CreateDatabase
import com.jeliuc.turso.sdk.models.CreateDatabaseResponse
import com.jeliuc.turso.sdk.models.CreateTokenResponse
import com.jeliuc.turso.sdk.models.DatabaseAuthorization
import com.jeliuc.turso.sdk.models.DatabaseDeleteResponse
import com.jeliuc.turso.sdk.models.DatabaseUsageResponse
import com.jeliuc.turso.sdk.models.InstanceResponse
import com.jeliuc.turso.sdk.models.ListDatabasesResponse
import com.jeliuc.turso.sdk.models.ListInstancesResponse
import com.jeliuc.turso.sdk.models.RetrieveDatabaseResponse
import com.jeliuc.turso.sdk.models.UploadDumpResponse
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
 * val databases: ListDatabasesResponse = client.databases.list("organization_name")
 * ```
 */
class Databases(private val client: TursoClient) : ResponseHandler {
    /**
     * Lists databases
     *
     * [See official documentation page](https://docs.turso.tech/api-reference/databases/list).
     *
     * @throws [com.jeliuc.turso.sdk.models.ApiError]
     * @throws [com.jeliuc.turso.sdk.models.UnexpectedResultError]
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
     * [See official documentation page](https://docs.turso.tech/api-reference/databases/create)
     *
     * @throws [com.jeliuc.turso.sdk.models.ApiError]
     * @throws [com.jeliuc.turso.sdk.models.UnexpectedResultError]
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
     * [See official documentation page](https://docs.turso.tech/api-reference/databases/retrieve)
     *
     * @throws [com.jeliuc.turso.sdk.models.ApiError]
     * @throws [com.jeliuc.turso.sdk.models.UnexpectedResultError]
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
     * [See official documentation page](https://docs.turso.tech/api-reference/databases/usage)
     *
     * @throws [com.jeliuc.turso.sdk.models.ApiError]
     * @throws [com.jeliuc.turso.sdk.models.UnexpectedResultError]
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
     * Deletes a database
     *
     * [See official documentation page](https://docs.turso.tech/api-reference/databases/delete)
     *
     * @throws [com.jeliuc.turso.sdk.models.ApiError]
     * @throws [com.jeliuc.turso.sdk.models.UnexpectedResultError]
     */
    suspend fun delete(
        organizationName: String,
        databaseName: String,
    ) = client.httpClient.delete(
        Resources.databasePath(organizationName, databaseName),
    ).let { response ->
        handleResponse<DatabaseDeleteResponse>(response)
    }

    /**
     * Lists instances
     *
     * [See official documentation page](https://docs.turso.tech/api-reference/databases/list-instances)
     *
     * @throws [com.jeliuc.turso.sdk.models.ApiError]
     * @throws [com.jeliuc.turso.sdk.models.UnexpectedResultError]
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
     * [See official documentation page](https://docs.turso.tech/api-reference/databases/retrieve-instance)
     *
     * @throws [com.jeliuc.turso.sdk.models.ApiError]
     * @throws [com.jeliuc.turso.sdk.models.UnexpectedResultError]
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
     * [See official documentation page](https://docs.turso.tech/api-reference/databases/create-token)
     *
     * @throws [com.jeliuc.turso.sdk.models.ApiError]
     * @throws [com.jeliuc.turso.sdk.models.UnexpectedResultError]
     */
    suspend fun createToken(
        organizationName: String,
        databaseName: String,
        expiration: String = "never",
        authorization: DatabaseAuthorization = DatabaseAuthorization.FULL_ACCESS,
    ) = client.httpClient.post(
        Resources.createTokenPath(organizationName, databaseName),
    ) {
        contentType(ContentType.Application.Json)
        parameter("expiration", expiration)
        parameter("authorization", authorization.value)
    }.let { response ->
        handleResponse<CreateTokenResponse>(response)
    }

    /**
     * Invalidates all tokens
     *
     * [See official documentation page](https://docs.turso.tech/api-reference/databases/invalidate-tokens)
     *
     * @throws [com.jeliuc.turso.sdk.models.ApiError]
     * @throws [com.jeliuc.turso.sdk.models.UnexpectedResultError]
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
     * [See official documentation page](https://docs.turso.tech/api-reference/databases/upload-dump)
     *
     * @throws [com.jeliuc.turso.sdk.models.ApiError]
     * @throws [com.jeliuc.turso.sdk.models.UnexpectedResultError]
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

        fun baseInstancesPath(
            organizationName: String,
            databaseName: String,
        ) = databasePath(organizationName, databaseName) + "/instances"

        fun instancePath(
            organizationName: String,
            databaseName: String,
            instanceName: String,
        ) = baseInstancesPath(organizationName, databaseName) + "/instances/$instanceName"

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
