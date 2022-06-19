package es.alejandro.mtgspoileralert.sets.viewmodel

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import es.alejandro.mtgspoileralert.datastore.SettingsDataStoreManager
import es.alejandro.mtgspoileralert.sets.model.Set
import es.alejandro.mtgspoileralert.sets.usecase.IGetSetUseCase
import es.alejandro.mtgspoileralert.settings.model.Settings
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class ViewState {
    object Loading : ViewState()
    data class Success(val data: List<Set>) : ViewState()
    data class PreferencesSuccess(val data: Settings) : ViewState()
    data class Error(val errorMessage: String) : ViewState()
}

sealed class ActionState {
    object IgnoreAction : ActionState()
    data class PreferencesAction(val data: Settings) : ActionState()
}

@HiltViewModel
class SetsViewModel @Inject constructor(
    useCase: IGetSetUseCase,
    val dataStoreManager: SettingsDataStoreManager
) : ViewModel() {

    private val _viewState: MutableState<ViewState> = mutableStateOf(
        ViewState.Loading
    )
    private val _actionState: MutableState<ActionState> = mutableStateOf(
        ActionState.IgnoreAction
    )

    val viewState: State<ViewState> = _viewState
    val actionState: State<ActionState> = _actionState

    init {
        viewModelScope.launch {
            dataStoreManager.settings.collect {
                _actionState.value = ActionState.PreferencesAction(it)
            }
        }

        viewModelScope.launch {
            try {
                val sets = useCase()
                _viewState.value = ViewState.Success(sets.data)
            } catch (e: Exception) {
                Log.d("MTGSA", "Exception ${e.message}")
                _viewState.value = ViewState.Error(e.message ?: "Unknown error")
            }
        }
    }
}
