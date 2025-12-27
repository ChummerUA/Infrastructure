package com.chummer.infrastructure.db.useCases.insert

import app.cash.sqldelight.Transacter
import com.chummer.infrastructure.db.useCases.DbExecutableUseCase
import com.chummer.infrastructure.usecase.UseCaseLogger
import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

abstract class DbInsertUseCase<Row : Any, QueryTransacter : Transacter>(
    id: String,
    transacter: QueryTransacter,
    logger: UseCaseLogger
) : DbExecutableUseCase<Row, Unit, QueryTransacter>(id, transacter, logger) {

    override val coroutineContext: CoroutineContext = Dispatchers.IO

    override suspend fun execute(input: Row) {
        transacter.insert(input)
    }

    protected abstract fun QueryTransacter.insert(item: Row)
}
