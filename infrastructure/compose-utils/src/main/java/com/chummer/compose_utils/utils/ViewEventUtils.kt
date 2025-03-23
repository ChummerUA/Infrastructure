package com.chummer.compose_utils.utils

typealias OnClickListener = (() -> Unit)

typealias TypedOnClickListener<Input, Output> = ((Input) -> Output)

typealias OnTextChanged = ((String) -> Unit)
