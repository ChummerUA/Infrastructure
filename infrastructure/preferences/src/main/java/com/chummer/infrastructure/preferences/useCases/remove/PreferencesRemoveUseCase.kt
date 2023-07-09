package com.chummer.infrastructure.preferences.useCases.remove

import android.content.Context
import androidx.datastore.preferences.core.edit
import com.chummer.infrastructure.preferences.MapToDomain
import com.chummer.infrastructure.preferences.useCases.HasPreferenceKey
import com.chummer.infrastructure.preferences.useCases.PreferencesUseCase
import kotlinx.coroutines.withContext

abstract class PreferencesRemoveUseCase<Dto, Domain>(
    id: String,
    context: Context
): PreferencesUseCase(id, context), MapToDomain<Dto, Domain>, HasPreferenceKey<Dto> {
    suspend fun remove(): Domain? {
        return withContext(coroutineContext) {
            var result: Domain? = null
            dataStore.edit {
                result = it.remove(key).toDomain()
            }
            result
        }
    }
}
