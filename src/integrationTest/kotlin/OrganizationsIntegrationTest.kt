package com.jeliuc.turso.sdk

import com.jeliuc.turso.sdk.model.ListAuditLogsResponse
import com.jeliuc.turso.sdk.model.ListInvitesResponse
import com.jeliuc.turso.sdk.resource.organizations
import io.ktor.client.engine.cio.CIO
import kotlinx.coroutines.runBlocking
import kotlin.test.Ignore
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
        val invitations =
            runBlocking {
                getClient().organizations.invites.list(organization)
            }

        assertIs<ListInvitesResponse>(invitations)
    }

    @Test
    @Ignore("The free plan we currently use doesn't support this endpoint")
    fun `can list audit logs`() {
        val logs =
            runBlocking {
                getClient().organizations.auditLogs.list(organization)
            }

        assertIs<ListAuditLogsResponse>(logs)
    }

    private fun getClient(): TursoClient = TursoClient.using(CIO.create(), token)
}
