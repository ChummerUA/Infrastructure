package com.chummer.infrastructure.network.useCase.request.parameterless.withResult

import com.chummer.infrastructure.network.useCase.HttpRequestUseCaseDefinition
import com.chummer.infrastructure.network.useCase.request.parameterless.ConfigureRequest
import com.chummer.infrastructure.usecase.SuspendUseCase
import io.ktor.client.HttpClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

abstract class HttpRequestUseCase<NetworkResult, NetworkError : Throwable>(
    id: String,
    private val client: HttpClient
) : SuspendUseCase(id), HttpRequestUseCaseDefinition<NetworkResult, NetworkError>,
    ConfigureRequest {
    override val coroutineContext: CoroutineContext = Dispatchers.IO

    private val _isExecuting = MutableStateFlow(false)
    val isExecuting: Flow<Boolean> = _isExecuting

    suspend fun execute(): NetworkResult {
        _isExecuting.value = true

        return try {
            withContext(coroutineContext) {
                client.executeRequest {
                    configureRequest()
                }
            }
        } catch (e: Error) {
            throw e
        } finally {
            _isExecuting.value = false
        }
    }
}
