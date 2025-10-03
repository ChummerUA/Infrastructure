package com.chummer.infrastructure.preferences.useCases.clear

import android.content.Context
import androidx.datastore.preferences.core.edit
import com.chummer.infrastructure.preferences.useCases.ExecutablePreferencesUseCase
import com.chummer.infrastructure.usecase.UseCaseLogger

class PreferencesClearUseCase(
    id: String,
    context: Context,
    logger: UseCaseLogger
) : ExecutablePreferencesUseCase<Unit, Unit>(id, context, logger) {

    override suspend fun execute(input: Unit) {
        dataStore.edit {
            it.clear()
        }
    }
}