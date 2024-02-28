package com.jeliuc.turso.sdk.model

import com.jeliuc.turso.sdk.serializer.LocalDateTimeSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement
import java.time.LocalDateTime

@Serializable
data class ListAuditLogsResponse(
    @SerialName("audit_logs") val auditLogs: List<AuditLog>,
    @SerialName("pagination") val pagination: Pagination,
)

@Serializable
data class AuditLog(
    @SerialName("author") val author: String,
    @SerialName("code") val code: String,
    @SerialName("created_at") @Serializable(with = LocalDateTimeSerializer::class) val createdAt: LocalDateTime,
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
