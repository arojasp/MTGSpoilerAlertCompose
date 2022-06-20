package es.alejandro.mtgspoileralert.datastore

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

private val Context.dataStore by preferencesDataStore("sets")

class SetsDataStoreManager @Inject constructor(@ApplicationContext appContext: Context) {

    companion object {
        val LAST_SET = stringPreferencesKey("last_set")
    }

    private val setsDataStore = appContext.dataStore

    suspend fun setLastSet(lastSet: String) {
        setsDataStore.edit { dataStore ->
            dataStore[LAST_SET] = lastSet
        }
    }

    val sets: Flow<String> = setsDataStore.data.map {
        it[LAST_SET] ?: ""
    }
}
