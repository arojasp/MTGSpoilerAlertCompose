package es.alejandro.mtgspoileralert.sets

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import coil.decode.SvgDecoder
import es.alejandro.mtgspoileralert.sets.model.Set
import es.alejandro.mtgspoileralert.sets.viewmodel.SetsViewModel

@Composable
fun SetsScreen(
    viewModel: SetsViewModel = hiltViewModel(),
    onItemClick: (String) -> Unit
) {

    val listOfSets by remember { viewModel.listOfSets }

    Column {
        Text("Sets")
        Spacer(modifier = Modifier.height(30.dp))
        LazyColumn {
            items(listOfSets) { item ->
                SingleSetItem(set = item) {
                    onItemClick(it)
                }
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
        .clickable { onClick(set.code) }, elevation = 8.dp) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Image(
                modifier = Modifier.size(80.dp), painter = rememberImagePainter(
                    data = set.icon_svg_uri,
                    builder = {
                        decoder(SvgDecoder(LocalContext.current))
                    }
                ), contentDescription = null)
            Text(text = set.name, fontSize = 24.sp)
        }
    }
}
