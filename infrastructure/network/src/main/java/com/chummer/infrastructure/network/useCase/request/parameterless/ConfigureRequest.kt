package com.chummer.infrastructure.network.useCase.request.parameterless

import io.ktor.client.request.HttpRequestBuilder

interface ConfigureRequest {
    fun HttpRequestBuilder.configureRequest()
}
