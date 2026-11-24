package com.chummer.infrastructure.db.useCases.flow

import app.cash.sqldelight.Transacter
import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToOneOrNull
import com.chummer.infrastructure.db.query.HasQuery
import com.chummer.infrastructure.usecase.FlowUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.mapLatest
import kotlin.coroutines.CoroutineContext
import app.cash.sqldelight.Query as SqlQuery

abstract class DbItemFlowUseCase<QueryArgument, Row : Any, QueryTransacter : Transacter>(
    id: String,
    protected val transacter: QueryTransacter,
) : FlowUseCase<QueryArgument, Row>(id), HasQuery<QueryArgument, Row, QueryTransacter> {

    override val coroutineContext: CoroutineContext = Dispatchers.IO

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun execute(input: QueryArgument): Flow<Row?> {
        getKeyQuery(input)?.let {
            return it
                .asFlow()
                .mapToOneOrNull(coroutineContext)
                .distinctUntilChanged()
                .mapLatest {
                    transacter.getQuery(input).executeAsOneOrNull()
                }
        }
        return transacter.getQuery(input).toFlow()
    }

    open fun getKeyQuery(input: QueryArgument): SqlQuery<Any>? { return null }

    protected open fun SqlQuery<Row>.toFlow(): Flow<Row?> {
        return this.asFlow().mapToOneOrNull(coroutineContext)
    }
}
