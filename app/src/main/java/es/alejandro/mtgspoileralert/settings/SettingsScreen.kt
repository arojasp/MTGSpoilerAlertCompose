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
    Column(modifier = Modifier.padding(16.dp)) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            val checkedState = remember { mutableStateOf(false) }

            Text(text = "Listen to main sets")
            Switch(checked = checkedState.value, onCheckedChange = { checkedState.value = it })
        }
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            val checkedState = remember { mutableStateOf(false) }

            Text(text = "Listen to commander sets")
            Switch(checked = checkedState.value, onCheckedChange = { checkedState.value = it })
        }
        var text by remember { mutableStateOf("") }
        OutlinedTextField(
            value = text,
            onValueChange = { text = it },
            label = { Text("Interval period") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.width((LocalConfiguration.current.screenWidthDp / 2).dp)
        )

        DropDown()
    }
}

@Composable
fun DropDown() {
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
        label = { Text(text = "Interval unit") },
        trailingIcon = {
            Icon(icon, contentDescription = null, Modifier.clickable { expanded = !expanded })
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