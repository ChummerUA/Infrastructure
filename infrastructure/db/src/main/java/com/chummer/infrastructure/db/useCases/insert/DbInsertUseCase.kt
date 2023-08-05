package com.chummer.infrastructure.db.useCases.insert

import app.cash.sqldelight.Transacter
import com.chummer.infrastructure.db.useCases.DbExecutableUseCase
import kotlinx.coroutines.withContext

abstract class DbInsertUseCase<Row : Any, QueryTransacter : Transacter>(
    id: String,
    transacter: QueryTransacter
) : DbExecutableUseCase<Row, Unit, QueryTransacter>(id, transacter) {
    override suspend fun invoke(input: Row) {
        withContext(coroutineContext) {
            transacter.insert(input)
        }
    }

    protected abstract fun QueryTransacter.insert(item: Row)
}
