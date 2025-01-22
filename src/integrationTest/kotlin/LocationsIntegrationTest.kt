/*
 * Copyright 2024 Jeliuc.com S.R.L. and Turso SDK contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
 */

package com.jeliuc.turso.sdk

import com.jeliuc.turso.sdk.model.ListLocationsResponse
import com.jeliuc.turso.sdk.resource.locations
import io.ktor.client.engine.cio.CIO
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertIs

class LocationsIntegrationTest {
    val token by lazy {
        System.getenv("TURSO_TOKEN")
    }

    @Test
    fun `can access available locations`() {
        val client = TursoClient.using(CIO.create(), token)

        val locations =
            runBlocking {
                client.locations.list()
            }

        assertIs<ListLocationsResponse>(locations)
    }
}
