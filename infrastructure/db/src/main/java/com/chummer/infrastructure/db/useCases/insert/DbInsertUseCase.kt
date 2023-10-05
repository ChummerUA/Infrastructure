package com.chummer.infrastructure.db.useCases.insert

import app.cash.sqldelight.Transacter
import com.chummer.infrastructure.db.useCases.DbExecutableUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

abstract class DbInsertUseCase<Row : Any, QueryTransacter : Transacter>(
    id: String,
    transacter: QueryTransacter
) : DbExecutableUseCase<Row, Unit, QueryTransacter>(id, transacter) {

    override val coroutineContext: CoroutineContext = Dispatchers.IO

    override suspend fun invoke(input: Row) {
        withContext(coroutineContext) {
            transacter.insert(input)
        }
    }

    protected abstract fun QueryTransacter.insert(item: Row)
}
