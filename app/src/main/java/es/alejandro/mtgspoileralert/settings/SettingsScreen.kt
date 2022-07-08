package es.alejandro.mtgspoileralert.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import androidx.hilt.navigation.compose.hiltViewModel
import es.alejandro.mtgspoileralert.BuildConfig
import es.alejandro.mtgspoileralert.R
import es.alejandro.mtgspoileralert.settings.model.Settings
import es.alejandro.mtgspoileralert.settings.viewmodel.SettingsViewModel
import es.alejandro.mtgspoileralert.settings.viewmodel.ViewState
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
fun SettingsSetupScreen(paddingValues: PaddingValues, viewModel: SettingsViewModel, settings: Settings) {
    var coreCheckedState by remember { mutableStateOf(settings.coreSetListen) }

    var enabledElements by remember {
        mutableStateOf(
            coreCheckedState
        )
    }

    Column(modifier = Modifier.fillMaxHeight().padding(paddingValues), verticalArrangement = Arrangement.SpaceBetween) {
        Column(modifier = Modifier
            .padding(16.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(text = stringResource(id = R.string.settings_checkbox_title))
                Switch(
                    checked = coreCheckedState,
                    onCheckedChange = {
                        coreCheckedState = it
                        enabledElements =
                            coreCheckedState

                        viewModel.saveCoreListen(it)
                    }
                )
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

            DropDown(enabledElements, viewModel, settings) { timeUnitSelected ->
                if (timeUnitSelected == TimeUnit.MINUTES) {
                    minRangeValue = 15f
                    if (periodInterval < 15) periodInterval = 15f
                } else
                    minRangeValue = 1f
            }

            val maxRangeValue = 60f
            Slider(
                value = periodInterval,
                onValueChange = {
                    periodInterval = it
                    viewModel.saveTimeInterval(it.toLong())
                },
                valueRange = minRangeValue..maxRangeValue,
                enabled = enabledElements
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

        Row(modifier = Modifier
            .weight(1f, false)
            .fillMaxWidth()
            .padding(0.dp, 0.dp, 0.dp, 16.dp), horizontalArrangement = Arrangement.Center) {
            Text(stringResource(id = R.string.settings_version, BuildConfig.VERSION_NAME))
        }
    }

}

@Composable
fun DropDown(
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
