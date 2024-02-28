package com.jeliuc.turso.sdk.resources

import com.jeliuc.turso.sdk.models.ApiError
import com.jeliuc.turso.sdk.models.UnexpectedResultError
import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse
import io.ktor.serialization.ContentConvertException
import kotlinx.serialization.SerializationException

interface ResponseHandler

internal suspend inline fun <reified T> handleResponse(response: HttpResponse): T =
    try {
        response.body<T>()
    } catch (e: SerializationException) {
        response.body<ApiError>().let { throw ApiError(it.message) }
    } catch (e: ContentConvertException) {
        response.body<ApiError>().let { throw ApiError(it.message) }
    } catch (e: Throwable) {
        throw UnexpectedResultError(e.message)
    }
