package com.chummer.infrastructure.db.useCases

import app.cash.sqldelight.Transacter
import com.chummer.infrastructure.db.dbMutex
import com.chummer.infrastructure.usecase.ExecutableUseCase
import com.chummer.infrastructure.usecase.UseCaseLogger
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withTimeout
import kotlin.coroutines.CoroutineContext

abstract class DbExecutableUseCase<Input, Output: Any?, QueryTransacter : Transacter>(
    id: String,
    protected val transacter: QueryTransacter,
    logger: UseCaseLogger
) : ExecutableUseCase<Input, Output>(id, logger) {
    override val coroutineContext: CoroutineContext = Dispatchers.IO

    override suspend fun invoke(input: Input): Output {
        return dbMutex.withLock {
            println("[$id][DB_MUTEX]: Locking db mutex")
            val output = withTimeout(5000) {
                super.invoke(input)
            }
            println("[$id][DB_MUTEX]: Unlocking db mutex")
            return@withLock output
        }
    }
}
