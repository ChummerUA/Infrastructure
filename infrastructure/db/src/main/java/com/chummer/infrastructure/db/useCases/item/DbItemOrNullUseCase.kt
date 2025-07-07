package com.chummer.infrastructure.db.useCases.item

import app.cash.sqldelight.Transacter
import com.chummer.infrastructure.db.query.HasQuery
import com.chummer.infrastructure.db.useCases.DbExecutableUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

abstract class DbItemOrNullUseCase<QueryArgument, Row : Any, QueryTransacter : Transacter>(
    id: String,
    transacter: QueryTransacter
) : DbExecutableUseCase<QueryArgument, Row?, QueryTransacter>(id, transacter),
    HasQuery<QueryArgument, Row, QueryTransacter> {

    override val coroutineContext: CoroutineContext = Dispatchers.IO

    override suspend fun execute(input: QueryArgument): Row? {
        return withContext(coroutineContext) {
            try {
                transacter.getQuery(input).executeAsOneOrNull()
            } catch (e: Throwable) {
                println(e)
                throw e
            }
        }
    }
}
