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
import com.jeliuc.turso.sdk.model.InvoicesResponse
import com.jeliuc.turso.sdk.model.ListInvitesResponse
import com.jeliuc.turso.sdk.model.ListMembersResponse
import com.jeliuc.turso.sdk.model.Organization
import com.jeliuc.turso.sdk.model.OrganizationPlan
import com.jeliuc.turso.sdk.model.OrganizationResponse
import com.jeliuc.turso.sdk.model.SubscriptionResponse
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
     * @see <a href="https://docs.turso.tech/api-reference/organizations/list">API Reference</a>
     */
    suspend fun list(): List<Organization> =
        client.httpClient.get(Path.organizations()) {
            contentType(ContentType.Application.Json)
        }.let { response ->
            handleResponse<List<Organization>>(response)
        }

    /**
     * Updates an organization
     *
     * @see <a href="https://docs.turso.tech/api-reference/organizations/update">API Reference</a>
     */
    suspend fun update(
        organizationName: String,
        organizationUpdate: UpdateOrganizationRequest,
    ): OrganizationResponse =
        client.httpClient.patch(
            Path.organizations(organizationName),
        ) {
            contentType(ContentType.Application.Json)
            setBody(organizationUpdate)
        }.let { response ->
            handleResponse<OrganizationResponse>(response)
        }

    /**
     * List Plans
     *
     * @see <a href="https://docs.turso.tech/api-reference/organizations/plans">API Reference</a>
     */
    suspend fun plans(organizationName: String): List<OrganizationPlan> =
        client.httpClient.get(
            Path.plans(organizationName),
        ) {
            contentType(ContentType.Application.Json)
        }.let { response ->
            handleResponse<List<OrganizationPlan>>(response)
        }

    /**
     * Current Subscription
     *
     * @see <a href="https://docs.turso.tech/api-reference/organizations/subscription">API Reference</a>
     */
    suspend fun subscription(organizationName: String): SubscriptionResponse =
        client.httpClient.get(
            Path.subscription(organizationName),
        ) {
            contentType(ContentType.Application.Json)
        }.let { response ->
            handleResponse<SubscriptionResponse>(response)
        }

    /**
     * List invoices
     *
     * @see <a href="https://docs.turso.tech/api-reference/organizations/invoices">API Reference</a>
     */
    suspend fun listInvoices(organizationName: String): InvoicesResponse =
        client.httpClient.get(
            Path.invoices(organizationName),
        ).let { response ->
            handleResponse<InvoicesResponse>(response)
        }

    internal object Path {
        private const val BASE_PATH = "/v1/organizations"

        fun organizations() = BASE_PATH

        fun organizations(organizationName: String) = "$BASE_PATH/$organizationName"

        fun plans(organizationName: String) = organizations(organizationName) + "/plans"

        fun subscription(organisationName: String) = organizations(organisationName) + "/subscription"

        fun invoices(organizationName: String) = organizations(organizationName) + "/invoices"
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
     * @see <a href="https://docs.turso.tech/api-reference/organizations/members/list">API Reference</a>
     */
    suspend fun list(organizationName: String): ListMembersResponse =
        client.httpClient.get(Path.members(organizationName)) {
            contentType(ContentType.Application.Json)
        }.let { response ->
            handleResponse<ListMembersResponse>(response)
        }

    /**
     * Adds a member to an organization
     *
     * @see <a href="https://docs.turso.tech/api-reference/organizations/members/add">API Reference</a>
     */
    suspend fun add(
        organizationName: String,
        member: CreateMember,
    ): CreateMemberResponse =
        client.httpClient.post(Path.members(organizationName)) {
            contentType(ContentType.Application.Json)
            setBody(member)
        }.let { response ->
            handleResponse<CreateMemberResponse>(response)
        }

    /**
     * Removes a member from an organization
     *
     * @see <a href="https://docs.turso.tech/api-reference/organizations/members/remove">API Reference</a>
     */
    suspend fun remove(
        organizationName: String,
        username: String,
    ): DeleteMemberResponse =
        client.httpClient.delete(Path.members(organizationName, username)) {
            contentType(ContentType.Application.Json)
        }.let { response ->
            handleResponse<DeleteMemberResponse>(response)
        }

    internal object Path {
        fun members(organizationName: String) = "${Organizations.Path.organizations()}/$organizationName/members"

        fun members(
            organizationName: String,
            memberName: String,
        ) = "${members(organizationName)}/$memberName"
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
     * @see <a href="https://docs.turso.tech/api-reference/organizations/invites/list">API Reference</a>
     */
    suspend fun list(organizationName: String): ListInvitesResponse =
        client.httpClient.get(Path.invites(organizationName)) {
            contentType(ContentType.Application.Json)
        }.let { response ->
            handleResponse<ListInvitesResponse>(response)
        }

    /**
     * Creates the user invitation to an organization
     *
     * @see <a href="https://docs.turso.tech/api-reference/organizations/invites/create">API Reference</a>
     */
    suspend fun create(
        organizationName: String,
        invite: CreateMember,
    ): CreateInviteResponse =
        client.httpClient.post(Path.invites(organizationName)) {
            contentType(ContentType.Application.Json)
            setBody(invite)
        }.let { response ->
            handleResponse<CreateInviteResponse>(response)
        }

    internal object Path {
        fun invites(organizationName: String) = "${Organizations.Path.organizations()}/$organizationName/invites"
    }
}
