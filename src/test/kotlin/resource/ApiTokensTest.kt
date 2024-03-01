package com.jeliuc.turso.sdk.resource

import com.jeliuc.turso.sdk.Fixture
import com.jeliuc.turso.sdk.client
import com.jeliuc.turso.sdk.model.CreateApiTokenResponse
import com.jeliuc.turso.sdk.model.ListApiTokensResponse
import com.jeliuc.turso.sdk.model.RevokeApiTokenResponse
import com.jeliuc.turso.sdk.model.ValidateTokenResponse
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
        val fixturesBasePath = "/fixtures/api_token"

        when (url.encodedPath) {
            ApiTokens.Resources.tokenPath("test-token") -> {
                when (method) {
                    HttpMethod.Post -> {
                        val data = Fixture.content("$fixturesBasePath/create.json")
                        respond(
                            data,
                            headers = headersOf("Content-Type" to listOf(ContentType.Application.Json.toString())),
                        )
                    }

                    HttpMethod.Delete -> {
                        val data = Fixture.content("$fixturesBasePath/revoke.json")
                        respond(
                            data,
                            headers = headersOf("Content-Type" to listOf(ContentType.Application.Json.toString())),
                        )
                    }

                    else -> error("Unhandled ${method.value} ${url.encodedPath}")
                }
            }

            ApiTokens.Resources.validatePath() -> {
                when (method) {
                    HttpMethod.Get -> {
                        val data = Fixture.content("$fixturesBasePath/validate.json")
                        respond(
                            data,
                            headers = headersOf("Content-Type" to listOf(ContentType.Application.Json.toString())),
                        )
                    }
                    else -> error("Unhandled ${method.value} ${url.encodedPath}")
                }
            }

            ApiTokens.Resources.basePath() -> {
                when (method) {
                    HttpMethod.Get -> {
                        val data = Fixture.content("$fixturesBasePath/list.json")
                        respond(
                            data,
                            headers = headersOf("Content-Type" to listOf(ContentType.Application.Json.toString())),
                        )
                    }
                    else -> error("Unhandled ${method.value} ${url.encodedPath}")
                }
            }

            else -> error("Unhandled ${url.encodedPath}")
        }
    }

class ApiTokensTest {
    @Test
    fun `can create an api token`() {
        runBlocking {
            val response = client(mockEngine()).apiTokens.create("test-token")
            assertIs<CreateApiTokenResponse>(response)
        }
    }

    @Test
    fun `can validate an api token`() {
        runBlocking {
            val response = client(mockEngine()).apiTokens.validate()
            assertIs<ValidateTokenResponse>(response)
        }
    }

    @Test
    fun `can list api tokens`() {
        runBlocking {
            val response = client(mockEngine()).apiTokens.list()
            assertIs<ListApiTokensResponse>(response)
        }
    }

    @Test
    fun `can revoke an api token`() {
        runBlocking {
            val response = client(mockEngine()).apiTokens.revoke("test-token")
            assertIs<RevokeApiTokenResponse>(response)
        }
    }
}
