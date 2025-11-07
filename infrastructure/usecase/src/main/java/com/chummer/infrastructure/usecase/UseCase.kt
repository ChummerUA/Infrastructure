package com.chummer.infrastructure.usecase

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.withContext
import java.time.Instant
import kotlin.coroutines.CoroutineContext

abstract class UseCase(
    protected val id: String
)

abstract class SuspendableUseCase(id: String) : UseCase(id) {
    abstract val coroutineContext: CoroutineContext
}

abstract class ExecutableUseCase<Input, Output>(
    id: String,
    protected open val useCaseLogger: UseCaseLogger
) : SuspendableUseCase(id) {
    private val _isExecuting = MutableStateFlow(false)
    val isExecuting: Flow<Boolean> = _isExecuting
    protected open val logAll = true

    protected abstract suspend fun execute(input: Input): Output

    open suspend operator fun invoke(input: Input): Output = withContext(coroutineContext) {
        _isExecuting.value = true
        val executionId = synchronized(id) {
           Instant.now().toEpochMilli()
        }
        if (logAll) useCaseLogger.onExecutionStarted(id, executionId, input)

        return@withContext try {
            execute(input).also {
                if (logAll) useCaseLogger.onExecuted(id, executionId, it)
            }
        } catch (e: Throwable) {
            useCaseLogger.onExecutionFailed(id, executionId, e)
            throw e
        } finally {
            _isExecuting.value = false
        }
    }
}

abstract class MappableUseCase<Input, Output, MappedOutput>(id: String) : SuspendableUseCase(id) {
    protected abstract val internalUseCase: ExecutableUseCase<Input, Output>

    suspend fun execute(input: Input): MappedOutput {
        return internalUseCase(input).mapResult()
    }

    protected abstract fun Output.mapResult(): MappedOutput
}

abstract class FlowUseCase<Input, Output>(id: String) : SuspendableUseCase(id) {
    abstract operator fun invoke(input: Input): Flow<Output?>
}

@OptIn(ExperimentalCoroutinesApi::class)
abstract class MappableFlowUseCase<Input, Output, MappedOutput>(id: String) :
    SuspendableUseCase(id) {
    protected abstract val internalUseCase: FlowUseCase<Input, Output>

    fun flow(input: Input): Flow<MappedOutput?> {
        return internalUseCase.invoke(input).mapLatest { it?.toMappedOutput() }
    }

    protected abstract fun Output.toMappedOutput(): MappedOutput
}

interface UseCaseLogger {
    fun <Input> onExecutionStarted(useCaseId: String, executionId: Long, input: Input)

    fun onExecutionFailed(useCaseId: String, executionId: Long, e: Throwable)

    fun <Output> onExecuted(useCaseId: String, executionId: Long, output: Output)
}
