package com.chummer.infrastructure.db.useCases.transaction.withParameter.resultless

import com.chummer.infrastructure.db.useCases.DbUseCase
import com.squareup.sqldelight.Transacter
import com.squareup.sqldelight.TransactionWithoutReturn
import kotlinx.coroutines.withContext

abstract class DbTransactionUseCase<QueryArgument, QueryTransacter : Transacter>(
    id: String,
    transacter: QueryTransacter
) : DbUseCase<QueryTransacter>(id, transacter) {

    suspend fun transaction(argument: QueryArgument) {
        withContext(coroutineContext) {
            transacter.transaction {
                execute(argument)
            }
        }
    }

    protected abstract fun TransactionWithoutReturn.execute(argument: QueryArgument)
}
