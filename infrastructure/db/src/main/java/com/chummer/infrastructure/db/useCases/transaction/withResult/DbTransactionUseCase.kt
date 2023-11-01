package com.chummer.infrastructure.db.useCases.transaction.withResult

import app.cash.sqldelight.Transacter
import app.cash.sqldelight.TransactionWithReturn
import com.chummer.infrastructure.db.useCases.DbExecutableUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

abstract class DbTransactionUseCase<QueryArgument, Row : Any, QueryTransacter : Transacter>(
    id: String,
    transacter: QueryTransacter
) : DbExecutableUseCase<QueryArgument, Row, QueryTransacter>(id, transacter) {

    override val coroutineContext: CoroutineContext = Dispatchers.IO

    override suspend fun execute(input: QueryArgument): Row {
        return withContext(coroutineContext) {
            transacter.transactionWithResult {
                execute(input)
            }
        }
    }

    protected abstract fun TransactionWithReturn<Row>.execute(argument: QueryArgument): Row
}
