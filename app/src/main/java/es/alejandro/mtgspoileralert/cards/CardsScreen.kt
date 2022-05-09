package es.alejandro.mtgspoileralert.cards

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import coil.decode.SvgDecoder
import coil.size.OriginalSize
import dagger.hilt.android.lifecycle.HiltViewModel
import es.alejandro.mtgspoileralert.cards.model.Card
import es.alejandro.mtgspoileralert.cards.viewmodel.CardsViewModel
import es.alejandro.mtgspoileralert.cards.viewmodel.ViewState
import es.alejandro.mtgspoileralert.sets.model.Set

@Composable
fun CardsScreen(
    viewModel: CardsViewModel = hiltViewModel(),
    set: String?,
    onCardClick: (String) -> Unit
) {

    DisposableEffect(key1 = Unit) {
        if (!set.isNullOrBlank())
            viewModel.getCardsForSet(set)
        onDispose {}
    }

    val viewState by remember { viewModel.viewState }

    when(val state = viewState) {
        is ViewState.Success -> {
            CardsList(state.data) { cardId ->
                onCardClick(cardId)
            }
        }
        is ViewState.Error -> {
            Text(text = "Error ${state.errorMessage}")
        }
        is ViewState.Loading -> {
            Text(text = "Loading")
        }
    }
}

@Composable
fun CardsList(cards: List<Card>, onCardClick: (String) -> Unit) {
    LazyColumn {
        items(cards) { item ->
            SingleCardItem(item) { cardId ->
                onCardClick(cardId)
            }
        }
    }
}

@OptIn(ExperimentalCoilApi::class)
@Composable
fun SingleCardItem(
    card: Card,
    onClick: (String) -> Unit
) {
    Card(modifier = Modifier
        .padding(8.dp)
        .fillMaxWidth()
        .clickable { onClick(card.id) }, elevation = 8.dp) {
        Image(
            modifier = Modifier.padding(16.dp).fillMaxSize(), painter = rememberImagePainter(
                card.image_uris.normal,
                builder = {
                    size(OriginalSize)
                }
            ), contentDescription = null, contentScale = ContentScale.Crop)
    }
}