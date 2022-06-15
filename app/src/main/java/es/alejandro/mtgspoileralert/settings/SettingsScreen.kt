package es.alejandro.mtgspoileralert.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import androidx.hilt.navigation.compose.hiltViewModel
import es.alejandro.mtgspoileralert.datastore.SettingsDataStoreManager
import es.alejandro.mtgspoileralert.settings.model.Settings
import es.alejandro.mtgspoileralert.settings.viewmodel.SettingsViewModel
import es.alejandro.mtgspoileralert.settings.viewmodel.ViewState
import java.util.concurrent.TimeUnit

@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel = hiltViewModel()
) {

    val viewState by remember { viewModel.viewState }

    when (val state = viewState) {
        is ViewState.Success -> {
            SettingsSetupScreen(viewModel = viewModel, settings = state.data)
        }
        is ViewState.Error -> {

        }
        is ViewState.Loading -> {

        }
    }

}

@Composable
fun SettingsSetupScreen(viewModel: SettingsViewModel, settings: Settings) {
    var coreCheckedState by remember { mutableStateOf(settings.coreSetListen) }

    var enabledElements by remember {
        mutableStateOf(
            coreCheckedState
        )
    }

    Column(modifier = Modifier.padding(16.dp)) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(text = "Listen to main sets")
            Switch(
                checked = coreCheckedState,
                onCheckedChange = {
                    coreCheckedState = it
                    enabledElements =
                        coreCheckedState

                    viewModel.saveCoreListen(it)
                })
        }

        Spacer(modifier = Modifier.size(30.dp))

        DropDown(enabledElements, viewModel, settings)

        var periodInterval by remember {
            mutableStateOf(settings.interval.first.toFloat())
        }
        val minRangeValue = 1f
        val maxRangeValue = 60f
        Slider(
            value = periodInterval,
            onValueChange = {
                periodInterval = it
                viewModel.saveTimeInterval(it.toInt())
            },
            valueRange = minRangeValue..maxRangeValue,
            enabled = enabledElements,
            steps = 99
        )
        Spacer(modifier = Modifier.size(15.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "${minRangeValue.toInt()}")
            Text(style = MaterialTheme.typography.h1, text = "${periodInterval.toInt()}")
            Text(text = "${maxRangeValue.toInt()}")
        }

    }
}

@Composable
fun DropDown(enabled: Boolean, viewModel: SettingsViewModel, settings: Settings) {
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

    OutlinedTextField(value = selectedItem.toString(),
        onValueChange = {
            val timeUnit = TimeUnit.valueOf(it)
            selectedItem = timeUnit
            viewModel.saveTimeUnit(timeUnit)
        },
        modifier = Modifier
            .fillMaxWidth()
            .onGloballyPositioned { layoutCoordinates ->
                textFiledSize = layoutCoordinates.size.toSize()
            },
        enabled = enabled,
        readOnly = true,
        label = { Text(text = "Interval unit") },
        trailingIcon = {
            Icon(
                icon,
                contentDescription = null,
                Modifier.clickable(enabled = enabled) { expanded = !expanded })
        })

    DropdownMenu(
        expanded = expanded, onDismissRequest = { expanded = false },
        modifier = Modifier.width(with(LocalDensity.current) { textFiledSize.width.toDp() })
    ) {
        list.forEach { timeUnit ->
            DropdownMenuItem(onClick = {
                selectedItem = timeUnit
                viewModel.saveTimeUnit(timeUnit)
                expanded = false
            }) {
                Text(text = timeUnit.toString())
            }
        }
    }
}