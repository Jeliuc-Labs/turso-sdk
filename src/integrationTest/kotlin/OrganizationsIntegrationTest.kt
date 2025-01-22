/*
 * Copyright 2024 Jeliuc.com S.R.L. and Turso SDK contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
 */

package com.jeliuc.turso.sdk

import com.jeliuc.turso.sdk.model.ListAuditLogsResponse
import com.jeliuc.turso.sdk.model.ListInvitesResponse
import com.jeliuc.turso.sdk.model.ListMembersResponse
import com.jeliuc.turso.sdk.model.Organization
import com.jeliuc.turso.sdk.model.OrganizationResponse
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
    fun `can list all organizations`() {
        val organizations =
            runBlocking {
                getClient().organizations.list()
            }

        assertIs<List<Organization>>(organizations)
    }

    @Test
    fun `can retrieve organization`() {
        val organization =
            runBlocking {
                getClient().organizations.retrieve(organization)
            }

        assertIs<OrganizationResponse>(organization)
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

    @Test
    fun `can list members`() {
        val members =
            runBlocking {
                getClient().organizations.members.list(organization)
            }

        assertIs<ListMembersResponse>(members)
    }

    private fun getClient(): TursoClient = TursoClient.using(CIO.create(), token)
}
