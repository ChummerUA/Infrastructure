package com.chummer.infrastructure.network

import com.chummer.infrastructure.usecase.ExecutableUseCase
import io.ktor.client.HttpClient
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.request
import io.ktor.client.request.url
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.client.statement.request
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.http.isSuccess
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

abstract class HttpUseCase<RequestParameter, NetworkResult>(
    id: String,
    private val client: HttpClient
) : ExecutableUseCase<RequestParameter, NetworkResult>(id) {
    protected abstract val definition: RequestDefinition

    override val coroutineContext: CoroutineContext = Dispatchers.IO

    override suspend fun execute(input: RequestParameter): NetworkResult {
        return withContext(coroutineContext) {
            client.executeRequest(input)
        }
    }

    private suspend fun HttpClient.executeRequest(parameter: RequestParameter): NetworkResult {
        val response = request {
            configureRequest(parameter)
        }

        println("--- $id ---")
        println("$id. Request url: ${response.request.url}")
        println("$id. Response code: ${response.status}")
        println("$id. Response body: ${response.bodyAsText()}")
        println("--- /$id ---")

        return parseResponse(response)
    }

    protected open fun HttpRequestBuilder.configureRequest(parameter: RequestParameter) {
        method = definition.method
        url(definition.subPath)
    }

    private suspend fun parseResponse(response: HttpResponse): NetworkResult {
        return if (!response.status.isSuccess())
            throw response.toError()
        else
            response.deserialize()
    }

    protected abstract suspend fun HttpResponse.deserialize(): NetworkResult

    protected open fun HttpResponse.toError(): Throwable {
        return when(status) {
            HttpStatusCode.NotFound -> NetworkError.NotFoundError(
                request.url.encodedPathAndQuery,
                request.method
            )
            HttpStatusCode.Unauthorized -> NetworkError.UnauthorizedError(
                request.url.encodedPathAndQuery,
                request.method
            )
            HttpStatusCode.BadRequest -> NetworkError.BadRequestError(
                request.url.encodedPathAndQuery,
                request.method
            )
            else -> NetworkError.OtherError(
                request.url.encodedPathAndQuery,
                request.method,
                status
            )
        }
    }
}


data class RequestDefinition(
    val subPath: String,
    val method: HttpMethod
)

sealed class NetworkError(
    open val url: String,
    open val method: HttpMethod,
    open val statusCode: HttpStatusCode
) : Throwable(
    "${method.value} $url request failed with $statusCode code"
) {
    data class NotFoundError(
        override val url: String,
        override val method: HttpMethod,
        override val statusCode: HttpStatusCode = HttpStatusCode.NotFound
    ) : NetworkError(url, method, statusCode)

    data class BadRequestError(
        override val url: String,
        override val method: HttpMethod,
        override val statusCode: HttpStatusCode = HttpStatusCode.BadRequest
    ) : NetworkError(url, method, statusCode)

    data class OtherError(
        override val url: String,
        override val method: HttpMethod,
        override val statusCode: HttpStatusCode
    ) : NetworkError(url, method, statusCode)

    data class UnauthorizedError(
        override val url: String,
        override val method: HttpMethod,
        override val statusCode: HttpStatusCode = HttpStatusCode.Unauthorized
    ) : NetworkError(url, method, statusCode)
}
