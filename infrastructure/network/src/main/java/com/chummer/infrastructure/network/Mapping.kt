package com.chummer.infrastructure.network

import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse

sealed class ResultMapper<Result> {
    suspend fun mapToResult(httpResponse: HttpResponse) = httpResponse.toResult()

    abstract suspend fun HttpResponse.toResult(): Result

    @Suppress("UNCHECKED_CAST")
    class JsonResultMapper<Result>: ResultMapper<Result>() {
        override suspend fun HttpResponse.toResult(): Result {
            return body<Any>() as Result
        }
    }
}

abstract class ErrorMapper<NetworkError: Throwable> {
    abstract suspend fun HttpResponse.toError(): NetworkError

    suspend fun mapToError(httpResponse: HttpResponse) = httpResponse.toError()
}
