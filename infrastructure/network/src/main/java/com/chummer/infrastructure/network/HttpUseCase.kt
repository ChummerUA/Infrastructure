package com.chummer.infrastructure.network

import com.chummer.infrastructure.usecase.ExecutableUseCase
import io.ktor.client.HttpClient
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.request
import io.ktor.client.request.url
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpMethod
import io.ktor.http.isSuccess
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

abstract class HttpUseCase<RequestParameter, NetworkResult, NetworkError : Throwable>(
    id: String,
    private val client: HttpClient
) : ExecutableUseCase<RequestParameter, NetworkResult>(id) {
    protected abstract val definition: RequestDefinition
    protected abstract val errorMapper: ErrorMapper<NetworkError>
    protected abstract val responseMapper: ResultMapper<NetworkResult>

    override val coroutineContext: CoroutineContext = Dispatchers.IO

    private val _isExecuting = MutableStateFlow(false)
    val isExecuting: Flow<Boolean> = _isExecuting

    override suspend fun invoke(input: RequestParameter): NetworkResult {
        _isExecuting.value = true

        return try {
            withContext(coroutineContext) {
                client.executeRequest(input)
            }
        } catch (e: Error) {
            throw e
        } finally {
            _isExecuting.value = false
        }
    }

    private suspend fun HttpClient.executeRequest(parameter: RequestParameter): NetworkResult {
        val response = request(definition.subPath) {
            configureRequest(parameter)
        }

        return parseResponse(response)
    }

    protected open fun HttpRequestBuilder.configureRequest(parameter: RequestParameter) {
        method = definition.method
        url(definition.subPath)
    }

    private suspend fun parseResponse(response: HttpResponse): NetworkResult {
        return if (!response.status.isSuccess())
            throw errorMapper.mapToError(response)
        else
            responseMapper.mapToResult(response)
    }
}


data class RequestDefinition(
    val subPath: String,
    val method: HttpMethod
)
