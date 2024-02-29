package com.jeliuc.turso.sdk.resource

import com.jeliuc.turso.sdk.Fixture
import com.jeliuc.turso.sdk.client
import com.jeliuc.turso.sdk.model.CreateDatabase
import com.jeliuc.turso.sdk.model.CreateDatabaseResponse
import com.jeliuc.turso.sdk.model.DatabaseDeleteResponse
import com.jeliuc.turso.sdk.model.DatabaseUsageResponse
import com.jeliuc.turso.sdk.model.InstanceResponse
import com.jeliuc.turso.sdk.model.ListDatabasesResponse
import com.jeliuc.turso.sdk.model.ListInstancesResponse
import com.jeliuc.turso.sdk.model.RetrieveDatabaseResponse
import com.jeliuc.turso.sdk.model.StatsResponse
import com.jeliuc.turso.sdk.model.TokenResponse
import com.jeliuc.turso.sdk.model.UploadDumpResponse
import com.jeliuc.turso.sdk.resources.Databases
import com.jeliuc.turso.sdk.resources.databases
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.http.ContentType
import io.ktor.http.HttpMethod
import io.ktor.http.headersOf
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertIs

private fun mockEngine(): HttpClientEngine =
    MockEngine { request ->
        val url = request.url
        val method = request.method
        val fixturesBasePath = "/fixtures/database"

        when (url.encodedPath) {
            Databases.Resources.organizationPath("test") -> {
                when (method) {
                    HttpMethod.Get -> {
                        val data = Fixture().content("$fixturesBasePath/list.json")
                        respond(data, headers = headersOf("Content-Type" to listOf(ContentType.Application.Json.toString())))
                    }
                    HttpMethod.Post -> {
                        val data = Fixture().content("$fixturesBasePath/create.json")
                        respond(data, headers = headersOf("Content-Type" to listOf(ContentType.Application.Json.toString())))
                    }
                    else -> error("Unhandled ${method.value} ${url.encodedPath}")
                }
            }
            Databases.Resources.databasePath("test", "test-database") -> {
                when (method) {
                    HttpMethod.Get -> {
                        val data = Fixture().content("$fixturesBasePath/retrieve.json")
                        respond(
                            data,
                            headers = headersOf("Content-Type" to listOf(ContentType.Application.Json.toString())),
                        )
                    }
                    HttpMethod.Delete -> {
                        val data = Fixture().content("$fixturesBasePath/delete.json")
                        respond(
                            data,
                            headers = headersOf("Content-Type" to listOf(ContentType.Application.Json.toString())),
                        )
                    }
                    else -> error("Unhandled ${method.value} ${url.encodedPath}")
                }
            }
            Databases.Resources.databaseUsagePath("test", "test-database") -> {
                val data = Fixture().content("$fixturesBasePath/usage.json")
                respond(data, headers = headersOf("Content-Type" to listOf(ContentType.Application.Json.toString())))
            }
            Databases.Resources.baseInstancesPath("test", "test-database") -> {
                val data = Fixture().content("$fixturesBasePath/list_instances.json")
                respond(
                    data,
                    headers = headersOf("Content-Type" to listOf(ContentType.Application.Json.toString())),
                )
            }
            Databases.Resources.instancePath("test", "test-database", "test-instance") -> {
                val data = Fixture().content("$fixturesBasePath/retrieve_instance.json")
                respond(
                    data,
                    headers = headersOf("Content-Type" to listOf(ContentType.Application.Json.toString())),
                )
            }
            Databases.Resources.createTokenPath("test", "test-database") -> {
                when (method) {
                    HttpMethod.Post -> {
                        val data = Fixture().content("$fixturesBasePath/create_token.json")
                        respond(
                            data,
                            headers =
                                headersOf(
                                    "Content-Type" to listOf(ContentType.Application.Json.toString()),
                                ),
                        )
                    }

                    else -> error("Unhandled ${method.value} ${url.encodedPath}")
                }
            }

            Databases.Resources.invalidateAuthTokensPath("test", "test-database") -> {
                when (method) {
                    HttpMethod.Post -> {
                        respond(
                            "",
                            headers =
                                headersOf(
                                    "Content-Type" to listOf(ContentType.Application.Json.toString()),
                                ),
                        )
                    }

                    else -> error("Unhandled ${method.value} ${url.encodedPath}")
                }
            }

            Databases.Resources.uploadDumpPath("test") -> {
                when (method) {
                    HttpMethod.Post -> {
                        val data = Fixture().content("$fixturesBasePath/upload_dump.json")
                        respond(
                            data,
                            headers =
                                headersOf(
                                    "Content-Type" to listOf(ContentType.Application.Json.toString()),
                                ),
                        )
                    }

                    else -> error("Unhandled ${method.value} ${url.encodedPath}")
                }
            }

            Databases.Resources.databaseStats("test", "test-database") -> {
                when (method) {
                    HttpMethod.Get -> {
                        val data = Fixture().content("$fixturesBasePath/stats.json")
                        respond(
                            data,
                            headers =
                                headersOf(
                                    "Content-Type" to listOf(ContentType.Application.Json.toString()),
                                ),
                        )
                    }

                    else -> error("Unhandled ${method.value} ${url.encodedPath}")
                }
            }

            else -> error("Unhandled ${method.value} ${url.encodedPath}")
        }
    }

