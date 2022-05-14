package es.alejandro.mtgspoileralert.datastore

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

private val Context.dataStore by preferencesDataStore("settings")

class DataStoreManager @Inject constructor(@ApplicationContext appContext: Context) {

    companion object {
        val CORE_LISTEN = booleanPreferencesKey("core_listen")
        val COMMANDER_LISTEN = booleanPreferencesKey("core_listen")
    }

    private val preferencesDataStore = appContext.dataStore

    suspend fun setCoreListening(isListening: Boolean) {
        preferencesDataStore.edit { settings ->
            settings[CORE_LISTEN] = isListening
        }
    }

    val isCoreListening: Flow<Boolean> = preferencesDataStore.data.map { preferences ->
        preferences[CORE_LISTEN] ?: false
    }

    suspend fun setCommanderListening(isListening: Boolean) {
        preferencesDataStore.edit { settings ->
            settings[COMMANDER_LISTEN] = isListening
        }
    }

    val isCommanderListening: Flow<Boolean> = preferencesDataStore.data.map { preferences ->
        preferences[COMMANDER_LISTEN] ?: false
    }

}