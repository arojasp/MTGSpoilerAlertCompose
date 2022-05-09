package es.alejandro.mtgspoileralert.detail.viewmodel

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import es.alejandro.mtgspoileralert.cards.viewmodel.ViewState
import es.alejandro.mtgspoileralert.db.MTGSpoilerAlertDao
import es.alejandro.mtgspoileralert.detail.model.CardResponse
import es.alejandro.mtgspoileralert.detail.usecase.GetCardDetailUseCase
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class CardDetailViewModel @Inject constructor(
        val useCase: GetCardDetailUseCase,
        val dao: MTGSpoilerAlertDao
): ViewModel() {

    private val _card: MutableState<CardResponse?> = mutableStateOf(null)
    val card: State<CardResponse?> = _card

    fun getCardDetail(cardId: String) {
        viewModelScope.launch {
            try {
                val cardDetail = useCase(cardId)
                dao.saveCard(cardDetail)
                _card.value = cardDetail
            } catch (e: Exception) {
                Log.e("MTGA", e.message ?: "")
            }
        }
    }

    fun saveToFavourites(card: CardResponse) {
        viewModelScope.launch {
            dao.saveCard(card)
        }
    }
}