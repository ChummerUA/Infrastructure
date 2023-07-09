package com.chummer.infrastructure.preferences

interface MapToDomain<T, Domain> {
    fun T.toDomain(): Domain
}

interface MapToDto<T, Dto> {
    fun T.toDto(): Dto
}
