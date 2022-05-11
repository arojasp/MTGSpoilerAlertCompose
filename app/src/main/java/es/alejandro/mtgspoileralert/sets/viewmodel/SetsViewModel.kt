package es.alejandro.mtgspoileralert.sets.viewmodel

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import es.alejandro.mtgspoileralert.cards.model.Card
import es.alejandro.mtgspoileralert.sets.model.Set
import es.alejandro.mtgspoileralert.sets.usecase.IGetSetUseCase
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

sealed class ViewState {
    object Loading: ViewState()
    data class Success(val data: List<Set>): ViewState()
    data class Error(val errorMessage: String): ViewState()
}

@HiltViewModel
class SetsViewModel @Inject constructor(
    useCase: IGetSetUseCase
) : ViewModel() {

    private val _viewState: MutableState<ViewState> = mutableStateOf(
        ViewState.Loading)
    val viewState: State<ViewState> = _viewState

    init {
        viewModelScope.launch {
            try{
                val sets = useCase()
                _viewState.value = ViewState.Success(sets.data)
            }catch (e: Exception) {
                Log.d("MTGSA", "Exception ${e.message}")
                _viewState.value = ViewState.Error(e.message ?: "Unknown error")
            }
        }
    }
}