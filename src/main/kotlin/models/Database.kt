package com.jeliuc.turso.sdk.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Database
 *
 * @property name The database name, unique across your organization.
 * @property dbId The database universal unique identifier (UUID).
 * @property hostname The DNS hostname used for client libSQL and HTTP connections.
 * @property regions A list of regions for the group the database belongs to.
 * @property primaryRegion The primary region for the group the database belongs to.
 * @property type The object type. Default logical.
 * @property version The current libSQL version the database is running.
 * @property group The group the database belongs to.
 * @property sleeping Whether the database is sleeping.
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
 *
 * @property name The database name, unique across your organization.
 * @property group The group the database belongs to.
 */
@Serializable
class CreateDatabase(val name: String, val group: String)

/**
 * CreatedDatabase
 *
 * @property dbId The database universal unique identifier (UUID).
 * @property name The database name, unique across your organization.
 * @property hostname The DNS hostname used for client libSQL and HTTP connections.
 * @property issuedCertCount The number of certificates issued for the database.
 * @property issuedCertLimit The maximum number of certificates that can be issued for the database.
 */
@Serializable
class CreatedDatabase(
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
 *
 * @property database The created database.
 * @property password The password for the database.
 * @property username The username for the database.
 */
@Serializable
class CreateDatabaseResponse(
    @SerialName("database") val database: CreatedDatabase,
    @SerialName("password") val password: String,
    @SerialName("username") val username: String,
)

/**
 * ListDatabasesResponse
 *
 * Represents the result of listing databases.
 *
 * @property databases The list of databases.
 */
@Serializable
data class ListDatabasesResponse(
    @SerialName("databases") val databases: List<Database>,
)

/**
 * RetrieveDatabaseResponse
 *
 * Represents the result of retrieving a database.
 *
 * @property database The retrieved database.
 */
@Serializable
data class RetrieveDatabaseResponse(
    @SerialName("database") val database: Database,
)

/**
 * Usage
 *
 * Represents the usage of a database, instance or total.
 *
 * @property rowsRead The number of rows read.
 * @property rowsWritten The number of rows written.
 * @property storageBytes The number of storage bytes.
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
 *
 * @property uuid The instance universal unique identifier (UUID).
 * @property usage The usage of the instance.
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
 *
 * @property uuid The database universal unique identifier (UUID).
 * @property instances The list of instances usage statistics.
 * @property usage The overall usage of the database.
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
 *
 * @property uuid The instance universal unique identifier (UUID).
 * @property name The instance name.
 * @property type The instance type.
 * @property region The instance region.
 * @property hostname The instance hostname.
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
 *
 * @property database The usage of the database.
 * @property instances The usage of the database instances.
 * @property total The overall usage of the database.
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
 * CreateTokenResponse
 *
 * Represents the result of creating a token.
 */
@Serializable
data class CreateTokenResponse(
    @SerialName("jwt") val jwt: String,
)

/**
 * UploadDumpResponse
 *
 * Represents the result of uploading a dump.
 *
 * @property dumpUrl The URL of the uploaded dump.
 */
@Serializable
data class UploadDumpResponse(
    @SerialName("dump_url") val dumpUrl: String,
)

/**
 * DatabaseAuthorization
 *
 * The authorization level for a database.
 *
 * @property value The value of the authorization level.
 */
enum class DatabaseAuthorization(val value: String) {
    FULL_ACCESS("full-access"),

    @Suppress("unused")
    READ_ONLY("read-only"),
}
