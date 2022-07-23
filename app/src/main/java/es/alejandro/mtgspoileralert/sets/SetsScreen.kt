package es.alejandro.mtgspoileralert.sets

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.SwipeRefreshIndicator
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import es.alejandro.mtgspoileralert.sets.model.Set
import es.alejandro.mtgspoileralert.sets.viewmodel.ActionState
import es.alejandro.mtgspoileralert.sets.viewmodel.SetsViewModel
import es.alejandro.mtgspoileralert.sets.viewmodel.ViewState
import es.alejandro.mtgspoileralert.settings.model.Settings
import es.alejandro.mtgspoileralert.R
import es.alejandro.mtgspoileralert.util.extensions.badgeLayout

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun SetsScreen(
    paddingValues: PaddingValues,
    viewModel: SetsViewModel = hiltViewModel(),
    onItemClick: (String) -> Unit,
    preferencesAction: (Settings) -> Unit
) {

    DisposableEffect(key1 = Unit) {
        viewModel.refresh()
        onDispose {}
    }

    val viewState by remember { viewModel.viewState }
    val actionState by remember { viewModel.actionState }
    val isRefreshing by remember { viewModel.isRefreshing }

    when (val state = viewState) {
        is ViewState.Success -> {
            AnimatedContent(targetState = viewState) {
                SwipeRefresh(
                    state = rememberSwipeRefreshState(isRefreshing = isRefreshing),
                    onRefresh = { viewModel.refresh() },
                    indicator = { state, trigger ->
                        SwipeRefreshIndicator(
                            state = state,
                            refreshTriggerDistance = trigger,
                            backgroundColor = MaterialTheme.colorScheme.primary
                        )
                    }
                ) {
                    LazyColumn(modifier = Modifier.padding(paddingValues)) {
                        items(state.data) { item ->
                            SingleSetItem(set = item) {
                                onItemClick(it)
                            }
                        }
                    }
                }
            }

        }
        is ViewState.Error -> {
            Text(modifier = Modifier.padding(paddingValues),
                text = stringResource(id = R.string.error, state.errorMessage))
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
        else -> {
            // ignore
        }
    }
    when (val state = actionState) {
        is ActionState.PreferencesAction -> {
            preferencesAction(state.data)
        }
        else -> {
            // ignore
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
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
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background)
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
                contentDescription = null,
                colorFilter = ColorFilter.tint(
                    MaterialTheme.colorScheme.onBackground
                )
            )
            Column {
                Text(
                    text = set.name,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.size(4.dp))
                Text(
                    text = set.code.uppercase(),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.background, shape = CircleShape)
                        .badgeLayout()
                        .padding(4.dp)
                )
            }
        }
    }
}
