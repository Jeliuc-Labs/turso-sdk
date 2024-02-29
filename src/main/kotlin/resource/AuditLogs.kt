package com.jeliuc.turso.sdk.resource

import com.jeliuc.turso.sdk.TursoClient
import com.jeliuc.turso.sdk.model.ListAuditLogsResponse
import com.jeliuc.turso.sdk.resources.ResponseHandler
import com.jeliuc.turso.sdk.resources.handleResponse
import io.ktor.client.request.get
import io.ktor.http.ContentType
import io.ktor.http.contentType

val TursoClient.auditLogs: AuditLogs
    get() = AuditLogs(this)

/**
 * Turso AuditLogs API Resource
 *
 * [See official documentation page](https://docs.turso.tech/api-reference/audit-logs)
 *
 * Example usage:
 *
 * ```kotlin
 * // client: TursoClient
 * val auditLogs = client.auditLogs.list("organizationName")
 * ```
 */
class AuditLogs(private val client: TursoClient) : ResponseHandler {
    /**
     * Lists audit logs
     *
     * [See official documentation page](https://docs.turso.tech/api-reference/audit-logs/list).
     *
     * @throws [com.jeliuc.turso.sdk.models.ApiError]
     * @throws [com.jeliuc.turso.sdk.models.UnexpectedResultError]
     */
    suspend fun list(organization: String) =
        this.client.httpClient.get(Resources.listPath(organization)) {
            contentType(ContentType.Application.Json)
        }.let { response ->
            handleResponse<ListAuditLogsResponse>(response)
        }

    internal object Resources {
        fun listPath(organization: String) = "/v1/organizations/$organization/audit-logs"
    }
}
