package com.jeliuc.turso.sdk.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ListGroupsResponse(
    @SerialName("groups") val groups: List<Group>,
)

@Serializable
data class Group(
    @SerialName("uuid") val uuid: String,
    @SerialName("name") val name: String,
    @SerialName("primary") val primary: String,
    @SerialName("locations") val locations: List<String>,
    @SerialName("archived") val archived: Boolean,
)

@Serializable
data class GroupResponse(
    @SerialName("group") val group: Group,
)

@Serializable
data class CreateGroup(
    @SerialName("name") val name: String,
    @SerialName("location") val location: String,
    @SerialName("extensions") val extensions: List<LibSqlExtension>? = null,
)

@Suppress("unused")
@Serializable
enum class LibSqlExtension {
    @SerialName("vector")
    VECTOR,

    @SerialName("vss")
    VSS,

    @SerialName("crypto")
    CRYPTO,

    @SerialName("fuzzy")
    FUZZY,

    @SerialName("math")
    MATH,

    @SerialName("stats")
    STATS,

    @SerialName("text")
    TEXT,

    @SerialName("unicode")
    UNICODE,

    @SerialName("uuid")
    UUID,

    @SerialName("regexp")
    REGEXP,
}

@Serializable
data class TransferGroupRequest(
    @SerialName("organization") val organization: String,
)
