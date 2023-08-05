package com.chummer.infrastructure.db.useCases.flow

import app.cash.sqldelight.Transacter
import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToOne
import com.chummer.infrastructure.db.query.HasQuery
import com.chummer.infrastructure.usecase.FlowUseCase
import kotlinx.coroutines.flow.Flow
import app.cash.sqldelight.Query as SqlQuery

abstract class DbItemFlowUseCase<QueryArgument, Row : Any, QueryTransacter : Transacter>(
    id: String,
    private val transacter: QueryTransacter
) : FlowUseCase<QueryArgument, Row>(id), HasQuery<QueryArgument, Row, QueryTransacter> {

    override fun invoke(input: QueryArgument): Flow<Row> {
        return transacter.getQuery(input).toFlow()
    }

    private fun SqlQuery<Row>.toFlow(): Flow<Row> {
        return this.asFlow().mapToOne(coroutineContext)
    }
}
