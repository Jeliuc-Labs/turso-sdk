package com.jeliuc.turso.sdk.resource

import com.jeliuc.turso.sdk.TursoClient
import com.jeliuc.turso.sdk.model.Authorization
import com.jeliuc.turso.sdk.model.CreateGroup
import com.jeliuc.turso.sdk.model.GroupResponse
import com.jeliuc.turso.sdk.model.ListGroupsResponse
import com.jeliuc.turso.sdk.model.TokenResponse
import com.jeliuc.turso.sdk.model.TransferGroupRequest
import com.jeliuc.turso.sdk.resources.handleResponse
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType

val TursoClient.groups: Groups
    get() = Groups(this)

class Groups(private val client: TursoClient) {
    suspend fun list(organization: String) =
        client.httpClient.get(Resources.basePath(organization)) {
            contentType(ContentType.Application.Json)
        }.let { response ->
            handleResponse<ListGroupsResponse>(response)
        }

    suspend fun retrieve(
        organization: String,
        groupName: String,
    ) = client.httpClient.get(Resources.groupPath(organization, groupName)) {
        contentType(ContentType.Application.Json)
    }.let { response ->
        handleResponse<GroupResponse>(response)
    }

    suspend fun create(
        organization: String,
        group: CreateGroup,
    ) = client.httpClient.post(Resources.basePath(organization)) {
        contentType(ContentType.Application.Json)
        setBody(group)
    }.let { response ->
        handleResponse<GroupResponse>(response)
    }

    suspend fun delete(
        organizationName: String,
        groupName: String,
    ) = client.httpClient.delete(Resources.groupPath(organizationName, groupName)) {
        contentType(ContentType.Application.Json)
    }.let { response ->
        handleResponse<GroupResponse>(response)
    }

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

    suspend fun removeLocation(
        organizationName: String,
        groupName: String,
        location: String,
    ) = client.httpClient.delete(Resources.locationPath(organizationName, groupName, location)) {
        contentType(ContentType.Application.Json)
    }.let { response ->
        handleResponse<GroupResponse>(response)
    }

    suspend fun updateVersion(
        organizationName: String,
        groupName: String,
    ) = client.httpClient.post(Resources.updatePath(organizationName, groupName)) {
        contentType(ContentType.Application.Json)
    }.let { response ->
        handleResponse<Unit>(response)
    }

    /**
     * Create an auth token for a group
     *
     * @param expiration Expiration time for the token (e.g., "2w1d30m"). Default: "never".
     * @throws [com.jeliuc.turso.sdk.models.ApiError]
     * @throws [com.jeliuc.turso.sdk.models.UnexpectedResultError]
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
