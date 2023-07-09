package com.chummer.infrastructure.db.useCases.query.withParameter.resultless

import com.chummer.infrastructure.db.useCases.DbUseCase
import com.squareup.sqldelight.Transacter
import kotlinx.coroutines.withContext

abstract class DbQueryUseCase<QueryArgument, QueryTransacter : Transacter>(
    id: String,
    transacter: QueryTransacter
) : DbUseCase<QueryTransacter>(id, transacter) {

    suspend fun execute(argument: QueryArgument) {
        withContext(coroutineContext) {
            executeQuery(argument)
        }
    }

    protected abstract fun executeQuery(argument: QueryArgument)
}
