package com.chummer.infrastructure.db.useCases.insert.withResult

import app.cash.sqldelight.ExecutableQuery
import app.cash.sqldelight.Transacter
import com.chummer.infrastructure.db.useCases.DbExecutableUseCase
import com.chummer.infrastructure.usecase.UseCaseLogger
import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

abstract class DbInsertUseCase<Row : Any, Result: Any, QueryTransacter : Transacter>(
    id: String,
    transacter: QueryTransacter,
    logger: UseCaseLogger
) : DbExecutableUseCase<Row, Result, QueryTransacter>(id, transacter, logger) {

    override val coroutineContext: CoroutineContext = Dispatchers.IO

    override suspend fun execute(input: Row): Result {
        return transacter.insert(input).executeAsOne()
    }

    protected abstract fun QueryTransacter.insert(item: Row): ExecutableQuery<Result>
}
