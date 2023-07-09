package com.chummer.infrastructure.db.useCases.query.parameterless.withResult

import com.chummer.infrastructure.db.MapToDomain
import com.chummer.infrastructure.db.query.parameterless.Query
import com.chummer.infrastructure.db.useCases.DbUseCase
import com.squareup.sqldelight.Transacter
import kotlinx.coroutines.withContext

abstract class DbQueryUseCase<Domain, Row : Any, QueryTransacter : Transacter>(
    id: String,
    transacter: QueryTransacter
) : DbUseCase<QueryTransacter>(id, transacter), Query<Row, QueryTransacter>,
    MapToDomain<Row, Domain> {

    suspend fun execute() {
        withContext(coroutineContext) {
            executeQuery().toDomain()
        }
    }

    protected abstract fun executeQuery(): Row
}
