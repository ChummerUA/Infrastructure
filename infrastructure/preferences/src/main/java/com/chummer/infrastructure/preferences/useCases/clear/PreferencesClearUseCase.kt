package com.chummer.infrastructure.preferences.useCases.clear

import android.content.Context
import androidx.datastore.preferences.core.edit
import com.chummer.infrastructure.preferences.useCases.ExecutablePreferencesUseCase

class PreferencesClearUseCase(
    id: String,
    context: Context
) : ExecutablePreferencesUseCase<Unit, Unit>(id, context) {

    override suspend fun execute(input: Unit) {
        dataStore.edit {
            it.clear()
        }
    }
}