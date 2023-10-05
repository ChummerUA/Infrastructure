package com.chummer.infrastructure.db.useCases.transaction.withoutResult

import app.cash.sqldelight.Transacter
import app.cash.sqldelight.TransactionWithoutReturn
import com.chummer.infrastructure.db.useCases.DbExecutableUseCase
import kotlinx.coroutines.withContext

abstract class DbTransactionUseCase<QueryArgument, QueryTransacter : Transacter>(
    id: String,
    transacter: QueryTransacter
) : DbExecutableUseCase<QueryArgument, Unit, QueryTransacter>(id, transacter) {

    override suspend fun invoke(input: QueryArgument) {
        return withContext(coroutineContext) {
            transacter.transaction {
                execute(input)
            }
        }
    }

    protected abstract fun TransactionWithoutReturn.execute(argument: QueryArgument)
}
