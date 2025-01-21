/*
 * Copyright 2024 Jeliuc.com S.R.L. and Turso SDK contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
 */

package com.jeliuc.turso.sdk.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Database(
    @SerialName("Name") val name: String,
    @SerialName("DbId") val dbId: String,
    @SerialName("Hostname") val hostname: String,
    @SerialName("is_schema") val isSchema: Boolean,
    @SerialName("block_reads") val blockReads: Boolean,
    @SerialName("block_writes") val blockWrites: Boolean,
    @SerialName("allow_attach") val allowAttach: Boolean,
    @SerialName("regions") val regions: List<String>,
    @SerialName("primaryRegion") val primaryRegion: String,
    @SerialName("type") val type: String,
    @SerialName("version") val version: String,
    @SerialName("group") val group: String,
    @SerialName("sleeping") val sleeping: Boolean,
    @SerialName("schema") val schema: String?,
    @SerialName("archived") val archived: Boolean,
)

@Serializable
data class CreateDatabase(
    @SerialName("name") val name: String,
    @SerialName("group") val group: String,
    @SerialName("is_schema") val isSchema: Boolean = false,
    @SerialName("schema") val schema: String? = null,
    @SerialName("seed") val seed: Seed? = null,
    @SerialName("size_limit") val sizeLimit: String? = null,
)

@Serializable
sealed class Seed

@Serializable
@SerialName("database")
data class DatabaseSeed(
    @SerialName("name") val name: String,
    @SerialName("timestamp") val timestamp: String?,
) : Seed()

@Suppress("unused")
@Serializable
@SerialName("dump")
data class DumpSeed(
    @SerialName("url") val url: String,
) : Seed()

@Serializable
data class CreatedDatabase(
    @SerialName("DbId") val dbId: String,
    @SerialName("Name") val name: String,
    @SerialName("Hostname") val hostname: String,
    @SerialName("IssuedCertCount") val issuedCertCount: Int,
    @SerialName("IssuedCertLimit") val issuedCertLimit: Int,
)

@Serializable
data class CreateDatabaseResponse(
    @SerialName("database") val database: CreatedDatabase,
    @SerialName("password") val password: String,
    @SerialName("username") val username: String,
)

@Serializable
data class ListDatabasesResponse(
    @SerialName("databases") val databases: List<Database>,
)

@Serializable
data class RetrieveDatabaseResponse(
    @SerialName("database") val database: Database,
)

@Serializable
data class ConfigurationResponse(
    @SerialName("size_limit") val sizeLimit: String,
    @SerialName("allow_attach") val allowAttach: Boolean,
    @SerialName("block_reads") val blockReads: Boolean,
    @SerialName("block_writes") val blockWrites: Boolean,
)

@Serializable
data class UpdateConfigurationRequest(
    @SerialName("block_reads") val blockReads: Boolean,
    @SerialName("block_writes") val blockWrites: Boolean,
    @SerialName("size_limit") val sizeLimit: String,
    @SerialName("allow_attach") val allowAttach: Boolean,
)

@Serializable
data class Usage(
    @SerialName("rows_read") val rowsRead: Long,
    @SerialName("rows_written") val rowsWritten: Long,
    @SerialName("storage_bytes") val storageBytes: Long,
    @SerialName("bytes_synced") val bytesSynced: Long,
)

@Serializable
data class InstanceUsage(
    @SerialName("uuid") val uuid: String,
    @SerialName("usage") val usage: Usage,
)

@Serializable
data class Instances<T>(
    @SerialName("uuid") val uuid: String,
    @SerialName("instances") val instances: List<T>,
    @SerialName("usage") val usage: InstanceUsage,
)

@Serializable
data class Instance(
    @SerialName("uuid") val uuid: String,
    @SerialName("name") val name: String,
    @SerialName("type") val type: String,
    @SerialName("region") val region: String,
    @SerialName("hostname") val hostname: String,
)

@Serializable
data class DatabaseUsageResponse(
    // @SerialName("database") val database: Instances<InstanceUsage>,
    @SerialName("total") val total: Usage,
)

@Serializable
data class DeleteDatabaseResponse(
    @SerialName("database") val database: String,
)

@Serializable
data class ListInstancesResponse(
    @SerialName("instances") val instances: List<Instance>,
)

@Serializable
data class InstanceResponse(
    @SerialName("instance") val instance: Instance,
)

@Serializable
data class StatsResponse(
    @SerialName("top_queries") val topQueries: List<QueryStatistics>? = null,
)

@Serializable
data class QueryStatistics(
    @SerialName("query") val query: String,
    @SerialName("rows_read") val rowsRead: Long,
    @SerialName("rows_written") val rowsWritten: Long,
)

@Serializable
data class UploadDumpResponse(
    @SerialName("dump_url") val dumpUrl: String,
)
