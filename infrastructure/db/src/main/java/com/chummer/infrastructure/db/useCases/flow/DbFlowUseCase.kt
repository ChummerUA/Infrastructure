package com.chummer.infrastructure.db.useCases.flow

import com.chummer.infrastructure.db.useCases.DbUseCase
import com.chummer.infrastructure.db.MapToDomain
import com.chummer.infrastructure.db.query.Query
import com.squareup.sqldelight.Transacter
import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToOne
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import com.squareup.sqldelight.Query as SqlQuery

abstract class DbFlowUseCase<QueryArgument, Domain : Any, Row : Any, QueryTransacter : Transacter>(
    id: String,
    transacter: QueryTransacter
) : DbUseCase<QueryTransacter>(id, transacter),
    Query<QueryArgument, Row, QueryTransacter>, MapToDomain<Row, Domain> {

    fun flow(parameter: QueryArgument): Flow<Domain> {
        return transacter.getQuery(parameter).toFlow().mapToDomain()
    }

    private fun SqlQuery<Row>.toFlow(): Flow<Row> {
        return this.asFlow().mapToOne()
    }

    private fun Flow<Row>.mapToDomain() = map { it.toDomain() }
}
