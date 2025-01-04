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
     * @see <a href="https://docs.turso.tech/api-reference/groups/list">API Reference</a>
     */
    suspend fun list(organizationName: String): ListGroupsResponse =
        client.httpClient.get(Path.groups(organizationName)) {
            contentType(ContentType.Application.Json)
        }.let { response ->
            handleResponse<ListGroupsResponse>(response)
        }

    /**
     * Retrieves a group
     *
     * @see <a href="https://docs.turso.tech/api-reference/groups/retrieve">API Reference</a>
     */
    suspend fun retrieve(
        organizationName: String,
        groupName: String,
        timeoutMillis: Long = 60000,
    ): GroupResponse =
        client.httpClient.get(Path.groups(organizationName, groupName)) {
            contentType(ContentType.Application.Json)
            timeout { requestTimeoutMillis = timeoutMillis }
        }.let { response ->
            handleResponse<GroupResponse>(response)
        }

    /**
     * Creates a group
     *
     * @see <a href="https://docs.turso.tech/api-reference/groups/create">API Reference</a>
     */
    suspend fun create(
        organizationName: String,
        group: CreateGroup,
    ): GroupResponse =
        client.httpClient.post(Path.groups(organizationName)) {
            // 30 seconds timeout, because groups creation takes longer than standard requests
            contentType(ContentType.Application.Json)
            setBody(group)
        }.let { response ->
            handleResponse<GroupResponse>(response)
        }

    /**
     * Deletes a group
     *
     * @see <a href="https://docs.turso.tech/api-reference/groups/delete">API Reference</a>
     */
    suspend fun delete(
        organizationName: String,
        groupName: String,
    ): GroupResponse =
        client.httpClient.delete(Path.groups(organizationName, groupName)) {
            contentType(ContentType.Application.Json)
        }.let { response ->
            handleResponse<GroupResponse>(response)
        }

    /**
     * Transfers a group to another organization
     *
     * @see <a href="https://docs.turso.tech/api-reference/groups/transfer">API Reference</a>
     */
    suspend fun transfer(
        groupName: String,
        fromOrganization: String,
        toOrganization: String,
    ): GroupResponse =
        client.httpClient.post(Path.transfer(fromOrganization, groupName)) {
            contentType(ContentType.Application.Json)
            setBody(TransferGroupRequest(toOrganization))
        }.let { response ->
            handleResponse<GroupResponse>(response)
        }

    /**
     * Adds a location to a group
     *
     * @see <a href="https://docs.turso.tech/api-reference/groups/add-location">API Reference</a>
     */
    suspend fun addLocation(
        organizationName: String,
        groupName: String,
        location: String,
    ): GroupResponse =
        client.httpClient.post(
            Path.locations(organizationName, groupName, location),
        ) {
            contentType(ContentType.Application.Json)
        }.let { response ->
            handleResponse<GroupResponse>(response) // todo check if group response doesn't have the `version` entry
        }

    /**
     * Removes a location from a group
     *
     * @see <a href="https://docs.turso.tech/api-reference/groups/remove-location">API Reference</a>
     */
    suspend fun removeLocation(
        organizationName: String,
        groupName: String,
        location: String,
    ): GroupResponse =
        client.httpClient.delete(Path.locations(organizationName, groupName, location)) {
            contentType(ContentType.Application.Json)
        }.let { response ->
            handleResponse<GroupResponse>(response)
        }

    /**
     * Updates the LibSql version for all databases in the group
     *
     * @see <a href="https://docs.turso.tech/api-reference/groups/update-database-versions">API Reference</a>
     */
    suspend fun updateVersion(
        organizationName: String,
        groupName: String,
    ): Unit =
        client.httpClient.post(Path.update(organizationName, groupName)) {
            contentType(ContentType.Application.Json)
        }.let { response ->
            handleResponse<Unit>(response)
        }

    /**
     * Creates an auth token for a group
     *
     * @see <a href="https://docs.turso.tech/api-reference/groups/create-token">API Reference</a>
     *
     * @param expiration Expiration time for the token (e.g., "2w1d30m"). Default: "never".
     */
    suspend fun createToken(
        organizationName: String,
        groupName: String,
        expiration: String? = null,
        authorization: Authorization? = null,
    ): TokenResponse =
        client.httpClient.post(Path.tokens(organizationName, groupName)) {
            expiration?.let { parameter("expiration", it) }
            authorization?.let { parameter("authorization", it) }
            contentType(ContentType.Application.Json)
        }.let { response ->
            handleResponse<TokenResponse>(response)
        }

    /**
     * Unarchive a group that has been archived due to inactivity.
     *
     * @see <a href="https://docs.turso.tech/api-reference/groups/unarchive">API Reference</a>
     */
    suspend fun unarchive(
        organizationName: String,
        groupName: String,
    ): GroupResponse =
        client.httpClient.post(Path.unarchive(organizationName, groupName)) {
            contentType(ContentType.Application.Json)
        }.let { response ->
            handleResponse<GroupResponse>(response)
        }

    /**
     * Invalidates all auth tokens of a group
     *
     * @see <a href="https://docs.turso.tech/api-reference/groups/invalidate-tokens">API Reference</a>
     */
    suspend fun invalidateTokens(
        organizationName: String,
        groupName: String,
    ): Unit =
        client.httpClient.post(Path.invalidateTokens(organizationName, groupName)) {
            contentType(ContentType.Application.Json)
        }.let { response ->
            handleResponse<Unit>(response)
        }

    internal object Path {
        private const val BASE_PATH = "/v1/organizations/{organizationName}/groups"

        fun groups(organizationName: String) = BASE_PATH.replace("{organizationName}", organizationName)

        fun groups(
            organizationName: String,
            groupName: String,
        ) = groups(organizationName) + "/$groupName"

        fun transfer(
            fromOrganization: String,
            groupName: String,
        ) = groups(fromOrganization, groupName) + "/transfer"

        fun locations(
            organizationName: String,
            groupName: String,
            location: String,
        ) = groups(
            organizationName,
            groupName,
        ) + "/locations/$location"

        fun update(
            organizationName: String,
            groupName: String,
        ) = groups(organizationName, groupName) + "/update"

        fun tokens(
            organizationName: String,
            groupName: String,
        ) = groups(organizationName, groupName) + "/auth/tokens"

        fun invalidateTokens(
            organizationName: String,
            groupName: String,
        ) = groups(organizationName, groupName) + "/auth/rotate"

        fun unarchive(
            organizationName: String,
            groupName: String,
        ) = groups(organizationName, groupName) + "/unarchive"
    }
}
