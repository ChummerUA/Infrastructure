package com.chummer.infrastructure.preferences.useCases.get

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import com.chummer.infrastructure.preferences.useCases.ExecutablePreferencesWithKeyAsInputUseCase
import kotlinx.coroutines.flow.first

abstract class PreferencesGetUseCase<Input>(
    id: String,
    context: Context
) : ExecutablePreferencesWithKeyAsInputUseCase<Input, Input>(id, context) {
    abstract val defaultValue: Input

    override suspend fun execute(input: Preferences.Key<Input>): Input {
        return dataStore.data.first()[input] ?: defaultValue
    }

    suspend operator fun invoke(): Input = invoke(key)
}
