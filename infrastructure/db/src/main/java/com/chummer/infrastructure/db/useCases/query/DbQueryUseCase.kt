package com.chummer.infrastructure.db.useCases.query

import app.cash.sqldelight.Transacter
import com.chummer.infrastructure.db.query.HasQuery
import com.chummer.infrastructure.db.useCases.DbExecutableUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext
import app.cash.sqldelight.Query as SqlQuery

abstract class DbQueryUseCase<QueryArgument, Row : Any, QueryTransacter : Transacter>(
    id: String,
    transacter: QueryTransacter
) : DbExecutableUseCase<QueryArgument, Row, QueryTransacter>(id, transacter),
    HasQuery<QueryArgument, Row, QueryTransacter> {

    override val coroutineContext: CoroutineContext = Dispatchers.IO

    override suspend fun execute(input: QueryArgument): Row {
        return withContext(coroutineContext) {
            transacter.getQuery(input).execute()
        }
    }

    protected abstract fun SqlQuery<Row>.execute(): Row
}
