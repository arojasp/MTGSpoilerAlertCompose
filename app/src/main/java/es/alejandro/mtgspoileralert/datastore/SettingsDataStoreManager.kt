package es.alejandro.mtgspoileralert.datastore

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.concurrent.TimeUnit
import javax.inject.Inject

private val Context.dataStore by preferencesDataStore("settings")

class SettingsDataStoreManager @Inject constructor(@ApplicationContext appContext: Context) {

    companion object {
        val CORE_LISTEN = booleanPreferencesKey("core_listen")
        val COMMANDER_LISTEN = booleanPreferencesKey("core_listen")
        val INTERVAL = intPreferencesKey("interval")
        val TIME_UNIT = longPreferencesKey("time_unit")
    }

    private val settingsDataStore = appContext.dataStore

    suspend fun setCoreListening(isListening: Boolean) {
        settingsDataStore.edit { settings ->
            settings[CORE_LISTEN] = isListening
        }
    }

    val isCoreListening: Flow<Boolean> = settingsDataStore.data.map { preferences ->
        preferences[CORE_LISTEN] ?: false
    }

    suspend fun setCommanderListening(isListening: Boolean) {
        settingsDataStore.edit { settings ->
            settings[COMMANDER_LISTEN] = isListening
        }
    }

    val isCommanderListening: Flow<Boolean> = settingsDataStore.data.map { preferences ->
        preferences[COMMANDER_LISTEN] ?: false
    }

}