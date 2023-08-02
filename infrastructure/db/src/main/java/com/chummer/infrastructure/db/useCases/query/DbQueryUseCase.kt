package com.chummer.infrastructure.db.useCases.query

import com.chummer.infrastructure.db.MapToDomain
import com.chummer.infrastructure.db.query.Query
import com.chummer.infrastructure.db.useCases.DbUseCase
import app.cash.sqldelight.Transacter
import kotlinx.coroutines.withContext
import app.cash.sqldelight.Query as SqlQuery

abstract class DbQueryUseCase<QueryArgument, Domain, Row : Any, QueryTransacter : Transacter>(
    id: String,
    transacter: QueryTransacter
) : DbUseCase<QueryTransacter>(id, transacter), Query<QueryArgument, Row, QueryTransacter>,
    MapToDomain<Row, Domain> {

    suspend fun execute(argument: QueryArgument): Domain {
        return withContext(coroutineContext) {
            transacter.getQuery(argument).executeQuery()
        }
    }

    protected abstract fun SqlQuery<Row>.executeQuery(): Domain
}
