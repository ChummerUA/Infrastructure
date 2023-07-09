package com.chummer.infrastructure.db.useCases.query.parameterless.resultless

import com.chummer.infrastructure.db.useCases.DbUseCase
import com.squareup.sqldelight.Transacter
import kotlinx.coroutines.withContext

abstract class DbQueryUseCase<QueryTransacter : Transacter>(
    id: String,
    transacter: QueryTransacter
) : DbUseCase<QueryTransacter>(id, transacter) {

    suspend fun execute() {
        withContext(coroutineContext) {
            executeQuery()
        }
    }

    protected abstract fun executeQuery()
}
