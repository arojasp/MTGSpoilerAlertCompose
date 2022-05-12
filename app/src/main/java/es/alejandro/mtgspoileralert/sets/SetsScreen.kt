package es.alejandro.mtgspoileralert.sets

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.toUpperCase
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import coil.decode.SvgDecoder
import es.alejandro.mtgspoileralert.detail.CardDetail
import es.alejandro.mtgspoileralert.sets.model.Set
import es.alejandro.mtgspoileralert.sets.viewmodel.SetsViewModel
import es.alejandro.mtgspoileralert.sets.viewmodel.ViewState
import es.alejandro.mtgspoileralert.ui.theme.MTGSpoilerAlertTheme
import java.util.*

@Composable
fun SetsScreen(
    viewModel: SetsViewModel = hiltViewModel(),
    onItemClick: (String) -> Unit
) {

    val viewState by remember { viewModel.viewState }

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
            Column(modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally) {
                CircularProgressIndicator(modifier = Modifier.size(100.dp))
            }
        }
    }
}

@OptIn(ExperimentalCoilApi::class)
@Composable
fun SingleSetItem(
    set: Set,
    onClick: (String) -> Unit
) {
    Card(modifier = Modifier
        .padding(8.dp)
        .fillMaxWidth()
        .clickable { onClick(set.code) }, elevation = 8.dp
    ) {
        Row(modifier = Modifier.padding(8.dp), verticalAlignment = Alignment.CenterVertically) {
            Image(
                modifier = Modifier
                    .size(60.dp)
                    .padding(8.dp), painter = rememberImagePainter(
                    data = set.icon_svg_uri,
                    builder = {
                        decoder(SvgDecoder(LocalContext.current))
                    }
                ), contentDescription = null)
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
