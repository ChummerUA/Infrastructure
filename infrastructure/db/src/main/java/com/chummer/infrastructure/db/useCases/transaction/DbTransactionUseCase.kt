package com.chummer.infrastructure.db.useCases.transaction

import com.chummer.infrastructure.db.MapToDomain
import com.chummer.infrastructure.db.useCases.DbUseCase
import app.cash.sqldelight.Transacter
import app.cash.sqldelight.TransactionWithReturn
import kotlinx.coroutines.withContext

abstract class DbTransactionUseCase<QueryArgument, Domain, Row : Any, QueryTransacter : Transacter>(
    id: String,
    transacter: QueryTransacter
) : DbUseCase<QueryTransacter>(id, transacter), MapToDomain<Row, Domain> {

    suspend fun transaction(argument: QueryArgument): Domain {
        return withContext(coroutineContext) {
            transacter.transactionWithResult {
                executeTransaction(argument)
            }.toDomain()
        }
    }

    protected abstract fun TransactionWithReturn<Row>.executeTransaction(argument: QueryArgument): Row
}
