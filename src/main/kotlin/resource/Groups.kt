/*
 * Copyright 2024 Jeliuc.com S.R.L. and Turso SDK contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
 */

package com.jeliuc.turso.sdk.resource

import com.jeliuc.turso.sdk.TursoClient
import com.jeliuc.turso.sdk.model.Authorization
import com.jeliuc.turso.sdk.model.CreateGroup
import com.jeliuc.turso.sdk.model.GroupResponse
import com.jeliuc.turso.sdk.model.ListGroupsResponse
import com.jeliuc.turso.sdk.model.TokenResponse
import com.jeliuc.turso.sdk.model.TransferGroupRequest
import io.ktor.client.plugins.timeout
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType

val TursoClient.groups: Groups
    get() = Groups(this)

/**
 * Turso Groups API Resource
 *
 * ```kotlin
 * // client: TursoClient
 * val groups = client.groups.list("organizationName")
 * ```
 */
class Groups(private val client: TursoClient) : ResponseHandler() {
    /**
     * Lists all groups
     *
     * @see [https://docs.turso.tech/api-reference/groups/list]
     */
    suspend fun list(organizationName: String) =
        client.httpClient.get(Resources.basePath(organizationName)) {
            contentType(ContentType.Application.Json)
        }.let { response ->
            handleResponse<ListGroupsResponse>(response)
        }

    /**
     * Retrieves a group
     *
     * @see [https://docs.turso.tech/api-reference/groups/retrieve]
     */
    suspend fun retrieve(
        organizationName: String,
        groupName: String,
        timeoutMillis: Long = 60000,
    ) = client.httpClient.get(Resources.groupPath(organizationName, groupName)) {
        contentType(ContentType.Application.Json)
        timeout { requestTimeoutMillis = timeoutMillis }
    }.let { response ->
        handleResponse<GroupResponse>(response)
    }

    /**
     * Creates a group
     *
     * @see [https://docs.turso.tech/api-reference/groups/create]
     */
    suspend fun create(
        organizationName: String,
        group: CreateGroup,
    ) = client.httpClient.post(Resources.basePath(organizationName)) {
        // 30 seconds timeout, because groups creation takes longer than standard requests
        contentType(ContentType.Application.Json)
        setBody(group)
    }.let { response ->
        handleResponse<GroupResponse>(response)
    }

    /**
     * Deletes a group
     *
     * @see [https://docs.turso.tech/api-reference/groups/delete]
     */
    suspend fun delete(
        organizationName: String,
        groupName: String,
    ) = client.httpClient.delete(Resources.groupPath(organizationName, groupName)) {
        contentType(ContentType.Application.Json)
    }.let { response ->
        handleResponse<GroupResponse>(response)
    }

    /**
     * Transfers a group to another organization
     *
     * @see [https://docs.turso.tech/api-reference/groups/transfer]
     */
    suspend fun transfer(
        groupName: String,
        fromOrganization: String,
        toOrganization: String,
    ) = client.httpClient.post(Resources.transferPath(fromOrganization, groupName)) {
        contentType(ContentType.Application.Json)
        setBody(TransferGroupRequest(toOrganization))
    }.let { response ->
        handleResponse<GroupResponse>(response)
    }

    /**
     * Adds a location to a group
     *
     * @see [https://docs.turso.tech/api-reference/groups/add-location]
     */
    suspend fun addLocation(
        organizationName: String,
        groupName: String,
        location: String,
    ) = client.httpClient.post(
        Resources.locationPath(organizationName, groupName, location),
    ) {
        contentType(ContentType.Application.Json)
    }.let { response ->
        handleResponse<GroupResponse>(response)
    }

    /**
     * Removes a location from a group
     *
     * @see [https://docs.turso.tech/api-reference/groups/remove-location]
     */
    suspend fun removeLocation(
        organizationName: String,
        groupName: String,
        location: String,
    ) = client.httpClient.delete(Resources.locationPath(organizationName, groupName, location)) {
        contentType(ContentType.Application.Json)
    }.let { response ->
        handleResponse<GroupResponse>(response)
    }

    /**
     * Updates the LibSql version for all databases in the group
     *
     * @see [https://docs.turso.tech/api-reference/groups/update-database-versions]
     */
    suspend fun updateVersion(
        organizationName: String,
        groupName: String,
    ) = client.httpClient.post(Resources.updatePath(organizationName, groupName)) {
        contentType(ContentType.Application.Json)
    }.let { response ->
        handleResponse<Unit>(response)
    }

    /**
     * Creates an auth token for a group
     *
     * @see [https://docs.turso.tech/api-reference/groups/create-token]
     *
     * @param expiration Expiration time for the token (e.g., "2w1d30m"). Default: "never".
     */
    suspend fun createToken(
        organizationName: String,
        groupName: String,
        expiration: String? = null,
        authorization: Authorization? = null,
    ) = client.httpClient.post(Resources.tokenPath(organizationName, groupName)) {
        expiration?.let { parameter("expiration", it) }
        authorization?.let { parameter("authorization", it) }
        contentType(ContentType.Application.Json)
    }.let { response ->
        handleResponse<TokenResponse>(response)
    }

    /**
     * Invalidates all auth tokens of a group
     *
     * @see [https://docs.turso.tech/api-reference/groups/invalidate-tokens]
     */
    suspend fun invalidateTokens(
        organizationName: String,
        groupName: String,
    ) = client.httpClient.post(Resources.authRotatePath(organizationName, groupName)) {
        contentType(ContentType.Application.Json)
    }.let { response ->
        handleResponse<Unit>(response)
    }

    internal object Resources {
        private const val BASE_PATH = "/v1/organizations/{organizationName}/groups"

        fun basePath(organizationName: String) = BASE_PATH.replace("{organizationName}", organizationName)

        fun groupPath(
            organizationName: String,
            groupName: String,
        ) = basePath(organizationName) + "/$groupName"

        fun transferPath(
            fromOrganization: String,
            groupName: String,
        ) = groupPath(fromOrganization, groupName) + "/transfer"

        fun locationPath(
            organizationName: String,
            groupName: String,
            location: String,
        ) = groupPath(
            organizationName,
            groupName,
        ) + "/locations/$location"

        fun updatePath(
            organizationName: String,
            groupName: String,
        ) = groupPath(organizationName, groupName) + "/update"

        fun tokenPath(
            organizationName: String,
            groupName: String,
        ) = groupPath(organizationName, groupName) + "/auth/tokens"

        fun authRotatePath(
            organizationName: String,
            groupName: String,
        ) = groupPath(organizationName, groupName) + "/auth/rotate"
    }
}
