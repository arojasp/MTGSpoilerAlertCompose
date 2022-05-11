package es.alejandro.mtgspoileralert.cards

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
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

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CardsList(cards: List<Card>, onCardClick: (String) -> Unit) {
    LazyVerticalGrid(cells = GridCells.Fixed(2)) {
        items(cards) { item ->
            SingleCardItem(item) { cardId ->
                onCardClick(cardId)
            }
        }
    }
}

@OptIn(ExperimentalCoilApi::class, ExperimentalFoundationApi::class)
@Composable
fun SingleCardItem(
    card: Card,
    onClick: (String) -> Unit
) {

    var showDialog by remember { mutableStateOf(false) }
    if (showDialog) {
        DialogCard(card.image_uris.normal) {
            showDialog = false
        }
    }

    Card(modifier = Modifier
        .padding(8.dp)
        .fillMaxWidth()
        .combinedClickable(
            onClick = { onClick(card.id) },
            onLongClick = { showDialog = true }
        ), elevation = 8.dp) {
        Image(
            modifier = Modifier.fillMaxSize(), painter = rememberImagePainter(
                card.image_uris.normal,
                builder = {
                    size(OriginalSize)
                }
            ), contentDescription = null, contentScale = ContentScale.Crop)
    }
}

@OptIn(ExperimentalCoilApi::class)
@Composable
fun DialogCard(imageUri: String, onClose: () -> Unit){
    Dialog(
        onDismissRequest = onClose,
        DialogProperties(dismissOnBackPress = true, dismissOnClickOutside = true)
    ) {
        Image(painter = rememberImagePainter(data = imageUri), contentDescription = null)
    }
}