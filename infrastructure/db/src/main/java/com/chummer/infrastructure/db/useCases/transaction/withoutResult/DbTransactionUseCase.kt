package com.chummer.infrastructure.db.useCases.transaction.withoutResult

import app.cash.sqldelight.Transacter
import app.cash.sqldelight.TransactionWithoutReturn
import com.chummer.infrastructure.db.dbMutex
import com.chummer.infrastructure.db.useCases.DbExecutableUseCase
import com.chummer.infrastructure.usecase.UseCaseLogger
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

abstract class DbTransactionUseCase<QueryArgument, QueryTransacter : Transacter>(
    id: String,
    transacter: QueryTransacter,
    logger: UseCaseLogger
) : DbExecutableUseCase<QueryArgument, Unit, QueryTransacter>(id, transacter, logger) {

    override val coroutineContext: CoroutineContext = Dispatchers.IO

    override suspend fun execute(input: QueryArgument) {
        return withContext(coroutineContext) {
            dbMutex.withLock {
                transacter.transaction {
                    execute(input)
                }
            }
        }
    }

    protected abstract fun TransactionWithoutReturn.execute(argument: QueryArgument)
}
