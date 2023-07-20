package com.chummer.infrastructure.network.useCase

import com.chummer.infrastructure.network.MapToError
import com.chummer.infrastructure.network.MapToResult
import io.ktor.client.HttpClient
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.request
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpMethod
import io.ktor.http.isSuccess

interface HttpRequestUseCaseDefinition<NetworkResult, NetworkError : Throwable> : MapToResult<NetworkResult>,
    MapToError<NetworkError> {
    val HttpRequestUseCaseDefinition<NetworkResult, NetworkError>.definition: HttpRequestDefinition

    suspend fun HttpClient.executeRequest(
        configureRequest: (HttpRequestBuilder.() -> Unit)
    ): NetworkResult {
        val response = request(definition.fullPath) {
            method = this@HttpRequestUseCaseDefinition.definition.method
            configureRequest()
        }

        return parseResponse(response)
    }

    fun HttpRequestUseCaseDefinition<NetworkResult, NetworkError>.parseResponse(response: HttpResponse): NetworkResult {
        return if (!response.status.isSuccess())
            throw response.toError()
        else
            response.toResult()
    }
}

data class HttpRequestDefinition(
    val baseUrl: String,
    val subPath: String,
    val method: HttpMethod
) {
    val fullPath
        get() = baseUrl + subPath
}
