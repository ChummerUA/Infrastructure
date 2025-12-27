package com.chummer.infrastructure.db.useCases.transaction.withResult

import app.cash.sqldelight.Transacter
import app.cash.sqldelight.TransactionWithReturn
import app.cash.sqldelight.db.QueryResult
import com.chummer.infrastructure.db.useCases.DbExecutableUseCase
import com.chummer.infrastructure.usecase.UseCaseLogger
import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

abstract class DbTransactionUseCase<QueryArgument, Row : Any, QueryTransacter : Transacter>(
    id: String,
    transacter: QueryTransacter,
    logger: UseCaseLogger
) : DbExecutableUseCase<QueryArgument, Row, QueryTransacter>(id, transacter, logger) {

    override val coroutineContext: CoroutineContext = Dispatchers.IO

    override suspend fun execute(input: QueryArgument): Row {
        return transacter.transactionWithResult {
            execute(input).value
        }
    }

    protected abstract fun TransactionWithReturn<Row>.execute(argument: QueryArgument): QueryResult<Row>
}
