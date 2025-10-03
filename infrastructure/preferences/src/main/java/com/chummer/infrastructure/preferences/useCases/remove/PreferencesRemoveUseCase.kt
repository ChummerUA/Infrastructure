package com.chummer.infrastructure.preferences.useCases.remove

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.chummer.infrastructure.preferences.useCases.ExecutablePreferencesWithKeyAsInputUseCase
import com.chummer.infrastructure.usecase.UseCaseLogger

abstract class PreferencesRemoveUseCase<KeyType>(
    id: String,
    context: Context,
    logger: UseCaseLogger,
): ExecutablePreferencesWithKeyAsInputUseCase<KeyType, KeyType?>(id, context, logger) {
    override suspend fun execute(input: Preferences.Key<KeyType>): KeyType? {
        var result: KeyType? = null
        dataStore.edit {
            try {
                result = it.remove(key)
            }
            catch (e: Throwable) { }
        }
        return result
    }

    suspend operator fun invoke(): KeyType? = invoke(key)
}
