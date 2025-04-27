/*
 * Copyright 2024 Jeliuc.com S.R.L. and Turso SDK contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
 */

package com.jeliuc.turso.sdk

import com.jeliuc.turso.sdk.model.ConfigurationResponse
import com.jeliuc.turso.sdk.model.CreateDatabase
import com.jeliuc.turso.sdk.model.CreateDatabaseResponse
import com.jeliuc.turso.sdk.model.DatabaseUsageResponse
import com.jeliuc.turso.sdk.model.DeleteDatabaseResponse
import com.jeliuc.turso.sdk.model.InstanceResponse
import com.jeliuc.turso.sdk.model.ListDatabasesResponse
import com.jeliuc.turso.sdk.model.ListInstancesResponse
import com.jeliuc.turso.sdk.model.RetrieveDatabaseResponse
import com.jeliuc.turso.sdk.model.UpdateConfigurationRequest
import com.jeliuc.turso.sdk.resource.databases
import io.ktor.client.engine.cio.CIO
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class DatabasesIntegrationTest {
    val token by lazy {
        System.getenv("TURSO_TOKEN")
    }

    val organization by lazy {
        System.getenv("TURSO_ORGANIZATION")
    }

    val group = "default"

    val newDatabaseName = "integration-test-db"

    /**
     *  Manipulate Databases suite
     *
     *  * Creates new database
     *  * List databases and find in list newly created database
     *  * Retrieve database by name
     *  * Retrieve configuration
     *  * Update configuration
     *  * Retrieve usage
     *  * Stats
     *  * List instances
     *  * Retrieve instance
     *  * Delete database
     *
     */
    @Test
    fun `can manipulate database`() {
        val client = TursoClient.using(CIO.create(), token)

        val newDatabase =
            CreateDatabase(
                name = newDatabaseName,
                group = group,
                isSchema = false,
                sizeLimit = "2M",
            )

        val createDatabaseResponse =
            runBlocking {
                client.databases.create(
                    organization,
                    newDatabase,
                )
            }

        assertIs<CreateDatabaseResponse>(createDatabaseResponse, "Not a create database response")

        val listOfAvailableDatabases =
            runBlocking {
                client.databases.list(organization)
            }

        assertIs<ListDatabasesResponse>(listOfAvailableDatabases, "Not a list databases response")
        assertNotNull(listOfAvailableDatabases.databases.firstOrNull { it.name == newDatabaseName }, "New database has not been enlisted")

        val retrievedResponse =
            runBlocking {
                client.databases.retrieve(
                    organization,
                    newDatabaseName,
                )
            }

        assertIs<RetrieveDatabaseResponse>(retrievedResponse, "Not a retrieve database response")
        assertEquals(newDatabaseName, retrievedResponse.database.name, "Retrieved db name is incorrect")

        val configuration =
            runBlocking {
                client.databases.retrieveConfiguration(organization, newDatabaseName)
            }

        assertIs<ConfigurationResponse>(configuration, "Not a configuration response")

        val configurationUpdate =
            UpdateConfigurationRequest(
                blockReads = false,
                blockWrites = false,
                sizeLimit = "1M",
                allowAttach = false,
            )

        val updateConfigurationResponse =
            runBlocking {
                client.databases.updateConfiguration(organization, newDatabaseName, configurationUpdate)
            }

        assertIs<ConfigurationResponse>(updateConfigurationResponse, "Not a configuration update response")

        val retrievedConfiguration =
            runBlocking {
                client.databases.retrieveConfiguration(
                    organization,
                    newDatabaseName,
                )
            }

        assertIs<ConfigurationResponse>(retrievedConfiguration, "No a configuration response")

        val retrievedUsage =
            runBlocking {
                client.databases.usage(
                    organization,
                    newDatabaseName,
                )
            }

        assertIs<DatabaseUsageResponse>(retrievedUsage, "Not a usage response")
// TODO remove. Turso says endpoint is deprecated and will not come back.
//        val statsResponse =
//            runBlocking {
//                client.databases.stats(organization, newDatabaseName)
//            }
//
//        assertIs<StatsResponse>(statsResponse, "Not a stats response")

        val instances =
            runBlocking {
                client.databases.listInstances(
                    organization,
                    newDatabaseName,
                )
            }

        assertIs<ListInstancesResponse>(instances, "Can't deserialize list instances response")
        assertTrue(instances.instances.size > 0, "Zero instances")

        val instanceName = instances.instances.first().name
        val retrievedInstance =
            runBlocking {
                client.databases.retrieveInstance(
                    organization,
                    newDatabaseName,
                    instanceName,
                )
            }

        assertIs<InstanceResponse>(retrievedInstance, "Not an instance response")

        val deleteResponse =
            runBlocking {
                client.databases.delete(
                    organization,
                    newDatabaseName,
                )
            }

        assertIs<DeleteDatabaseResponse>(deleteResponse, "Not a delete response")
    }
}
