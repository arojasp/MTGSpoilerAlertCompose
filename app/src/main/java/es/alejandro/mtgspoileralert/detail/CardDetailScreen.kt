package es.alejandro.mtgspoileralert.detail

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.hilt.navigation.compose.hiltViewModel
import es.alejandro.mtgspoileralert.detail.viewmodel.CardDetailViewModel

@Composable
fun CardDetailScreen(
    viewModel: CardDetailViewModel = hiltViewModel(),
    cardId: String?
) {

    DisposableEffect(key1 = Unit) {
        if (!cardId.isNullOrBlank())
            viewModel.getCardDetail(cardId)
        onDispose {  }
    }

    val card by remember { viewModel.card }

    card?.let {
        Text(it.name)
    }

}