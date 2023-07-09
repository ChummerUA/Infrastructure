package com.chummer.infrastructure.network.useCase.request.withParameter.resultless

import com.chummer.infrastructure.network.useCase.HttpRequestUseCaseDefinition
import com.chummer.infrastructure.network.useCase.request.withParameter.ConfigureRequest
import com.chummer.infrastructure.usecase.SuspendUseCase
import io.ktor.client.HttpClient
import io.ktor.client.statement.HttpResponse
import io.ktor.http.isSuccess
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

abstract class HttpRequestUseCase<RequestParameter, NetworkError : Throwable>(
    id: String,
    private val client: HttpClient
) : SuspendUseCase(id), HttpRequestUseCaseDefinition<Boolean, Boolean, NetworkError>,
    ConfigureRequest<RequestParameter> {
    override val coroutineContext: CoroutineContext = Dispatchers.IO

    private val _isExecuting = MutableStateFlow(false)
    val isExecuting: Flow<Boolean> = _isExecuting

    suspend fun execute(parameter: RequestParameter): Boolean {
        _isExecuting.value = true

        return try {
            withContext(coroutineContext) {
                client.executeRequest {
                    configureRequest(parameter)
                }
            }
        } catch (e: Error) {
            throw e
        } finally {
            _isExecuting.value = false
        }
    }

    override fun Boolean.toDomain(): Boolean = this

    override fun HttpResponse.toResult(): Boolean {
        return status.isSuccess()
    }
}
