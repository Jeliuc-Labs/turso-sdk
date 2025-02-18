/*
 * Copyright 2024 Jeliuc.com S.R.L. and Turso SDK contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
 */

package com.jeliuc.turso.sdk.resource

import com.jeliuc.turso.sdk.model.ApiError
import com.jeliuc.turso.sdk.model.UnexpectedResultError
import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse
import io.ktor.serialization.ContentConvertException
import kotlinx.serialization.SerializationException

abstract class ResponseHandler {
    @Throws(ApiError::class, UnexpectedResultError::class)
    protected suspend inline fun <reified T> handleResponse(response: HttpResponse): T =
        try {
            response.body<T>()
        } catch (e: SerializationException) {
            throw ApiError(e.message.toString())
        } catch (e: ContentConvertException) {
            throw ApiError(e.message.toString())
        } catch (e: Throwable) {
            throw UnexpectedResultError(e.message)
        }
}
