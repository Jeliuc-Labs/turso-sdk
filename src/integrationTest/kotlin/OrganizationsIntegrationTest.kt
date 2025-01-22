/*
 * Copyright 2024 Jeliuc.com S.R.L. and Turso SDK contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
 */

package com.jeliuc.turso.sdk

import com.jeliuc.turso.sdk.model.InvoicesResponse
import com.jeliuc.turso.sdk.model.ListAuditLogsResponse
import com.jeliuc.turso.sdk.model.ListInvitesResponse
import com.jeliuc.turso.sdk.model.ListMembersResponse
import com.jeliuc.turso.sdk.model.MemberResponse
import com.jeliuc.turso.sdk.model.Organization
import com.jeliuc.turso.sdk.model.OrganizationDatabaseUsageResponse
import com.jeliuc.turso.sdk.model.OrganizationPlansResponse
import com.jeliuc.turso.sdk.model.OrganizationResponse
import com.jeliuc.turso.sdk.model.SubscriptionResponse
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
    fun `can retrieve current subscription`() {
        val subscription =
            runBlocking {
                getClient().organizations.subscription(organization)
            }

        assertIs<SubscriptionResponse>(subscription)
    }

    @Test
    fun `can get organization usage`() {
        val usage =
            runBlocking {
                getClient().organizations.usage(organization)
            }

        assertIs<OrganizationDatabaseUsageResponse>(usage)
    }

    @Test
    fun `can list invoices`() {
        val invoices =
            runBlocking {
                getClient().organizations.listInvoices(organization)
            }

        assertIs<InvoicesResponse>(invoices)
    }

    @Test
    fun `can list plans`() {
        val plans =
            runBlocking {
                getClient().organizations.plans(organization)
            }

        assertIs<OrganizationPlansResponse>(plans)
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
        val client = getClient()
        val members =
            runBlocking {
                client.organizations.members.list(organization)
            }

        assertIs<ListMembersResponse>(members)

        val username = members.members.first().username
        val member =
            runBlocking {
                client.organizations.members.retrieve(organization, username)
            }

        assertIs<MemberResponse>(member)
    }

    private fun getClient(): TursoClient = TursoClient.using(CIO.create(), token)
}
