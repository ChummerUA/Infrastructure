package com.chummer.infrastructure.network.useCase.request.withParameter

import io.ktor.client.request.HttpRequestBuilder

interface ConfigureRequest<RequestParameter> {
    fun HttpRequestBuilder.configureRequest(parameter: RequestParameter)
}
