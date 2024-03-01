package com.jeliuc.turso.sdk.resource

import com.jeliuc.turso.sdk.Fixture
import com.jeliuc.turso.sdk.client
import com.jeliuc.turso.sdk.model.ListLocationsResponse
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.request.get
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
        val fixturesBasePath = "/fixtures/location"

        when (url.encodedPath) {
            Locations.Resources.listPath() -> {
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

class LocationsTest {
    @Test
    fun `can list locations`() {
        runBlocking {
            val response = client(mockEngine()).locations.list()
            assertIs<ListLocationsResponse>(response)
        }
    }
}
