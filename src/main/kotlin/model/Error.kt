/*
 * Copyright 2024 Jeliuc.com S.R.L. and Turso SDK contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
 */

package com.jeliuc.turso.sdk.model

import kotlinx.serialization.Serializable

/**
 * Api Error
 *
 * Base class for all officially documented API error responses.
 * See official [documentation](https://docs.turso.tech/api-reference/introduction) for more information.
 *
 * Example error response:
 *
 * ```json
 * {
 *   "error": "could not find database with name [databaseName]: record not found"
 * }
 * ```
 *
 * @param message A message describing the error.
 */
@Serializable
class ApiError(override val message: String) : Error(message) {
    override fun toString(): String {
        return "ApiError(message='$message')"
    }
}

/**
 * Unexpected Result Error
 *
 * Error class for unexpected API responses.
 * We raise this error when we receive a response that doesn't conform to the API specification format.
 */
class UnexpectedResultError(override val message: String?) : Error(message) {
    override fun toString(): String {
        return "UnexpectedApiError(message='$message')"
    }
}
