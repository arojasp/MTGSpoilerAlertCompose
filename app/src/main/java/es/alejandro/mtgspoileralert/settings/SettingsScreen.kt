package es.alejandro.mtgspoileralert.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import java.util.concurrent.TimeUnit

@Composable
fun SettingsScreen() {
    var coreCheckedState by remember { mutableStateOf(false) }
    var commanderCheckedState by remember { mutableStateOf(false) }

    var enabledElements by remember {
        mutableStateOf(
            checkEnabledElements(
                coreCheckedState,
                commanderCheckedState
            )
        )
    }

    Column(modifier = Modifier.padding(16.dp)) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(text = "Listen to main sets")
            Switch(
                checked = coreCheckedState,
                onCheckedChange = {
                    coreCheckedState = it
                    enabledElements = checkEnabledElements(
                        coreCheckedState,
                        commanderCheckedState
                    )
                })
        }
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(text = "Listen to commander sets")
            Switch(
                checked = commanderCheckedState,
                onCheckedChange = {
                    commanderCheckedState = it
                    enabledElements = checkEnabledElements(
                        coreCheckedState,
                        commanderCheckedState
                    )
                })
        }

        Spacer(modifier = Modifier.size(30.dp))

        DropDown(enabledElements)

        var periodInterval by remember {
            mutableStateOf(1f)
        }
        Slider(
            value = periodInterval,
            onValueChange = { periodInterval = it },
            valueRange = 1f..60f,
            enabled = enabledElements,
            steps = 99
        )

    }
}

fun checkEnabledElements(coreCheckedState: Boolean, commanderCheckedState: Boolean): Boolean =
    coreCheckedState || commanderCheckedState

@Composable
fun DropDown(enabled: Boolean) {
    var expanded by remember {
        mutableStateOf(false)
    }
    val list = listOf(TimeUnit.MINUTES, TimeUnit.HOURS, TimeUnit.DAYS)
    var selectedItem by remember {
        mutableStateOf(TimeUnit.MINUTES)
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
        onValueChange = { selectedItem = TimeUnit.valueOf(it) },
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
                expanded = false
            }) {
                Text(text = timeUnit.toString())
            }
        }
    }
}