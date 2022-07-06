package es.alejandro.mtgspoileralert.datastore

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import es.alejandro.mtgspoileralert.settings.model.Settings
import es.alejandro.mtgspoileralert.util.PreferencesConstant
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.concurrent.TimeUnit
import javax.inject.Inject

private val Context.dataStore by preferencesDataStore(PreferencesConstant.Settings.SETTINGS_KEY)

class SettingsDataStoreManager @Inject constructor(@ApplicationContext appContext: Context) {

    companion object {
        val CORE_LISTEN = booleanPreferencesKey(PreferencesConstant.Settings.CORE_LISTEN_KEY)
        val INTERVAL = longPreferencesKey(PreferencesConstant.Settings.INTERVAL_KEY)
        val TIME_UNIT = stringPreferencesKey(PreferencesConstant.Settings.TIME_UNIT_KEY)
    }

    private val settingsDataStore = appContext.dataStore

    suspend fun setSettings(settings: Settings) {
        settingsDataStore.edit { settingsPreferences ->
            settingsPreferences[CORE_LISTEN] = settings.coreSetListen
            settingsPreferences[INTERVAL] = settings.interval.first
            settingsPreferences[TIME_UNIT] = settings.interval.second.toString()
        }
    }

    suspend fun setCoreListening(isListening: Boolean) {
        settingsDataStore.edit { settingsDataStore ->
            settingsDataStore[CORE_LISTEN] = isListening
        }
    }

    suspend fun setTimeInterval(interval: Long) {
        settingsDataStore.edit { settingsDataStore ->
            settingsDataStore[INTERVAL] = interval
        }
    }

    suspend fun setTimeUnit(timeUnit: TimeUnit) {
        settingsDataStore.edit { settingsDataStore ->
            settingsDataStore[TIME_UNIT] = timeUnit.toString()
        }
    }

    val settings: Flow<Settings> = settingsDataStore.data.map { settingsPreferences ->
        Settings(
            coreSetListen = settingsPreferences[CORE_LISTEN] ?: false,
            interval = Pair(
                settingsPreferences[INTERVAL] ?: 15,
                TimeUnit.valueOf(settingsPreferences[TIME_UNIT] ?: TimeUnit.MINUTES.toString())
            )
        )
    }
}
