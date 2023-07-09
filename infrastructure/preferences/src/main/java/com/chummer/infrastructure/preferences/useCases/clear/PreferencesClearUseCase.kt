package com.chummer.infrastructure.preferences.useCases.clear

import android.content.Context
import androidx.datastore.preferences.core.edit
import com.chummer.infrastructure.preferences.useCases.PreferencesUseCase
import kotlinx.coroutines.withContext

class PreferencesClearUseCase(
    id: String,
    context: Context
) : PreferencesUseCase(id, context) {
    suspend fun clear() {
        withContext(coroutineContext) {
            dataStore.edit {
                it.clear()
            }
        }
    }
}