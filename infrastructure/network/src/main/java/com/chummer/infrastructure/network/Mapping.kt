package com.chummer.infrastructure.network

import io.ktor.client.statement.HttpResponse

interface MapToResult<Result> {
    fun HttpResponse.toResult(): Result
}

interface MapToError<NetworkError: Throwable> {
    fun HttpResponse.toError(): NetworkError
}

interface MapToDomain<Domain, NetworkResult> {
    fun NetworkResult.toDomain(): Domain
}
