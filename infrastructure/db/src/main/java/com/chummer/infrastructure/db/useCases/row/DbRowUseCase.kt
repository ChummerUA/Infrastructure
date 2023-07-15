package com.chummer.infrastructure.db.useCases.row

import com.chummer.infrastructure.db.useCases.DbUseCase
import com.chummer.infrastructure.db.MapToDomain
import com.chummer.infrastructure.db.query.Query
import com.squareup.sqldelight.Transacter
import kotlinx.coroutines.withContext

abstract class DbRowUseCase<QueryArgument, Domain, Row : Any, QueryTransacter : Transacter>(
    id: String,
    transacter: QueryTransacter
) : DbUseCase<QueryTransacter>(id, transacter),
    Query<QueryArgument, Row, QueryTransacter>, MapToDomain<Row, Domain> {

    suspend fun execute(argument: QueryArgument): Domain {
        return withContext(coroutineContext) {
            transacter.getQuery(argument).executeAsOne().toDomain()
        }
    }

    suspend fun executeNullable(argument: QueryArgument): Domain? {
        return withContext(coroutineContext) {
            transacter.getQuery(argument).executeAsOneOrNull()?.toDomain()
        }
    }
}