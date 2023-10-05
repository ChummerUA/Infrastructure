package com.chummer.infrastructure.db.useCases.transaction.withResult

import app.cash.sqldelight.Transacter
import app.cash.sqldelight.TransactionWithReturn
import com.chummer.infrastructure.db.useCases.DbExecutableUseCase
import kotlinx.coroutines.withContext

abstract class DbTransactionUseCase<QueryArgument, Row : Any, QueryTransacter : Transacter>(
    id: String,
    transacter: QueryTransacter
) : DbExecutableUseCase<QueryArgument, Row, QueryTransacter>(id, transacter) {

    override suspend fun invoke(input: QueryArgument): Row {
        return withContext(coroutineContext) {
            transacter.transactionWithResult {
                execute(input)
            }
        }
    }

    protected abstract fun TransactionWithReturn<Row>.execute(argument: QueryArgument): Row
}
