/*
 * Copyright 2024 Jeliuc.com S.R.L. and Turso SDK contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
 */

package com.jeliuc.turso.sdk.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

enum class Authorization(val value: String) {
    FULL_ACCESS("full-access"),

    @Suppress("unused")
    READ_ONLY("read-only"),
}

@Serializable
data class ListLocationsResponse(
    @SerialName("locations") val locations: Map<String, String>,
)

@Serializable
data class TokenResponse(
    @SerialName("jwt") val jwt: String,
)
