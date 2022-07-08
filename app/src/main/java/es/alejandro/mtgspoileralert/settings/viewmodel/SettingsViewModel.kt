package es.alejandro.mtgspoileralert.settings.viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import es.alejandro.mtgspoileralert.datastore.SettingsDataStoreManager
import es.alejandro.mtgspoileralert.settings.model.Settings
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import kotlinx.coroutines.flow.collect

sealed class ViewState {
    object Loading : ViewState()
    data class Success(val data: Settings) : ViewState()
    data class Error(val errorMessage: String) : ViewState()
    data class SuccessChange(val data: Settings) : ViewState()
}

@HiltViewModel
class SettingsViewModel @Inject constructor(
    val dataStoreManager: SettingsDataStoreManager
) : ViewModel() {

    private val _viewState: MutableState<ViewState> = mutableStateOf(
        ViewState.Loading
    )
    val viewState: State<ViewState> = _viewState

    init {
        viewModelScope.launch {
            dataStoreManager.settings.collect {
                _viewState.value = ViewState.Success(it)
            }
        }
    }

    fun saveCoreListen(isListening: Boolean) {
        viewModelScope.launch {
            dataStoreManager.setCoreListening(isListening)
        }
    }

    fun saveTimeInterval(interval: Long) {
        viewModelScope.launch {
            dataStoreManager.setTimeInterval(interval)
        }
    }

    fun saveTimeUnit(timeUnit: TimeUnit) {
        viewModelScope.launch {
            dataStoreManager.setTimeUnit(timeUnit)
        }
    }

    fun saveLanguage(languageCode: String){
        viewModelScope.launch {
            dataStoreManager.setPreferredCardLanguage(languageCode)
        }
    }
}
