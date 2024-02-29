package com.jeliuc.turso.sdk.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ListLocationsResponse(
    @SerialName("locations") val locations: Map<String, String>,
)
