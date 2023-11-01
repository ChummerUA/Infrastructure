package com.chummer.infrastructure.preferences.useCases.flow

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import com.chummer.infrastructure.preferences.dataStore
import com.chummer.infrastructure.preferences.useCases.HasPreferenceKey
import com.chummer.infrastructure.usecase.FlowUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlin.coroutines.CoroutineContext

abstract class PreferencesFlowUseCase<Output>(
    id: String,
    context: Context
) : FlowUseCase<Preferences.Key<Output>, Output>(id), HasPreferenceKey<Output> {
    override val coroutineContext: CoroutineContext = Dispatchers.IO
    private val dataStore = context.dataStore

    protected abstract val defaultValue: Output

    override fun invoke(input: Preferences.Key<Output>): Flow<Output> = dataStore.data.map {
        it[key] ?: defaultValue
    }

    operator fun invoke(): Flow<Output> {
        return invoke(key)
    }
}
