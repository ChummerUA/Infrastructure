package com.chummer.infrastructure.db.useCases.item

import app.cash.sqldelight.Transacter
import com.chummer.infrastructure.db.query.HasQuery
import com.chummer.infrastructure.db.useCases.DbExecutableUseCase
import com.chummer.infrastructure.usecase.UseCaseLogger
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

abstract class DbItemUseCase<QueryArgument, Row : Any, QueryTransacter : Transacter>(
    id: String,
    transacter: QueryTransacter,
    logger: UseCaseLogger
) : DbExecutableUseCase<QueryArgument, Row, QueryTransacter>(id, transacter, logger),
    HasQuery<QueryArgument, Row, QueryTransacter> {

    override val coroutineContext: CoroutineContext = Dispatchers.IO

    override suspend fun execute(input: QueryArgument): Row {
        return withContext(coroutineContext) {
            transacter.getQuery(input).executeAsOne()
        }
    }
}
