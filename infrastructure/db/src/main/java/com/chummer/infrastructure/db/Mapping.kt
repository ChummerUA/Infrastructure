package com.chummer.infrastructure.db

interface MapToDomain<T: Any, Domain> {
    fun T.toDomain(): Domain
}

interface MapToDbRow<T, Row: Any> {
    fun T.toRow(): Row
}
