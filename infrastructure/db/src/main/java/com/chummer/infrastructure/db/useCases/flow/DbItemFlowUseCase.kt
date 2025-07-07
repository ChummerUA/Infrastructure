package com.chummer.infrastructure.db.useCases.flow

import app.cash.sqldelight.Transacter
import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToOne
import app.cash.sqldelight.coroutines.mapToOneOrNull
import com.chummer.infrastructure.db.query.HasQuery
import com.chummer.infrastructure.usecase.FlowUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlin.coroutines.CoroutineContext
import app.cash.sqldelight.Query as SqlQuery

abstract class DbItemFlowUseCase<QueryArgument, Row : Any, QueryTransacter : Transacter>(
    id: String,
    protected val transacter: QueryTransacter
) : FlowUseCase<QueryArgument, Row>(id), HasQuery<QueryArgument, Row, QueryTransacter> {

    override val coroutineContext: CoroutineContext = Dispatchers.IO

    override fun invoke(input: QueryArgument): Flow<Row?> {
        return transacter.getQuery(input).toFlow()
    }

    protected open fun SqlQuery<Row>.toFlow(): Flow<Row?> {
        return this.asFlow().mapToOneOrNull(coroutineContext)
    }
}
