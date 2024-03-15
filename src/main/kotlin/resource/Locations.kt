/*
 * Copyright 2024 Jeliuc.com S.R.L. and Turso SDK contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
 */

package com.jeliuc.turso.sdk.resource

import com.jeliuc.turso.sdk.TursoClient
import com.jeliuc.turso.sdk.model.ListLocationsResponse
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
class Locations(private val client: TursoClient) : ResponseHandler() {
    /**
     * Lists locations
     *
     * @see <a href="https://docs.turso.tech/api-reference/locations/list">API Reference</a>
     */
    suspend fun list() =
        this.client.httpClient.get(Path.locations()) {
            contentType(ContentType.Application.Json)
        }.let {
            handleResponse<ListLocationsResponse>(it)
        }

    object Path {
        fun locations() = "/v1/locations"
    }
}
