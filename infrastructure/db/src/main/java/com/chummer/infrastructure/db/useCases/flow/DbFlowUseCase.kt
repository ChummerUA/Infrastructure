package com.chummer.infrastructure.db.useCases.flow

import com.chummer.infrastructure.db.useCases.DbUseCase
import com.chummer.infrastructure.db.MapToDomain
import com.chummer.infrastructure.db.query.Query
import app.cash.sqldelight.Transacter
import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToOne
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import app.cash.sqldelight.Query as SqlQuery

abstract class DbFlowUseCase<QueryArgument, Domain : Any, Row : Any, QueryTransacter : Transacter>(
    id: String,
    transacter: QueryTransacter
) : DbUseCase<QueryTransacter>(id, transacter),
    Query<QueryArgument, Row, QueryTransacter>, MapToDomain<Row, Domain> {

    fun flow(parameter: QueryArgument): Flow<Domain> {
        return transacter.getQuery(parameter).toFlow().mapToDomain()
    }

    private fun SqlQuery<Row>.toFlow(): Flow<Row> {
        return this.asFlow().mapToOne(coroutineContext)
    }

    private fun Flow<Row>.mapToDomain() = map { it.toDomain() }
}
