package com.chummer.infrastructure.db.useCases.flow.parameterless

import com.chummer.infrastructure.db.MapToDomain
import com.chummer.infrastructure.db.query.parameterless.Query
import com.chummer.infrastructure.db.useCases.DbUseCase
import com.squareup.sqldelight.Transacter
import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToOne
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import com.squareup.sqldelight.Query as SqlQuery

abstract class DbFlowUseCase<Domain : Any, Row : Any, QueryTransacter : Transacter>(
    id: String,
    transacter: QueryTransacter
) : DbUseCase<QueryTransacter>(id, transacter),
    Query<Row, QueryTransacter>, MapToDomain<Row, Domain> {

    fun flow(): Flow<Domain> {
        return transacter.getQuery().toFlow().mapToDomain()
    }

    private fun SqlQuery<Row>.toFlow(): Flow<Row> {
        return this.asFlow().mapToOne()
    }

    private fun Flow<Row>.mapToDomain() = map { it.toDomain() }
}
