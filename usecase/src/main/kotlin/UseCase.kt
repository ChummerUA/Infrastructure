package com.chummer.infrastructure.usecase

import com.chummer.infrastructure.annotations.GenerateInvoke
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.withContext
import java.time.Instant
import kotlin.coroutines.CoroutineContext

abstract class UseCase(
    protected val id: String
)

abstract class SuspendableUseCase(id: String) : UseCase(id) {
    abstract val coroutineContext: CoroutineContext
}

@GenerateInvoke
abstract class ExecutableUseCase<Input, Output>(
    id: String,
    protected open val useCaseLogger: UseCaseLogger
) : SuspendableUseCase(id) {
    private val _isExecuting = MutableStateFlow(false)
    val isExecuting: Flow<Boolean> = _isExecuting

    protected abstract suspend fun run(input: Input): Output

    suspend fun execute(input: Input): Output = withContext(coroutineContext) {
        _isExecuting.value = true
        val executionId = synchronized(id) {
            Instant.now().toEpochMilli()
        }

        return@withContext try {
            run(input)
        } catch (e: Throwable) {
            useCaseLogger.onExecutionFailed(id, e)
            throw e
        } finally {
            _isExecuting.value = false
        }
    }
}

@GenerateInvoke
abstract class FlowUseCase<Input, Output>(id: String) : SuspendableUseCase(id) {
    abstract fun execute(input: Input): Flow<Output?>
}

interface UseCaseLogger {
    fun onExecutionFailed(useCaseId: String, e: Throwable)
}
