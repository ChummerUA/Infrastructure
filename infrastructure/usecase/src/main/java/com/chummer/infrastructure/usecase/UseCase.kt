package com.chummer.infrastructure.usecase

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapLatest
import kotlin.coroutines.CoroutineContext

abstract class UseCase(
    protected val id: String
)

abstract class SuspendableUseCase(id: String) : UseCase(id) {
    abstract val coroutineContext: CoroutineContext
}

abstract class ExecutableUseCase<Input, Output>(id: String) : SuspendableUseCase(id) {
    abstract suspend fun execute(input: Input): Output
}

abstract class MappableUseCase<Input, Output, MappedOutput>(id: String) : SuspendableUseCase(id) {
    protected abstract val internalUseCase: ExecutableUseCase<Input, Output>

    suspend fun execute(input: Input): MappedOutput {
        return internalUseCase.execute(input).mapResult()
    }

    protected abstract fun Output.mapResult(): MappedOutput
}

abstract class FlowUseCase<Input, Output>(id: String) : SuspendableUseCase(id) {
    abstract fun flow(input: Input): Flow<Output>
}

@OptIn(ExperimentalCoroutinesApi::class)
abstract class MappableFlowUseCase<Input, Output, MappedOutput>(id: String) :
    SuspendableUseCase(id) {
    protected abstract val internalUseCase: FlowUseCase<Input, Output>

    fun flow(input: Input): Flow<MappedOutput> {
        return internalUseCase.flow(input).mapLatest { it.toMappedOutput() }
    }

    protected abstract fun Output.toMappedOutput(): MappedOutput
}
