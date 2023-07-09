package com.chummer.infrastructure.db.useCases.transaction.parameterless.withResult

import com.chummer.infrastructure.db.useCases.DbUseCase
import com.squareup.sqldelight.Transacter
import com.squareup.sqldelight.TransactionWithReturn
import com.squareup.sqldelight.TransactionWithoutReturn
import kotlinx.coroutines.withContext

abstract class DbTransactionUseCase<Result, QueryTransacter : Transacter>(
    id: String,
    transacter: QueryTransacter
) : DbUseCase<QueryTransacter>(id, transacter) {

    suspend fun transaction(): Result {
        return withContext(coroutineContext) {
            transacter.transactionWithResult {
                execute()
            }
        }
    }

    protected abstract fun TransactionWithReturn<Result>.execute(): Result
}
