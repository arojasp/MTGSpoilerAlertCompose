package es.alejandro.mtgspoileralert.datastore

import android.content.Context
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import es.alejandro.mtgspoileralert.settings.model.Settings
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
        val TIME_UNIT = stringPreferencesKey("time_unit")
    }

    private val settingsDataStore = appContext.dataStore

    suspend fun setSettings(settings: Settings) {
        settingsDataStore.edit { settingsPreferences ->
            settingsPreferences[CORE_LISTEN] = settings.coreSetListen
            settingsPreferences[COMMANDER_LISTEN] = settings.commanderSetListen
            settingsPreferences[INTERVAL] = settings.interval.first
            settingsPreferences[TIME_UNIT] = settings.interval.second.toString()
        }
    }

    val settings: Flow<Settings> = settingsDataStore.data.map { settingsPreferences ->
        Settings(
            coreSetListen = settingsPreferences[CORE_LISTEN] ?: false,
            commanderSetListen = settingsPreferences[COMMANDER_LISTEN] ?: false,
            interval = Pair(
                settingsPreferences[INTERVAL] ?: 15,
                TimeUnit.valueOf(settingsPreferences[TIME_UNIT] ?: TimeUnit.MINUTES.toString())
            )
        )

    }

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