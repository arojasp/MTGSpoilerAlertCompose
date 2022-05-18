package es.alejandro.mtgspoileralert.settings.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import es.alejandro.mtgspoileralert.datastore.SettingsDataStoreManager
import es.alejandro.mtgspoileralert.settings.model.Settings
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class ViewState {
    object Loading: ViewState()
    data class Success(val data: Settings): ViewState()
    data class Error(val errorMessage: String): ViewState()
}

@HiltViewModel
class SettingsViewModel @Inject constructor(
    val dataStoreManager: SettingsDataStoreManager
): ViewModel() {

    init {
        //todo get data from storemanager and load onto the view
    }

    fun saveCommanderListen(isListening: Boolean) {
        viewModelScope.launch {
            dataStoreManager.setCommanderListening(isListening)
        }
    }

}