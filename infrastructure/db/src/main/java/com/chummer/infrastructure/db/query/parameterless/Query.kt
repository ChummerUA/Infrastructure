package com.chummer.infrastructure.db.query.parameterless

import com.squareup.sqldelight.Query
import com.squareup.sqldelight.Transacter


interface Query<Row : Any, QueryTransacter : Transacter> {
    fun QueryTransacter.getQuery(): Query<Row>
}
