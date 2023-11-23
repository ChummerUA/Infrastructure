package com.chummer.infrastructure.db.useCases

import app.cash.sqldelight.Transacter
import com.chummer.infrastructure.usecase.ExecutableUseCase
import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

abstract class DbExecutableUseCase<Input, Output: Any?, QueryTransacter : Transacter>(
    id: String,
    protected val transacter: QueryTransacter
) : ExecutableUseCase<Input, Output>(id) {
    override val coroutineContext: CoroutineContext = Dispatchers.IO
}
