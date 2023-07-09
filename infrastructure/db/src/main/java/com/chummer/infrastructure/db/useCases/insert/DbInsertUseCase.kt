package com.chummer.infrastructure.db.useCases.insert

import com.chummer.infrastructure.db.useCases.DbUseCase
import com.chummer.infrastructure.db.MapToDbRow
import com.squareup.sqldelight.Transacter
import kotlinx.coroutines.withContext

abstract class DbInsertUseCase<Domain, Row : Any, QueryTransacter : Transacter>(
    id: String,
    transacter: QueryTransacter
) : DbUseCase<QueryTransacter>(id, transacter), MapToDbRow<Domain, Row> {

    suspend fun insert(item: Domain) {
        withContext(coroutineContext) {
            transacter.insert(item.toRow())
        }
    }

    protected abstract fun QueryTransacter.insert(item: Row)
}
