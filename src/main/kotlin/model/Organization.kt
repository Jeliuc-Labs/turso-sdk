/*
 * Copyright 2024 Jeliuc.com S.R.L. and Turso SDK contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
 */

package com.jeliuc.turso.sdk.model

import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

@Serializable
data class Organization(
    @SerialName("name") val name: String,
    @SerialName("slug") val slug: String,
    @SerialName("type") val type: String,
    @SerialName("blocked_reads") val blockedReads: Boolean,
    @SerialName("blocked_writes") val blockedWrites: Boolean,
    @SerialName("overages") val overages: Boolean,
    @SerialName("plan_id") val planId: String? = null,
    @SerialName("plan_timeline") val planTimeline: String? = null,
    @SerialName("platform") val platform: String? = null,
)

@Serializable
data class OrganizationResponse(
    @SerialName("organization") val organization: Organization,
)

@Suppress("unused")
@Serializable
enum class OrganizationType {
    @SerialName("personal")
    PERSONAL,

    @SerialName("team")
    TEAM,
}

@Serializable
data class UpdateOrganizationRequest(
    @SerialName("overages") val overages: Boolean,
)

@Serializable
data class OrganizationPlan(
    @SerialName("name") val name: String,
    @SerialName("price") val price: String,
    @SerialName("quotas") val quotas: PlanQuotas,
)

@Serializable
data class PlanQuotas(
    @SerialName("rowsRead") val rowRead: Long,
    @SerialName("rowsWritten") val rowsWritten: Long,
    @SerialName("databases") val databases: Int,
    @SerialName("locations") val locations: Int,
    @SerialName("storage") val storage: Long,
    @SerialName("groups") val groups: Int,
    @SerialName("bytesSynced") val bytesSynced: Long,
)

@Serializable
data class Subscription(
    @SerialName("name") val name: String,
    @SerialName("overages") val overages: Boolean,
    @SerialName("plan") val plan: String,
    @SerialName("timeline") val timeline: String,
)

@Serializable
data class SubscriptionResponse(
    @SerialName("subscription") val subscription: Subscription,
)

@Serializable
data class InvoicesResponse(
    @SerialName("invoices") val invoices: List<Invoice>,
)

@Serializable
data class Invoice(
    @SerialName("invoice_number") val invoiceNumber: String,
    @SerialName("amount_due") val amountDue: String,
    @SerialName("due_date") val dueDate: String,
    @SerialName("paid_at") val paidAt: String,
    @SerialName("payment_failed_at") val paymentFailedAt: String,
    @SerialName("invoice_pdf") val invoicePdf: String,
)

@Serializable
data class OrganizationDatabaseUsageResponse(
    @SerialName("organization") val organizationUsageResponse: OrganizationDatabaseUsage,
)

@Serializable
data class OrganizationDatabaseUsage(
    @SerialName("uuid") val uuid: String,
    @SerialName("usage") val usage: OrganizationUsage,
    @SerialName("databases") val databases: List<DatabaseUsage>,
)

@Serializable
data class DatabaseUsage(
    @SerialName("uuid") val uuid: String,
    @SerialName("instances") val instances: List<InstanceUsage>,
)

@Serializable
data class OrganizationUsage(
    @SerialName("rows_read") val rowsRead: Long,
    @SerialName("rows_written") val rowsWritten: Long,
    @SerialName("databases") val databases: Int,
    @SerialName("locations") val locations: Int,
    @SerialName("groups") val groups: Int,
    @SerialName("bytes_synced") val bytesSynced: Long,
    @SerialName("storage_bytes") val storage: Long,
)

@Serializable
data class ListMembersResponse(
    @SerialName("members") val members: List<Member>,
)

@Serializable
data class MemberResponse(
    @SerialName("member") val member: Member,
)

@Serializable
data class Member(
    @SerialName("email") val email: String,
    @SerialName("role") val role: MemberRole,
    @SerialName("username") val username: String,
)

@Serializable
enum class MemberRole {
    @SerialName("owner")
    OWNER,

    @SerialName("admin")
    ADMIN,

    @Suppress("unused")
    @SerialName("member")
    MEMBER,
}

@Serializable
data class CreateMember(
    @SerialName("username") val username: String,
    @SerialName("role") val role: MemberRole,
) {
    init {
        require(MemberRole.OWNER != role) { "Owner role is not allowed" }
    }
}

@Serializable
data class CreateMemberResponse(
    @SerialName("member") val member: String,
    @SerialName("role") val role: MemberRole,
)

@Serializable
data class UpdateMemberRequest(
    @SerialName("role") val role: String,
)

@Serializable
data class UpdateMemberResponse(
    @SerialName("member") val member: UpdateMemberResponseInner,
)

@Serializable
data class UpdateMemberResponseInner(
    @SerialName("username") val username: String,
    @SerialName("email") val email: String,
    @SerialName("role") val role: String,
)

@Serializable
data class DeleteMemberResponse(
    @SerialName("member") val member: String,
)

@Serializable
data class ListInvitesResponse(
    @SerialName("invites") val invites: List<Invite>,
)

@Serializable
data class Invite(
    @SerialName("ID") val id: Int,
    @SerialName("CreatedAt") val createdAt: Instant,
    @SerialName("UpdatedAt") val updatedAt: Instant,
    @SerialName("DeletedAt") val deletedAt: Instant,
    @SerialName("Role") val role: MemberRole,
    @SerialName("Email") val email: String,
    @SerialName("OrganizationID") val organizationID: Int,
    @SerialName("Token") val token: String,
    @SerialName("Organization") val organization: Organization,
    @SerialName("Accepted") val accepted: Boolean,
)

@Serializable
data class CreateInviteResponse(
    @SerialName("invited") val invited: Invite,
)

@Serializable
data class ListAuditLogsResponse(
    @SerialName("audit_logs") val auditLogs: List<AuditLog>,
    @SerialName("pagination") val pagination: Pagination,
)

@Serializable
data class AuditLog(
    @SerialName("author") val author: String,
    @SerialName("code") val code: String,
    @SerialName("created_at") val createdAt: Instant,
    @SerialName("data") val data: JsonElement,
    @SerialName("message") val message: String,
    @SerialName("origin") val origin: String,
)

@Serializable
data class Pagination(
    @SerialName("page") val page: Int,
    @SerialName("page_size") val pageSize: Int,
    @SerialName("total_pages") val totalPages: Int,
    @SerialName("total_rows") val totalRows: Int,
)
