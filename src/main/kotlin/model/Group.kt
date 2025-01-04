/*
 * Copyright 2024 Jeliuc.com S.R.L. and Turso SDK contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
 */

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
    @SerialName("version") val version: String,
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

    @SerialName("vec")
    VEC,
}

@Serializable
data class TransferGroupRequest(
    @SerialName("organization") val organization: String,
)
