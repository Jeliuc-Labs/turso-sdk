package com.jeliuc.turso.sdk

import io.ktor.client.engine.HttpClientEngine
import java.io.File

val client: (HttpClientEngine) -> TursoClient = {
        engine ->
    TursoClient.of(engine, "test-token")
}

class Fixture {
    private fun path(path: String) = this::class.java.getResource(path)!!.path

    fun file(path: String) = File(path(path))

    fun content(path: String) = file(path).readText(Charsets.UTF_8).trimIndent()
}
