package es.alejandro.mtgspoileralert.detail.viewmodel

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import es.alejandro.mtgspoileralert.db.MTGSpoilerAlertDao
import es.alejandro.mtgspoileralert.detail.model.CardResponse
import es.alejandro.mtgspoileralert.detail.usecase.GetCardDetailUseCase
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class ViewState {
    object Loading : ViewState()
    data class Success(val data: CardResponse) : ViewState()
    data class Error(val errorMessage: String) : ViewState()
}

@HiltViewModel
class CardDetailViewModel @Inject constructor(
    val useCase: GetCardDetailUseCase,
    val dao: MTGSpoilerAlertDao
) : ViewModel() {

    private val _viewState: MutableState<ViewState> = mutableStateOf(ViewState.Loading)
    val viewState: State<ViewState> = _viewState

    fun getCardDetail(cardId: String) {
        viewModelScope.launch {
            try {
                val cardDetail = useCase(cardId)
                // dao.saveCard(cardDetail)
                _viewState.value = ViewState.Success(cardDetail)
            } catch (e: Exception) {
                Log.e("MTGA", e.message ?: "")
                _viewState.value = ViewState.Error(e.message ?: "Unknown error")
            }
        }
    }
}
