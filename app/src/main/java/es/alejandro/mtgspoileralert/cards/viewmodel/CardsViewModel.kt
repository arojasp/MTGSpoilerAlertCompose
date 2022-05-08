package es.alejandro.mtgspoileralert.cards.viewmodel

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import es.alejandro.mtgspoileralert.cards.model.Card
import es.alejandro.mtgspoileralert.cards.usecase.IGetCardsUseCase
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

sealed class ViewState {
    object Loading: ViewState()
    data class Success(val data: List<Card>): ViewState()
    data class Error(val errorMessage: String): ViewState()

}

@HiltViewModel
class CardsViewModel @Inject constructor(
    val useCase: IGetCardsUseCase
): ViewModel() {

    init {

    }

    private val _viewState: MutableState<ViewState> = mutableStateOf(ViewState.Loading)
    val viewState: State<ViewState> = _viewState

    fun getCardsForSet(setCode: String) {
        viewModelScope.launch {
            try{
                val cardsResponse = useCase(setCode)
                _viewState.value = ViewState.Success(cardsResponse.data)
            }catch (e: Exception) {
                Log.d("MTGSA", "Exception ${e.message}")
                _viewState.value = ViewState.Error(e.message ?: "Unknown error")
            }
        }
    }
}