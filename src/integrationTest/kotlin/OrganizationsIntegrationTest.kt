package com.jeliuc.turso.sdk

import com.jeliuc.turso.sdk.model.ListInvitesResponse
import com.jeliuc.turso.sdk.resource.organizations
import io.ktor.client.engine.cio.CIO
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertIs

class OrganizationsIntegrationTest {
    val token by lazy {
        System.getenv("TURSO_TOKEN")
    }

    val organization by lazy {
        System.getenv("TURSO_ORGANIZATION")
    }

    @Test
    fun `can list invitations`() {
        val client = TursoClient.using(CIO.create(), token)

        val invitations =
            runBlocking {
                client.organizations.invites.list(organization)
            }

        assertIs<ListInvitesResponse>(invitations)
    }
}
