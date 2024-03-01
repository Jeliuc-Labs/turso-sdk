package com.jeliuc.turso.sdk.resource

import com.jeliuc.turso.sdk.Fixture
import com.jeliuc.turso.sdk.client
import com.jeliuc.turso.sdk.model.CreateInviteResponse
import com.jeliuc.turso.sdk.model.CreateMember
import com.jeliuc.turso.sdk.model.CreateMemberResponse
import com.jeliuc.turso.sdk.model.DeleteMemberResponse
import com.jeliuc.turso.sdk.model.ListInvitesResponse
import com.jeliuc.turso.sdk.model.ListMembersResponse
import com.jeliuc.turso.sdk.model.MemberRole
import com.jeliuc.turso.sdk.model.Organization
import com.jeliuc.turso.sdk.model.OrganizationResponse
import com.jeliuc.turso.sdk.model.UpdateOrganizationRequest
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.http.ContentType
import io.ktor.http.HttpMethod
import io.ktor.http.headersOf
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.assertThrows
import kotlin.test.Test
import kotlin.test.assertIs

private fun mockEngine() =
    MockEngine { request ->
        val url = request.url
        val method = request.method
        val fixturesBasePath = "/fixtures/organization"

        when (url.encodedPath) {
            Organizations.Resources.basePath() -> {
                when (method) {
                    HttpMethod.Get -> {
                        respond(
                            Fixture.content("$fixturesBasePath/list.json"),
                            headers = headersOf("Content-Type" to listOf(ContentType.Application.Json.toString())),
                        )
                    }

                    else -> error("Unhandled ${method.value} ${url.encodedPath}")
                }
            }

            Organizations.Resources.organizationPath("test") -> {
                when (method) {
                    HttpMethod.Patch -> {
                        respond(
                            Fixture.content("$fixturesBasePath/organization.json"),
                            headers = headersOf("Content-Type" to listOf(ContentType.Application.Json.toString())),
                        )
                    }

                    else -> error("Unhandled ${method.value} ${url.encodedPath}")
                }
            }

            Members.Resources.basePath("test") -> {
                when (method) {
                    HttpMethod.Get -> {
                        respond(
                            Fixture.content("$fixturesBasePath/member/list.json"),
                            headers = headersOf("Content-Type" to listOf(ContentType.Application.Json.toString())),
                        )
                    }

                    HttpMethod.Post -> {
                        respond(
                            Fixture.content("$fixturesBasePath/member/create.json"),
                            headers = headersOf("Content-Type" to listOf(ContentType.Application.Json.toString())),
                        )
                    }

                    else -> error("Unhandled ${method.value} ${url.encodedPath}")
                }
            }

            Members.Resources.memberPath("test", "test-member") -> {
                when (method) {
                    HttpMethod.Delete -> {
                        respond(
                            Fixture.content("$fixturesBasePath/member/delete.json"),
                            headers = headersOf("Content-Type" to listOf(ContentType.Application.Json.toString())),
                        )
                    }

                    else -> error("Unhandled ${method.value} ${url.encodedPath}")
                }
            }

            Invites.Resources.basePath("test") -> {
                when (method) {
                    HttpMethod.Get -> {
                        respond(
                            Fixture.content("$fixturesBasePath/invite/list.json"),
                            headers = headersOf("Content-Type" to listOf(ContentType.Application.Json.toString())),
                        )
                    }

                    HttpMethod.Post -> {
                        respond(
                            Fixture.content("$fixturesBasePath/invite/create.json"),
                            headers = headersOf("Content-Type" to listOf(ContentType.Application.Json.toString())),
                        )
                    }

                    else -> error("Unhandled ${method.value} ${url.encodedPath}")
                }
            }

            else -> error("Unhandled ${url.encodedPath}")
        }
    }

class OrganizationsTest {
    @Test
    fun `can list organizations`() {
        runBlocking {
            client(mockEngine()).organizations.list().let { response ->
                assertIs<List<Organization>>(response)
            }
        }
    }

    @Test
    fun `can update organization`() {
        runBlocking {
            client(mockEngine()).organizations.update("test", UpdateOrganizationRequest(true)).let { response ->
                assertIs<OrganizationResponse>(response)
            }
        }
    }

    @Test
    fun `can list members of an organization`() {
        runBlocking {
            client(mockEngine()).organizations.members.list("test").let { response ->
                assertIs<ListMembersResponse>(response)
            }
        }
    }

    @Test
    fun `can add a member of an organization`() {
        runBlocking {
            client(mockEngine()).organizations.members.add("test", CreateMember("Alex", MemberRole.ADMIN)).let { response ->
                assertIs<CreateMemberResponse>(response)
            }

            assertThrows<IllegalArgumentException> {
                client(mockEngine()).organizations.members.add("test", CreateMember("Alex", MemberRole.OWNER))
            }
        }
    }

    @Test
    fun `can remove a member of the organization`() {
        runBlocking {
            client(mockEngine()).organizations.members.remove("test", "test-member").let { response ->
                assertIs<DeleteMemberResponse>(response)
            }
        }
    }

    @Test
    fun `can list organization invites`() {
        runBlocking {
            client(mockEngine()).organizations.invites.list("test").let { response ->
                assertIs<ListInvitesResponse>(response)
            }
        }
    }

    @Test
    fun `can create an organization invite`() {
        runBlocking {
            client(mockEngine()).organizations.invites.create("test", CreateMember("Alex", MemberRole.ADMIN)).let { response ->
                assertIs<CreateInviteResponse>(response)
            }
        }
    }
}
