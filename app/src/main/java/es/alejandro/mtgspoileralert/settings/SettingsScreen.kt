package es.alejandro.mtgspoileralert.settings

import android.util.Log
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import androidx.hilt.navigation.compose.hiltViewModel
import es.alejandro.mtgspoileralert.BuildConfig
import es.alejandro.mtgspoileralert.R
import es.alejandro.mtgspoileralert.settings.model.Settings
import es.alejandro.mtgspoileralert.settings.model.Settings.Available.formatString
import es.alejandro.mtgspoileralert.settings.viewmodel.SettingsViewModel
import es.alejandro.mtgspoileralert.settings.viewmodel.ViewState
import es.alejandro.mtgspoileralert.util.extensions.badgeLayout
import java.util.*
import java.util.concurrent.TimeUnit

@Composable
fun SettingsScreen(
    paddingValues: PaddingValues,
    viewModel: SettingsViewModel = hiltViewModel(),
    preferencesAction: (Settings) -> Unit
) {

    val viewState by remember { viewModel.viewState }

    when (val state = viewState) {
        is ViewState.Success -> {
            SettingsSetupScreen(paddingValues, viewModel = viewModel, settings = state.data)
            preferencesAction(state.data)
        }
        is ViewState.SuccessChange -> {
            preferencesAction(state.data)
        }
        is ViewState.Error -> {
        }
        is ViewState.Loading -> {
        }
    }
}

@Composable
fun SettingsSetupScreen(
    paddingValues: PaddingValues,
    viewModel: SettingsViewModel,
    settings: Settings
) {
    var coreCheckedState by remember { mutableStateOf(settings.coreSetListen) }

    var enabledElements by remember {
        mutableStateOf(
            coreCheckedState
        )
    }

    Column(
        modifier = Modifier
            .fillMaxHeight()
            .padding(paddingValues), verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {

            LanguageDropDown(viewModel, settings)

            Spacer(modifier = Modifier.size(30.dp))

            SetSelectionSection(viewModel, settings)

            Spacer(modifier = Modifier.size(15.dp))

            ListenToNewCardsSection(coreCheckedState) {
                coreCheckedState = it
                enabledElements =
                    coreCheckedState

                viewModel.saveCoreListen(it)
            }

            Spacer(modifier = Modifier.size(30.dp))

            var minRangeValue by remember {
                if (settings.interval.second == TimeUnit.MINUTES)
                    mutableStateOf(15f)
                else
                    mutableStateOf(1f)
            }

            var periodInterval by remember {
                mutableStateOf(settings.interval.first.toFloat())
            }

            IntervalUnitDropDown(enabledElements, viewModel, settings) { timeUnitSelected ->
                if (timeUnitSelected == TimeUnit.MINUTES) {
                    minRangeValue = 15f
                    if (periodInterval < 15) periodInterval = 15f
                } else
                    minRangeValue = 1f
            }

            val maxRangeValue = 60f

            SliderSection(periodInterval, minRangeValue, maxRangeValue, enabledElements) {
                periodInterval = it
                viewModel.saveTimeInterval(it.toLong())
            }
        }

        Row(
            modifier = Modifier
                .weight(1f, false)
                .fillMaxWidth()
                .padding(0.dp, 0.dp, 0.dp, 16.dp), horizontalArrangement = Arrangement.Center
        ) {
            Text(stringResource(id = R.string.settings_version, BuildConfig.VERSION_NAME))
        }
    }

}

@Composable
fun ListenToNewCardsSection(
    coreCheckedState: Boolean,
    onCheckedAction: (checked: Boolean) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = stringResource(id = R.string.settings_checkbox_title))
        Switch(
            checked = coreCheckedState,
            onCheckedChange = {
                onCheckedAction(it)
            }
        )
    }
}

@Composable
fun SetSelectionSection(
    viewModel: SettingsViewModel,
    settings: Settings
) {

    LazyVerticalGrid(columns = GridCells.Fixed(3), verticalArrangement = Arrangement.Center) {
        items(Settings.Available.SET_TYPES) { item ->

            var checked by remember { mutableStateOf(settings.setTypes.contains(item)) }

            IconToggleButton(
                checked = checked,
                onCheckedChange = {

                    if (settings.setTypes.size == 1 && !it) {
                        Log.d("TAG","${settings.setTypes.size}")
                    } else {

                        if (it){
                            if (!settings.setTypes.any { it == item })
                                settings.setTypes.add(item)
                        } else {
                            if (settings.setTypes.any { it == item })
                                settings.setTypes.remove(item)
                        }

                        checked = it
                        viewModel.saveSetType(settings.setTypes)
                    }
                },
                modifier = Modifier
                    .padding(8.dp, 4.dp)
            ) {

                val backgroundColor =
                    if (checked) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant

                val textColor =
                    if (checked) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface

                AutoSizeText(
                    text = item.formatString().lowercase()
                        .replaceFirstChar { it.uppercaseChar() },
                    textStyle = MaterialTheme.typography.bodyMedium,
                    color = textColor,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .background(
                            backgroundColor,
                            shape = CircleShape
                        )
                        .badgeLayout()
                        .padding(4.dp)
                        .fillMaxWidth()
                )
            }


        }
    }
}

