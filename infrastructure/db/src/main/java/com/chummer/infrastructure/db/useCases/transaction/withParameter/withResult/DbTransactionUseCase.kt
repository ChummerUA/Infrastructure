package com.chummer.infrastructure.db.useCases.transaction.withParameter.withResult

import com.chummer.infrastructure.db.useCases.DbUseCase
import com.squareup.sqldelight.Transacter
import com.squareup.sqldelight.TransactionWithReturn
import com.squareup.sqldelight.TransactionWithoutReturn
import kotlinx.coroutines.withContext

abstract class DbTransactionUseCase<QueryArgument, Result, QueryTransacter : Transacter>(
    id: String,
    transacter: QueryTransacter
) : DbUseCase<QueryTransacter>(id, transacter) {

    suspend fun transaction(argument: QueryArgument): Result {
        return withContext(coroutineContext) {
            transacter.transactionWithResult {
                execute(argument)
            }
        }
    }

    protected abstract fun TransactionWithReturn<Result>.execute(argument: QueryArgument): Result
}
