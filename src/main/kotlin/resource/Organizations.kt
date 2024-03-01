/*
 * Copyright 2024 Jeliuc.com S.R.L. and Turso SDK contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
 */

package com.jeliuc.turso.sdk.resource

import com.jeliuc.turso.sdk.TursoClient
import com.jeliuc.turso.sdk.model.CreateInviteResponse
import com.jeliuc.turso.sdk.model.CreateMember
import com.jeliuc.turso.sdk.model.CreateMemberResponse
import com.jeliuc.turso.sdk.model.DeleteMemberResponse
import com.jeliuc.turso.sdk.model.ListInvitesResponse
import com.jeliuc.turso.sdk.model.ListMembersResponse
import com.jeliuc.turso.sdk.model.Organization
import com.jeliuc.turso.sdk.model.OrganizationResponse
import com.jeliuc.turso.sdk.model.UpdateOrganizationRequest
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.patch
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType

val TursoClient.organizations: Organizations
    get() = Organizations(this)

/**
 * Turso Organizations API Resource
 *
 * Example usage:
 *
 * ```kotlin
 * // client: TursoClient
 * val organizations = client.organizations.list()
 * ```
 */
class Organizations(val client: TursoClient) : ResponseHandler() {
    val members: Members
        get() = Members(this.client)

    val invites: Invites
        get() = Invites(this.client)

    /**
     * Lists all organizations
     *
     * @see [https://docs.turso.tech/api-reference/organizations/list]
     */
    suspend fun list() =
        client.httpClient.get(Resources.basePath()) {
            contentType(ContentType.Application.Json)
        }.let { response ->
            handleResponse<List<Organization>>(response)
        }

    /**
     * Updates an organization
     *
     * @see [https://docs.turso.tech/api-reference/organizations/update]
     */
    suspend fun update(
        organizationName: String,
        organizationUpdate: UpdateOrganizationRequest,
    ) = client.httpClient.patch(
        Resources.organizationPath(organizationName),
    ) {
        contentType(ContentType.Application.Json)
        setBody(organizationUpdate)
    }.let { response ->
        handleResponse<OrganizationResponse>(response)
    }

    internal object Resources {
        private const val BASE_PATH = "/v1/organizations"

        fun basePath() = BASE_PATH

        fun organizationPath(organizationName: String) = "$BASE_PATH/$organizationName"
    }
}

/**
 * Turso Members API Resource
 *
 * Example usage:
 *
 * ```kotlin
 * // client: TursoClient
 * val members = client.organizations.members.list("organizationName")
 * ```
 */
class Members(val client: TursoClient) : ResponseHandler() {
    /**
     * Lists all members of an organization
     *
     * @see [https://docs.turso.tech/api-reference/organizations/members/list]
     */
    suspend fun list(organizationName: String) =
        client.httpClient.get(Resources.basePath(organizationName)) {
            contentType(ContentType.Application.Json)
        }.let { response ->
            handleResponse<ListMembersResponse>(response)
        }

    /**
     * Adds a member to an organization
     *
     * @see [https://docs.turso.tech/api-reference/organizations/members/add]
     */
    suspend fun add(
        organizationName: String,
        member: CreateMember,
    ) = client.httpClient.post(Resources.basePath(organizationName)) {
        contentType(ContentType.Application.Json)
        setBody(member)
    }.let { response ->
        handleResponse<CreateMemberResponse>(response)
    }

    /**
     * Removes a member from an organization
     *
     * @see [https://docs.turso.tech/api-reference/organizations/members/remove]
     */
    suspend fun remove(
        organizationName: String,
        username: String,
    ) = client.httpClient.delete(Resources.memberPath(organizationName, username)) {
        contentType(ContentType.Application.Json)
    }.let { response ->
        handleResponse<DeleteMemberResponse>(response)
    }

    internal object Resources {
        fun basePath(organizationName: String) = "${Organizations.Resources.basePath()}/$organizationName/members"

        fun memberPath(
            organizationName: String,
            memberName: String,
        ) = "${basePath(organizationName)}/$memberName"
    }
}

/**
 * Turso Invites API Resource
 *
 * Example usage:
 *
 * ```kotlin
 * // client: TursoClient
 * val invites = client.organizations.invites.list("organizationName")
 * ```
 */
class Invites(val client: TursoClient) : ResponseHandler() {
    /**
     * Lists all invites of an organization
     *
     * @see [https://docs.turso.tech/api-reference/organizations/invites/list]
     */
    suspend fun list(organizationName: String) =
        client.httpClient.get(Resources.basePath(organizationName)) {
            contentType(ContentType.Application.Json)
        }.let { response ->
            handleResponse<ListInvitesResponse>(response)
        }

    /**
     * Creates the user invitation to an organization
     *
     * @see [https://docs.turso.tech/api-reference/organizations/invites/create]
     */
    suspend fun create(
        organizationName: String,
        invite: CreateMember,
    ) = client.httpClient.post(Resources.basePath(organizationName)) {
        contentType(ContentType.Application.Json)
        setBody(invite)
    }.let { response ->
        handleResponse<CreateInviteResponse>(response)
    }

    internal object Resources {
        fun basePath(organizationName: String) = "${Organizations.Resources.basePath()}/$organizationName/invites"
    }
}
