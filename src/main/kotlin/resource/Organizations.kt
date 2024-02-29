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
import com.jeliuc.turso.sdk.resources.ResponseHandler
import com.jeliuc.turso.sdk.resources.handleResponse
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.patch
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType

val TursoClient.organizations: Organizations
    get() = Organizations(this)

class Organizations(private val client: TursoClient) : ResponseHandler {
    val members: Members
        get() = Members(this.client)

    val invites: Invites
        get() = Invites(this.client)

    suspend fun list() =
        client.httpClient.get(Resources.basePath()) {
            contentType(ContentType.Application.Json)
        }.let { response ->
            handleResponse<List<Organization>>(response)
        }

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

class Members(private val client: TursoClient) : ResponseHandler {
    suspend fun list(organizationName: String) =
        client.httpClient.get(Resources.basePath(organizationName)) {
            contentType(ContentType.Application.Json)
        }.let { response ->
            handleResponse<ListMembersResponse>(response)
        }

    suspend fun add(
        organizationName: String,
        member: CreateMember,
    ) = client.httpClient.post(Resources.basePath(organizationName)) {
        contentType(ContentType.Application.Json)
        setBody(member)
    }.let { response ->
        handleResponse<CreateMemberResponse>(response)
    }

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

class Invites(private val client: TursoClient) : ResponseHandler {
    suspend fun list(organizationName: String) =
        client.httpClient.get(Resources.basePath(organizationName)) {
            contentType(ContentType.Application.Json)
        }.let { response ->
            handleResponse<ListInvitesResponse>(response)
        }

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
