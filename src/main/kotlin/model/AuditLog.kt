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
