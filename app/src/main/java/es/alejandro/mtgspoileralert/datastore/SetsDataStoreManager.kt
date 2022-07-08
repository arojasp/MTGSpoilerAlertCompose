package es.alejandro.mtgspoileralert.datastore

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import es.alejandro.mtgspoileralert.util.PreferencesConstant
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

private val Context.dataStore by preferencesDataStore(PreferencesConstant.Set.SETS_KEY)

class SetsDataStoreManager @Inject constructor(@ApplicationContext appContext: Context) {

    companion object {
        val LAST_SET = stringPreferencesKey(PreferencesConstant.Set.LAST_SET_KEY)
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
