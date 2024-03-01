/*
 * Copyright 2024 Jeliuc.com S.R.L. and Turso SDK contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
 */

package com.jeliuc.turso.sdk.resource

import com.jeliuc.turso.sdk.Fixture
import com.jeliuc.turso.sdk.client
import com.jeliuc.turso.sdk.model.CreateGroup
import com.jeliuc.turso.sdk.model.GroupResponse
import com.jeliuc.turso.sdk.model.ListGroupsResponse
import com.jeliuc.turso.sdk.model.TokenResponse
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.http.ContentType
import io.ktor.http.HttpMethod
import io.ktor.http.headersOf
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertIs

private fun mockEngine() =
    MockEngine { request ->
        val url = request.url
        val method = request.method
        val fixturesBasePath = "/fixtures/group"

        when (url.encodedPath) {
            Groups.Resources.basePath("test") -> {
                when (method) {
                    HttpMethod.Get -> {
                        respond(
                            Fixture.content("$fixturesBasePath/list.json"),
                            headers = headersOf("Content-Type" to listOf(ContentType.Application.Json.toString())),
                        )
                    }

                    HttpMethod.Post -> {
                        respond(
                            Fixture.content("$fixturesBasePath/group.json"),
                            headers = headersOf("Content-Type" to listOf(ContentType.Application.Json.toString())),
                        )
                    }

                    else -> error("Unhandled ${method.value} ${url.encodedPath}")
                }
            }

            Groups.Resources.groupPath("test", "test-group") -> {
                when (method) {
                    HttpMethod.Get -> {
                        respond(
                            Fixture.content("$fixturesBasePath/group.json"),
                            headers = headersOf("Content-Type" to listOf(ContentType.Application.Json.toString())),
                        )
                    }

                    HttpMethod.Delete -> {
                        respond(
                            Fixture.content("$fixturesBasePath/group.json"),
                            headers = headersOf("Content-Type" to listOf(ContentType.Application.Json.toString())),
                        )
                    }

                    else -> error("Unhandled ${method.value} ${url.encodedPath}")
                }
            }

            Groups.Resources.transferPath("from-org", "test-group") -> {
                when (method) {
                    HttpMethod.Post -> {
                        respond(
                            Fixture.content("$fixturesBasePath/group.json"),
                            headers = headersOf("Content-Type" to listOf(ContentType.Application.Json.toString())),
                        )
                    }

                    else -> error("Unhandled ${method.value} ${url.encodedPath}")
                }
            }

            Groups.Resources.locationPath("test", "test-group", "ams") -> {
                when (method) {
                    HttpMethod.Post -> {
                        respond(
                            Fixture.content("$fixturesBasePath/group.json"),
                            headers = headersOf("Content-Type" to listOf(ContentType.Application.Json.toString())),
                        )
                    }

                    HttpMethod.Delete -> {
                        respond(
                            Fixture.content("$fixturesBasePath/group.json"),
                            headers = headersOf("Content-Type" to listOf(ContentType.Application.Json.toString())),
                        )
                    }

                    else -> error("Unhandled ${method.value} ${url.encodedPath}")
                }
            }

            Groups.Resources.updatePath("test", "test-group") -> {
                when (method) {
                    HttpMethod.Post -> {
                        respond(
                            "",
                            headers = headersOf("Content-Type" to listOf(ContentType.Application.Json.toString())),
                        )
                    }

                    else -> error("Unhandled ${method.value} ${url.encodedPath}")
                }
            }

            Groups.Resources.tokenPath("test", "test-group") -> {
                when (method) {
                    HttpMethod.Post -> {
                        respond(
                            Fixture.content("$fixturesBasePath/token.json"),
                            headers = headersOf("Content-Type" to listOf(ContentType.Application.Json.toString())),
                        )
                    }

                    else -> error("Unhandled ${method.value} ${url.encodedPath}")
                }
            }

            Groups.Resources.authRotatePath("test", "test-group") -> {
                when (method) {
                    HttpMethod.Post -> {
                        respond(
                            "",
                            headers = headersOf("Content-Type" to listOf(ContentType.Application.Json.toString())),
                        )
                    }

                    else -> error("Unhandled ${method.value} ${url.encodedPath}")
                }
            }

            else -> error("Unhandled ${method.value} ${url.encodedPath}")
        }
    }

class GroupsTest {
    @Test
    fun `can list groups`() {
        runBlocking {
            val response = client(mockEngine()).groups.list("test")
            assertIs<ListGroupsResponse>(response)
        }
    }

    @Test
    fun `can retrieve a group`() {
        runBlocking {
            val response = client(mockEngine()).groups.retrieve("test", "test-group")
            assertIs<GroupResponse>(response)
        }
    }

    @Test
    fun `can create a group`() {
        runBlocking {
            val response = client(mockEngine()).groups.create("test", CreateGroup("test-group", "otp"))
            assertIs<GroupResponse>(response)
        }
    }

    @Test
    fun `can delete a group`() {
        runBlocking {
            val response = client(mockEngine()).groups.delete("test", "test-group")
            assertIs<GroupResponse>(response)
        }
    }

    @Test
    fun `can transfer the group to another organization`() {
        runBlocking {
            val response = client(mockEngine()).groups.transfer("test-group", "from-org", "to-org")
            assertIs<GroupResponse>(response)
        }
    }

    @Test
    fun `can add location to a group`() {
        runBlocking {
            val response = client(mockEngine()).groups.addLocation("test", "test-group", "ams")
            assertIs<GroupResponse>(response)
        }
    }

    @Test
    fun `can remove a location`() {
        runBlocking {
            val response = client(mockEngine()).groups.removeLocation("test", "test-group", "ams")
            assertIs<GroupResponse>(response)
        }
    }

    @Test
    fun `can update the version of libSql to all group members`() {
        runBlocking {
            val response = client(mockEngine()).groups.updateVersion("test", "test-group")
            assertIs<Unit>(response)
        }
    }

    @Test
    fun `can create auth token`() {
        runBlocking {
            val response = client(mockEngine()).groups.createToken("test", "test-group")
            assertIs<TokenResponse>(response)
        }
    }

    @Test
    fun `can invalidate group tokens`() {
        runBlocking {
            val response = client(mockEngine()).groups.invalidateTokens("test", "test-group")
            assertIs<Unit>(response)
        }
    }
}
