package com.chummer.infrastructure.preferences.useCases.update

import android.content.Context
import androidx.datastore.preferences.core.edit
import com.chummer.infrastructure.preferences.MapToDto
import com.chummer.infrastructure.preferences.useCases.HasPreferenceKey
import com.chummer.infrastructure.preferences.useCases.PreferencesUseCase
import kotlinx.coroutines.withContext

abstract class PreferencesUpdateUseCase<Domain, Dto>(
    id: String,
    context: Context
): PreferencesUseCase(id, context), MapToDto<Domain, Dto>, HasPreferenceKey<Dto> {
    suspend fun update(value: Domain) {
        withContext(coroutineContext) {
            dataStore.edit {
                it[key] = value.toDto()
            }
        }
    }
}
