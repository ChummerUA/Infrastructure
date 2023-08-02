package com.chummer.infrastructure.db.useCases

import com.chummer.infrastructure.usecase.SuspendUseCase
import app.cash.sqldelight.Transacter
import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

abstract class DbUseCase<QueryTransacter : Transacter>(
    id: String,
    protected val transacter: QueryTransacter
) : SuspendUseCase(id) {
    override val coroutineContext: CoroutineContext = Dispatchers.IO
}
