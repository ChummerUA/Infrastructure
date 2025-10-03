package com.chummer.infrastructure.preferences.useCases.get

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import com.chummer.infrastructure.preferences.useCases.ExecutablePreferencesWithKeyAsInputUseCase
import com.chummer.infrastructure.usecase.UseCaseLogger
import kotlinx.coroutines.flow.first

abstract class PreferencesGetUseCase<Input>(
    id: String,
    context: Context,
    logger: UseCaseLogger
) : ExecutablePreferencesWithKeyAsInputUseCase<Input, Input>(id, context, logger) {
    abstract val defaultValue: Input

    override suspend fun execute(input: Preferences.Key<Input>): Input {
        return dataStore.data.first()[input] ?: defaultValue
    }

    suspend operator fun invoke(): Input = invoke(key)
}
