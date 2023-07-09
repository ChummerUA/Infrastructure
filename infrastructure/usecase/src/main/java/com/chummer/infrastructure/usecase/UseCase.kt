package com.chummer.infrastructure.usecase

import kotlin.coroutines.CoroutineContext

abstract class UseCase(
    protected val id: String
)

abstract class SuspendUseCase(id: String): UseCase(id) {
    abstract val coroutineContext: CoroutineContext
}
