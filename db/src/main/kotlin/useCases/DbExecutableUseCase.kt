package com.chummer.infrastructure.db.useCases

import app.cash.sqldelight.Transacter
import com.chummer.infrastructure.usecase.ExecutableUseCase
import com.chummer.infrastructure.usecase.UseCaseLogger
import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

abstract class DbExecutableUseCase<Input, Output: Any?, QueryTransacter : Transacter>(
    id: String,
    protected val transacter: QueryTransacter,
    logger: UseCaseLogger
) : ExecutableUseCase<Input, Output>(id, logger) {
    override val coroutineContext: CoroutineContext = Dispatchers.IO
}
