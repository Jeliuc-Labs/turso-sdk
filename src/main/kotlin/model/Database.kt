package com.jeliuc.turso.sdk.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Database
 */
@Serializable
data class Database(
    @SerialName("Name") val name: String,
    @SerialName("DbId") val dbId: String,
    @SerialName("Hostname") val hostname: String,
    @SerialName("regions") val regions: List<String>,
    @SerialName("primaryRegion") val primaryRegion: String,
    @SerialName("type") val type: String,
    @SerialName("version") val version: String,
    @SerialName("group") val group: String,
    @SerialName("sleeping") val sleeping: Boolean,
)

/**
 * CreateDatabase
 */
@Serializable
data class CreateDatabase(
    @SerialName("name") val name: String,
    @SerialName("group") val group: String,
    @SerialName("size_limit") val sizeLimit: String? = null,
    @SerialName("seed") val seed: Seed? = null,
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

/**
 * Created Database
 */
@Serializable
data class CreatedDatabase(
    @SerialName("DbId") val dbId: String,
    @SerialName("Name") val name: String,
    @SerialName("Hostname") val hostname: String,
    @SerialName("IssuedCertCount") val issuedCertCount: Int,
    @SerialName("IssuedCertLimit") val issuedCertLimit: Int,
)

/**
 * CreateDatabaseResponse
 *
 * Represents the result of creating a database.
 */
@Serializable
data class CreateDatabaseResponse(
    @SerialName("database") val database: CreatedDatabase,
    @SerialName("password") val password: String,
    @SerialName("username") val username: String,
)

/**
 * ListDatabasesResponse
 *
 * Represents the result of listing databases.
 */
@Serializable
data class ListDatabasesResponse(
    @SerialName("databases") val databases: List<Database>,
)

/**
 * RetrieveDatabaseResponse
 *
 * Represents the result of retrieving a database.
 */
@Serializable
data class RetrieveDatabaseResponse(
    @SerialName("database") val database: Database,
)

/**
 * Usage
 *
 * Represents the usage of a database, instance or total.
 */
@Serializable
data class Usage(
    @SerialName("rows_read") val rowsRead: Int,
    @SerialName("rows_written") val rowsWritten: Int,
    @SerialName("storage_bytes") val storageBytes: Int,
)

/**
 * InstanceUsage
 *
 * Represents the usage of an instance.
 */
@Serializable
data class InstanceUsage(
    @SerialName("uuid") val uuid: String,
    @SerialName("usage") val usage: Usage,
)

/**
 * Instances
 *
 * Represents the usage of a database, instance or total.
 */
@Serializable
data class Instances<T>(
    @SerialName("uuid") val uuid: String,
    @SerialName("instances") val instances: List<T>,
    @SerialName("usage") val usage: Usage,
)

/**
 * Instance
 *
 * Represents a database instance.
 */
@Serializable
data class Instance(
    @SerialName("uuid") val uuid: String,
    @SerialName("name") val name: String,
    @SerialName("type") val type: String,
    @SerialName("region") val region: String,
    @SerialName("hostname") val hostname: String,
)

/**
 * DatabaseUsageResponse
 *
 * Represents the result of retrieving the usage of a database.
 */
@Serializable
data class DatabaseUsageResponse(
    @SerialName("database") val database: Instances<InstanceUsage>,
    @SerialName("instances") val instances: Map<String, Usage>,
    @SerialName("total") val total: Usage,
)

@Serializable
data class DatabaseDeleteResponse(
    @SerialName("database") val database: String,
)

/**
 * ListInstancesResponse
 *
 * Represents the result of instances.
 *
 * @property instances The list of instances.
 */
@Serializable
data class ListInstancesResponse(
    @SerialName("instances") val instances: List<Instance>,
)

/**
 * InstanceResponse
 *
 * Represents the result of retrieving an instance.
 *
 * @property instance The retrieved instance.
 */
@Serializable
data class InstanceResponse(
    @SerialName("instance") val instance: Instance,
)

@Serializable
data class StatsResponse(
    @SerialName("top_queries") val topQueries: List<QueryStatistics>,
)

@Serializable
data class QueryStatistics(
    @SerialName("query") val query: String,
    @SerialName("rows_read") val rowsRead: Long,
    @SerialName("rows_written") val rowsWritten: Long,
)

/**
 * UploadDumpResponse
 *
 * Represents the result of uploading a dump.
 */
@Serializable
data class UploadDumpResponse(
    @SerialName("dump_url") val dumpUrl: String,
)
