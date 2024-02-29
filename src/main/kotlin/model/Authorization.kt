package com.jeliuc.turso.sdk.model

/**
 * Authorization
 *
 * The authorization level for a database.
 */
enum class Authorization(val value: String) {
    FULL_ACCESS("full-access"),

    @Suppress("unused")
    READ_ONLY("read-only"),
}