class DatabasesTest {
    @Test
    fun `can list databases`() {
        runBlocking {
            val databases: ListDatabasesResponse = client(mockEngine()).databases.list("test")
            assertIs<ListDatabasesResponse>(databases)
        }
    }

    @Test
    fun `can create a database`() {
        runBlocking {
            val newDatabase = client(mockEngine()).databases.create("test", CreateDatabase("test-database", "default"))
            assertIs<CreateDatabaseResponse>(newDatabase)
        }
    }

    @Test
    fun `can retrieve a database`() {
        runBlocking {
            val database = client(mockEngine()).databases.retrieve("test", "test-database")
            assertIs<RetrieveDatabaseResponse>(database)
        }
    }

    @Test
    fun `can get a database usage`() {
        runBlocking {
            val usage = client(mockEngine()).databases.usage("test", "test-database")
            assertIs<DatabaseUsageResponse>(usage)
        }
    }

    @Test
    fun `can delete a database`() {
        runBlocking {
            val response = client(mockEngine()).databases.delete("test", "test-database")
            assertIs<DatabaseDeleteResponse>(response)
        }
    }

    @Test
    fun `can list instances`() {
        runBlocking {
            val response = client(mockEngine()).databases.listInstances("test", "test-database")
            assertIs<ListInstancesResponse>(response)
        }
    }

    @Test
    fun `can retrieve an instance`() {
        runBlocking {
            val response = client(mockEngine()).databases.retrieveInstance("test", "test-database", "test-instance")
            assertIs<InstanceResponse>(response)
        }
    }

    @Test
    fun `can create a token`() {
        runBlocking {
            val response = client(mockEngine()).databases.createToken("test", "test-database")
            assertIs<TokenResponse>(response)
        }
    }

    @Test
    fun `can invalidate tokens`() {
        runBlocking {
            val response = client(mockEngine()).databases.invalidateTokens("test", "test-database")
            assertIs<Unit>(response)
        }
    }

    @Test
    fun `can upload a database dump`() {
        runBlocking {
            val response =
                client(
                    mockEngine(),
                ).databases.uploadDump("test", Fixture().file("/fixtures/database/test.sql"))
            assertIs<UploadDumpResponse>(response)
        }
    }

    @Test
    fun `can get database stats`() {
        runBlocking {
            val response = client(mockEngine()).databases.stats("test", "test-database")
            assertIs<StatsResponse>(response)
        }
    }
}
