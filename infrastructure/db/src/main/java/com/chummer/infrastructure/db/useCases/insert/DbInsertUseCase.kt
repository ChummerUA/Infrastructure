package com.chummer.infrastructure.db.useCases.insert

import app.cash.sqldelight.Transacter
import com.chummer.infrastructure.db.useCases.DbExecutableUseCase
import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

abstract class DbInsertUseCase<Row : Any, QueryTransacter : Transacter>(
    id: String,
    transacter: QueryTransacter
) : DbExecutableUseCase<Row, Unit, QueryTransacter>(id, transacter) {

    override val coroutineContext: CoroutineContext = Dispatchers.IO

    override suspend fun execute(input: Row) {
        transacter.insert(input)
    }

    protected abstract fun QueryTransacter.insert(item: Row)
}
