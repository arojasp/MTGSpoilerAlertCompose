package es.alejandro.mtgspoileralert.cards

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.HorizontalPagerIndicator
import com.google.accompanist.pager.rememberPagerState
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.SwipeRefreshIndicator
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import es.alejandro.mtgspoileralert.R
import es.alejandro.mtgspoileralert.cards.model.Card
import es.alejandro.mtgspoileralert.cards.viewmodel.CardsViewModel
import es.alejandro.mtgspoileralert.cards.viewmodel.ViewState

@Composable
fun CardsScreen(
    paddingValues: PaddingValues,
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
    val isRefreshing by remember { viewModel.isRefreshing }

    when (val state = viewState) {
        is ViewState.Success -> {
            SwipeRefresh(
                state = rememberSwipeRefreshState(isRefreshing = isRefreshing),
                onRefresh = {
                    if (!set.isNullOrBlank())
                        viewModel.getCardsForSet(set)
                }, indicator = { state, trigger ->
                    SwipeRefreshIndicator(
                        state = state,
                        refreshTriggerDistance = trigger,
                        backgroundColor = MaterialTheme.colorScheme.primary
                    )
                }) {
                CardsList(paddingValues, state.data) { cardId ->
                    onCardClick(cardId)
                }
            }
        }
        is ViewState.Error -> {
            Text(
                modifier = Modifier.padding(paddingValues),
                text = stringResource(id = R.string.error, state.errorMessage)
            )
        }
        is ViewState.Loading -> {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CircularProgressIndicator(modifier = Modifier.size(100.dp))
            }
        }
    }
}

@Composable
fun CardsList(
    paddingValues: PaddingValues,
    cards: List<Pair<String, List<Card>>>,
    onCardClick: (String) -> Unit
) {
    LazyVerticalGrid(GridCells.Fixed(2), modifier = Modifier.padding(paddingValues)) {
        items(cards) { item ->
            SingleCardItem(item) { cardId ->
                onCardClick(cardId)
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun SingleCardItem(
    cards: Pair<String, List<Card>>,
    onClick: (String) -> Unit
) {

    var showDialog by remember { mutableStateOf(false) }
    if (showDialog) {
        DialogCard(cards.second.map { it.image_uris?.normal }) {
            showDialog = false
        }
    }

    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .combinedClickable(
                onClick = { onClick(cards.second[0].id) },
                onLongClick = { showDialog = true }
            ),
        elevation = CardDefaults.cardElevation()
    ) {
        AsyncImage(
            modifier = Modifier.fillMaxSize(),
            model = ImageRequest.Builder(LocalContext.current)
                .data(cards.second[0].image_uris?.normal).build(),
            contentDescription = null,
            contentScale = ContentScale.Crop
        )
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun DialogCard(imageUris: List<String?>, onClose: () -> Unit) {
    Dialog(
        onDismissRequest = onClose,
        DialogProperties(dismissOnBackPress = true, dismissOnClickOutside = true)
    ) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = MaterialTheme.colorScheme.background,
            modifier = Modifier.wrapContentHeight()
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                val pagerState = rememberPagerState()

                HorizontalPager(
                    count = imageUris.size,
                    state = pagerState,
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                ) { page ->

                    AsyncImage(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp, 32.dp, 8.dp, 0.dp),
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(imageUris[page]).build(),
                        contentDescription = null
                    )
                }

                HorizontalPagerIndicator(
                    pagerState = pagerState,
                    activeColor = MaterialTheme.colorScheme.primary,
                    inactiveColor = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier
                        .padding(16.dp)

                )
            }
        }

    }
}
