package com.chummer.infrastructure.preferences.useCases

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import com.chummer.infrastructure.preferences.dataStore
import com.chummer.infrastructure.usecase.SuspendUseCase
import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

abstract class PreferencesUseCase(
    id: String,
    context: Context
): SuspendUseCase(id) {
    override val coroutineContext: CoroutineContext = Dispatchers.IO

    protected val dataStore = context.dataStore
}

interface HasPreferenceKey<Dto> {
    val key: Preferences.Key<Dto>
}
