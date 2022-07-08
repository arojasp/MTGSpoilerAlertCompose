package es.alejandro.mtgspoileralert.detail

import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import es.alejandro.mtgspoileralert.detail.model.CardResponse
import es.alejandro.mtgspoileralert.detail.viewmodel.CardDetailViewModel
import es.alejandro.mtgspoileralert.detail.viewmodel.ViewState
import es.alejandro.mtgspoileralert.R

@Composable
fun CardDetailScreen(
    paddingValues: PaddingValues,
    viewModel: CardDetailViewModel = hiltViewModel(),
    cardId: String?
) {

    DisposableEffect(key1 = Unit) {
        if (!cardId.isNullOrBlank())
            viewModel.getCardDetail(cardId)
        onDispose { }
    }

    val viewState by remember { viewModel.viewState }

    when (val state = viewState) {
        is ViewState.Success -> {
            CardDetail(paddingValues, card = state.data)
        }
        is ViewState.Error -> {
            Text(modifier = Modifier.padding(paddingValues), text = stringResource(id = R.string.error, state.errorMessage))
        }
        is ViewState.Loading -> {
            Column(
                modifier = Modifier.fillMaxSize().padding(paddingValues),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CircularProgressIndicator(modifier = Modifier.size(100.dp))
            }
        }
    }
}

@Composable
fun CardDetail(paddingValues: PaddingValues, card: CardResponse) {
    with(card) {
        Column(modifier = Modifier.padding(paddingValues).padding(16.dp)) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = name,
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = mana_cost ?: "",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            Spacer(modifier = Modifier.size(20.dp))
            Text(
                text = type_line,
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.size(20.dp))
            Text(
                text = oracle_text ?: "",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}
