package com.chummer.infrastructure.preferences.useCases.flow

import android.content.Context
import com.chummer.infrastructure.preferences.MapToDomain
import com.chummer.infrastructure.preferences.useCases.HasPreferenceKey
import com.chummer.infrastructure.preferences.useCases.PreferencesUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

abstract class PreferencesFlowUseCase<Dto, Domain>(
    id: String,
    context: Context
) : PreferencesUseCase(id, context), MapToDomain<Dto, Domain>, HasPreferenceKey<Dto> {
    fun flow(defaultValue: Dto): Flow<Domain> {
        return dataStore.data.map {
            (it[key] ?: defaultValue).toDomain()
        }
    }
}
