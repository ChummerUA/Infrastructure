package com.chummer.infrastructure.db.useCases.list

import app.cash.sqldelight.Transacter
import com.chummer.infrastructure.db.query.HasQuery
import com.chummer.infrastructure.db.useCases.DbExecutableUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

abstract class DbListUseCase<QueryArgument, Row : Any, QueryTransacter : Transacter>(
    id: String,
    transacter: QueryTransacter
) : DbExecutableUseCase<QueryArgument, List<Row>, QueryTransacter>(id, transacter),
    HasQuery<QueryArgument, Row, QueryTransacter> {

    override val coroutineContext: CoroutineContext = Dispatchers.IO

    override suspend fun execute(input: QueryArgument): List<Row> {
        return withContext(coroutineContext) {
            transacter.getQuery(input).executeAsList()
        }
    }
}
