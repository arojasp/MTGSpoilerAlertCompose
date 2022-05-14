package es.alejandro.mtgspoileralert.settings.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import es.alejandro.mtgspoileralert.datastore.DataStoreManager
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    val dataStoreManager: DataStoreManager
): ViewModel() {

    fun saveCommanderListen(isListening: Boolean) {
        viewModelScope.launch {
            dataStoreManager.setCommanderListening(isListening)
        }
    }

}