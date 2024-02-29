package com.jeliuc.turso.sdk.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * CreateTokenResponse
 *
 * Represents the result of the token creation.
 */
@Serializable
data class TokenResponse(
    @SerialName("jwt") val jwt: String,
)
