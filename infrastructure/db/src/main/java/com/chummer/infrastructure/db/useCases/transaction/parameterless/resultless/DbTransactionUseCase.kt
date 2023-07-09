package com.chummer.infrastructure.db.useCases.transaction.parameterless.resultless

import com.chummer.infrastructure.db.useCases.DbUseCase
import com.squareup.sqldelight.Transacter
import com.squareup.sqldelight.TransactionWithoutReturn
import kotlinx.coroutines.withContext

abstract class DbTransactionUseCase<QueryTransacter : Transacter>(
    id: String,
    transacter: QueryTransacter
) : DbUseCase<QueryTransacter>(id, transacter) {

    suspend fun transaction() {
        withContext(coroutineContext) {
            transacter.transaction {
                execute()
            }
        }
    }

    protected abstract fun TransactionWithoutReturn.execute()
}
