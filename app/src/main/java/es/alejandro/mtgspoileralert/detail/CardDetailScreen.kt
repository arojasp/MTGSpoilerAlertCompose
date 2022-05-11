package es.alejandro.mtgspoileralert.detail

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import es.alejandro.mtgspoileralert.detail.model.CardResponse
import es.alejandro.mtgspoileralert.detail.viewmodel.CardDetailViewModel
import es.alejandro.mtgspoileralert.R
import es.alejandro.mtgspoileralert.cards.CardsList
import es.alejandro.mtgspoileralert.detail.viewmodel.ViewState

@Composable
fun CardDetailScreen(
    viewModel: CardDetailViewModel = hiltViewModel(),
    cardId: String?
) {

    DisposableEffect(key1 = Unit) {
        if (!cardId.isNullOrBlank())
            viewModel.getCardDetail(cardId)
        onDispose { }
    }

    val viewState by remember { viewModel.viewState }

    when(val state = viewState) {
        is ViewState.Success -> {
            CardDetail(card = state.data)
        }
        is ViewState.Error -> {
            Text(text = "Error ${state.errorMessage}")
        }
        is ViewState.Loading -> {
            Text(text = "Loading")
        }
    }
}

@OptIn(ExperimentalCoilApi::class)
@Composable
fun CardDetail(card: CardResponse) {
    with(card) {
        Column(modifier = Modifier.padding(16.dp)) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = name)
                Text(text = mana_cost)
            }
            Image(
                painter = rememberImagePainter(data = image_uris.art_crop),
                contentDescription = null
            )
            Text(text = type_line)
            Text(text = oracle_text)
        }
    }
}