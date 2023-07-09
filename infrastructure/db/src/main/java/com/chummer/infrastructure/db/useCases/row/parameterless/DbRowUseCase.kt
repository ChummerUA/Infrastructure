package com.chummer.infrastructure.db.useCases.row.parameterless

import com.chummer.infrastructure.db.useCases.DbUseCase
import com.chummer.infrastructure.db.MapToDomain
import com.chummer.infrastructure.db.query.parameterless.Query
import com.squareup.sqldelight.Transacter
import kotlinx.coroutines.withContext

abstract class DbRowWithoutArgumentUseCase<Domain : Any, Row : Any, QueryTransacter : Transacter>(
    id: String,
    transacter: QueryTransacter
) : DbUseCase<QueryTransacter>(id, transacter),
    Query<Row, QueryTransacter>, MapToDomain<Row, Domain> {

    suspend fun execute(): Domain {
        return withContext(coroutineContext) {
            transacter.getQuery().executeAsOne().toDomain()
        }
    }

    suspend fun executeNullable(): Domain? {
        return withContext(coroutineContext) {
            transacter.getQuery().executeAsOneOrNull()?.toDomain()
        }
    }
}
