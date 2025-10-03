package com.chummer.infrastructure.preferences.useCases.update

import android.content.Context
import androidx.datastore.preferences.core.edit
import com.chummer.infrastructure.preferences.useCases.ExecutablePreferencesUseCase
import com.chummer.infrastructure.preferences.useCases.HasPreferenceKey
import com.chummer.infrastructure.usecase.UseCaseLogger

abstract class PreferencesUpdateUseCase<Input>(
    id: String,
    context: Context,
    logger: UseCaseLogger
): ExecutablePreferencesUseCase<Input, Unit>(id, context, logger), HasPreferenceKey<Input> {
    override suspend fun execute(input: Input) {
        dataStore.edit {
            it[key] = input
        }
    }
}
