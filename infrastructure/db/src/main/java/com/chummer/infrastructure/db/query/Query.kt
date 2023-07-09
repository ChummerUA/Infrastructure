package com.chummer.infrastructure.db.query

import com.squareup.sqldelight.Transacter
import com.squareup.sqldelight.Query as SqlQuery

interface Query<QueryArgument, Row: Any, QueryTransacter : Transacter> {
    fun QueryTransacter.getQuery(argument: QueryArgument) : SqlQuery<Row>
}
