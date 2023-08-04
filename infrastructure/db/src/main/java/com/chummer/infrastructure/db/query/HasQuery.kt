package com.chummer.infrastructure.db.query

import app.cash.sqldelight.Transacter
import app.cash.sqldelight.Query as SqlQuery

interface HasQuery<QueryArgument, Row : Any, QueryTransacter : Transacter> {
    fun QueryTransacter.getQuery(argument: QueryArgument): SqlQuery<Row>
}
