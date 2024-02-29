package com.jeliuc.turso.sdk.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreateApiTokenResponse(
    @SerialName("id") val id: String,
    @SerialName("name") val name: String,
    @SerialName("token") val token: String,
)

@Serializable
data class ValidateTokenResponse(
    @SerialName("exp") val exp: Int,
)

@Serializable
data class ListApiTokensResponse(
    @SerialName("tokens") val tokens: List<ApiToken>,
)

@Serializable
data class ApiToken(
    @SerialName("id") val id: String,
    @SerialName("name") val name: String,
)

@Serializable
data class RevokeApiTokenResponse(
    @SerialName("token") val token: String,
)
