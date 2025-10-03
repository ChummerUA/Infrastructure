package com.chummer.infrastructure.preferences.useCases

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import com.chummer.infrastructure.preferences.dataStore
import com.chummer.infrastructure.usecase.ExecutableUseCase
import com.chummer.infrastructure.usecase.UseCaseLogger
import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

abstract class ExecutablePreferencesUseCase<Input, Output>(
    id: String,
    context: Context,
    logger: UseCaseLogger
) : ExecutableUseCase<Input, Output>(id, logger) {
    override val coroutineContext: CoroutineContext = Dispatchers.IO

    protected val dataStore = context.dataStore
}

abstract class ExecutablePreferencesWithKeyAsInputUseCase<Input, Output>(
    id: String,
    context: Context,
    logger: UseCaseLogger
) : ExecutableUseCase<Preferences.Key<Input>, Output>(id, logger), HasPreferenceKey<Input> {
    override val coroutineContext: CoroutineContext = Dispatchers.IO

    protected val dataStore = context.dataStore
}

interface HasPreferenceKey<Key> {
    val key: Preferences.Key<Key>
}
