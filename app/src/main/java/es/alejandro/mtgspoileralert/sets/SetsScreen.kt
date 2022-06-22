package es.alejandro.mtgspoileralert.sets

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import es.alejandro.mtgspoileralert.sets.model.Set
import es.alejandro.mtgspoileralert.sets.viewmodel.ActionState
import es.alejandro.mtgspoileralert.sets.viewmodel.SetsViewModel
import es.alejandro.mtgspoileralert.sets.viewmodel.ViewState
import es.alejandro.mtgspoileralert.settings.model.Settings

@Composable
fun SetsScreen(
    viewModel: SetsViewModel = hiltViewModel(),
    onItemClick: (String) -> Unit,
    preferencesAction: (Settings) -> Unit
) {

    val viewState by remember { viewModel.viewState }
    val actionState by remember { viewModel.actionState }

    when (val state = viewState) {
        is ViewState.Success -> {
            Column {
                LazyColumn {
                    items(state.data) { item ->
                        SingleSetItem(set = item) {
                            onItemClick(it)
                        }
                    }
                }
            }
        }
        is ViewState.Error -> {
            Text(text = "Error ${state.errorMessage}")
        }
        is ViewState.Loading -> {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CircularProgressIndicator(modifier = Modifier.size(100.dp))
            }
        }
    }
    when (val state = actionState) {
        is ActionState.PreferencesAction -> {
            preferencesAction(state.data)
        }
    }
}

@Composable
fun SingleSetItem(
    set: Set,
    onClick: (String) -> Unit
) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .clickable { onClick(set.code) },
        elevation = 8.dp
    ) {
        Row(modifier = Modifier.padding(8.dp), verticalAlignment = Alignment.CenterVertically) {
            AsyncImage(
                modifier = Modifier
                    .size(60.dp)
                    .padding(8.dp),
                model = ImageRequest.Builder(LocalContext.current)
                    .data(set.icon_svg_uri)
                    .decoderFactory(SvgDecoder.Factory())
                    .build(),
                contentDescription = null
            )
            Column {
                Text(
                    text = set.name,
                    style = MaterialTheme.typography.h6,
                    color = MaterialTheme.colors.primary
                )
                Text(
                    text = set.code.uppercase(),
                    style = MaterialTheme.typography.body1,
                    color = MaterialTheme.colors.secondary
                )
            }
        }
    }
}
