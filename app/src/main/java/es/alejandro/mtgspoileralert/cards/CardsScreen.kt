package es.alejandro.mtgspoileralert.cards

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.hilt.navigation.compose.hiltViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import es.alejandro.mtgspoileralert.cards.viewmodel.CardsViewModel
import es.alejandro.mtgspoileralert.cards.viewmodel.ViewState

@Composable
fun CardsScreen(
    viewModel: CardsViewModel = hiltViewModel(),
    set: String?
) {

    DisposableEffect(key1 = Unit) {
        if (!set.isNullOrBlank())
            viewModel.getCardsForSet(set)
        onDispose {}
    }

    val viewState by remember { viewModel.viewState }

    when(val state = viewState) {
        is ViewState.Success -> {
            Text(text = "Success ${state.data}")
        }
        is ViewState.Error -> {
            Text(text = "Error ${state.errorMessage}")
        }
        is ViewState.Loading -> {
            Text(text = "Loading")
        }
    }
}