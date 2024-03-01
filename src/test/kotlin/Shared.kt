/*
 * Copyright 2024 Jeliuc.com S.R.L. and Turso SDK contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
 */

package com.jeliuc.turso.sdk

import io.ktor.client.engine.HttpClientEngine
import java.io.File

val client: (HttpClientEngine) -> TursoClient = {
        engine ->
    TursoClient.using(engine, "test-token")
}

object Fixture {
    private fun path(path: String) = this::class.java.getResource(path)!!.path

    fun file(path: String) = File(path(path))

    fun content(path: String) = file(path).readText(Charsets.UTF_8).trimIndent()
}
