package com.chummer.infrastructure.db.useCases.insert.withResult

import app.cash.sqldelight.ExecutableQuery
import app.cash.sqldelight.Transacter
import com.chummer.infrastructure.db.dbMutex
import com.chummer.infrastructure.db.useCases.DbExecutableUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.sync.withLock
import kotlin.coroutines.CoroutineContext

abstract class DbInsertUseCase<Row : Any, Result: Any, QueryTransacter : Transacter>(
    id: String,
    transacter: QueryTransacter
) : DbExecutableUseCase<Row, Result, QueryTransacter>(id, transacter) {

    override val coroutineContext: CoroutineContext = Dispatchers.IO

    override suspend fun execute(input: Row): Result {
        return dbMutex.withLock {
            transacter.insert(input).executeAsOne()
        }
    }

    protected abstract fun QueryTransacter.insert(item: Row): ExecutableQuery<Result>
}
