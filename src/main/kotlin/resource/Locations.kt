package com.jeliuc.turso.sdk.resource

import com.jeliuc.turso.sdk.TursoClient
import com.jeliuc.turso.sdk.model.ListLocationsResponse
import com.jeliuc.turso.sdk.resources.ResponseHandler
import com.jeliuc.turso.sdk.resources.handleResponse
import io.ktor.client.request.get
import io.ktor.http.ContentType
import io.ktor.http.contentType

val TursoClient.locations: Locations
    get() = Locations(this)

/**
 * Locations resource
 *
 * Example usage:
 *
 * ```kotlin
 * val locationsResponse = client.locations.list()
 * ```
 */
class Locations(private val client: TursoClient) : ResponseHandler {
    /**
     * Lists locations
     *
     * @throws [com.jeliuc.turso.sdk.models.ApiError]
     * @throws [com.jeliuc.turso.sdk.models.UnexpectedResultError]
     */
    suspend fun list() =
        this.client.httpClient.get(Resources.listPath()) {
            contentType(ContentType.Application.Json)
        }.let {
            handleResponse<ListLocationsResponse>(it)
        }

    object Resources {
        fun listPath() = "/v1/locations"
    }
}