@Composable
fun AutoSizeText(
    text: String,
    textStyle: TextStyle,
    textAlign: TextAlign?,
    color: Color,
    modifier: Modifier = Modifier
) {
    var scaledTextStyle by remember { mutableStateOf(textStyle) }
    var readyToDraw by remember { mutableStateOf(false) }

    Text(
        text,
        modifier.drawWithContent {
            if (readyToDraw) {
                drawContent()
            }
        },
        style = scaledTextStyle,
        softWrap = false,
        color = color,
        textAlign = textAlign,
        onTextLayout = { textLayoutResult ->
            if (textLayoutResult.didOverflowWidth) {
                scaledTextStyle =
                    scaledTextStyle.copy(fontSize = scaledTextStyle.fontSize * 0.9)
            } else {
                readyToDraw = true
            }
        }
    )
}

@Composable
fun SliderSection(
    periodInterval: Float,
    minRangeValue: Float,
    maxRangeValue: Float,
    enabled: Boolean,
    onValueChange: (value: Float) -> Unit
) {
    Slider(
        value = periodInterval,
        onValueChange = {
            onValueChange(it)
        },
        valueRange = minRangeValue..maxRangeValue,
        enabled = enabled
    )
    Spacer(modifier = Modifier.size(15.dp))
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = "${minRangeValue.toInt()}")
        Text(style = MaterialTheme.typography.titleMedium, text = "${periodInterval.toInt()}")
        Text(text = "${maxRangeValue.toInt()}")
    }
}

@Composable
fun LanguageDropDown(
    viewModel: SettingsViewModel,
    settings: Settings
) {

    var expanded by remember {
        mutableStateOf(false)
    }
    val list = Settings.Available.LANGUAGES

    var selectedItem by remember {
        mutableStateOf(Locale.forLanguageTag(settings.language).displayName)
    }

    var textFiledSize by remember {
        mutableStateOf(Size.Zero)
    }

    val icon = if (expanded) {
        Icons.Filled.KeyboardArrowUp
    } else {
        Icons.Filled.KeyboardArrowDown
    }

    val interactionSource = remember { MutableInteractionSource() }
    val isPressed: Boolean by interactionSource.collectIsPressedAsState()

    if (isPressed) {
        expanded = !expanded
    }

    Column {
        OutlinedTextField(
            value = selectedItem,
            onValueChange = {
                selectedItem = it
                viewModel.saveLanguage(it)
            },
            interactionSource = interactionSource,
            modifier = Modifier
                .fillMaxWidth()
                .onGloballyPositioned { layoutCoordinates ->
                    textFiledSize = layoutCoordinates.size.toSize()
                },
            readOnly = true,
            label = { Text(text = stringResource(id = R.string.settings_card_language_title)) },
            trailingIcon = {
                Icon(
                    icon,
                    contentDescription = null,
                    Modifier.clickable { expanded = !expanded }
                )
            }
        )

        DropdownMenu(
            expanded = expanded, onDismissRequest = { expanded = false },
            modifier = Modifier.width(with(LocalDensity.current) { textFiledSize.width.toDp() })
        ) {
            list.forEach { language ->
                DropdownMenuItem(onClick = {
                    selectedItem = Locale.forLanguageTag(language).displayName
                    viewModel.saveLanguage(language)
                    expanded = false
                }, text = { Text(text = Locale.forLanguageTag(language).displayName) })
            }
        }
    }
}

@Composable
fun IntervalUnitDropDown(
    enabled: Boolean,
    viewModel: SettingsViewModel,
    settings: Settings,
    itemSelected: (timeUnitSelected: TimeUnit) -> Unit
) {

    var expanded by remember {
        mutableStateOf(false)
    }
    val list = listOf(TimeUnit.MINUTES, TimeUnit.HOURS, TimeUnit.DAYS)
    var selectedItem by remember {
        mutableStateOf(settings.interval.second)
    }

    var textFiledSize by remember {
        mutableStateOf(Size.Zero)
    }

    val icon = if (expanded) {
        Icons.Filled.KeyboardArrowUp
    } else {
        Icons.Filled.KeyboardArrowDown
    }

    val interactionSource = remember { MutableInteractionSource() }
    val isPressed: Boolean by interactionSource.collectIsPressedAsState()

    if (isPressed) {
        expanded = !expanded
    }

    Column {
        OutlinedTextField(
            value = selectedItem.toString(),
            onValueChange = {
                val timeUnit = TimeUnit.valueOf(it)
                selectedItem = timeUnit
                viewModel.saveTimeUnit(timeUnit)
            },
            interactionSource = interactionSource,
            modifier = Modifier
                .fillMaxWidth()
                .onGloballyPositioned { layoutCoordinates ->
                    textFiledSize = layoutCoordinates.size.toSize()
                },
            enabled = enabled,
            readOnly = true,
            label = { Text(text = stringResource(id = R.string.settings_interval_title)) },
            trailingIcon = {
                Icon(
                    icon,
                    contentDescription = null,
                    Modifier.clickable(enabled = enabled) { expanded = !expanded }
                )
            }
        )

        DropdownMenu(
            expanded = expanded, onDismissRequest = { expanded = false },
            modifier = Modifier.width(with(LocalDensity.current) { textFiledSize.width.toDp() })
        ) {
            list.forEach { timeUnit ->
                DropdownMenuItem(onClick = {
                    selectedItem = timeUnit
                    viewModel.saveTimeUnit(timeUnit)
                    expanded = false
                    itemSelected(selectedItem)
                }, text = { Text(text = timeUnit.toString()) })
            }
        }
    }
}
