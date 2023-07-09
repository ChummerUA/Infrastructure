package com.chummer.infrastructure.network.useCase

import com.chummer.infrastructure.network.MapToDomain
import com.chummer.infrastructure.network.MapToError
import com.chummer.infrastructure.network.MapToResult
import io.ktor.client.HttpClient
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.request
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpMethod
import io.ktor.http.isSuccess

interface HttpRequestUseCaseDefinition<Domain, NetworkResult, NetworkError : Throwable> : MapToResult<NetworkResult>,
    MapToDomain<Domain, NetworkResult>, MapToError<NetworkError> {

    val HttpRequestUseCaseDefinition<Domain, NetworkResult, NetworkError>.baseUrl: String
    val HttpRequestUseCaseDefinition<Domain, NetworkResult, NetworkError>.subpath: String
    val HttpRequestUseCaseDefinition<Domain, NetworkResult, NetworkError>.method: HttpMethod

    fun HttpRequestUseCaseDefinition<Domain, NetworkResult, NetworkError>.getFullPath(): String {
        return baseUrl + subpath
    }

    suspend fun HttpClient.executeRequest(
        configureRequest: (HttpRequestBuilder.() -> Unit)
    ): Domain {
        val response = request(getFullPath()) {
            method = this@HttpRequestUseCaseDefinition.method
            configureRequest()
        }

        return parseResponse(response).toDomain()
    }

    fun HttpRequestUseCaseDefinition<Domain, NetworkResult, NetworkError>.parseResponse(response: HttpResponse): NetworkResult {
        return if (!response.status.isSuccess())
            throw response.toError()
        else
            response.toResult()
    }
}
