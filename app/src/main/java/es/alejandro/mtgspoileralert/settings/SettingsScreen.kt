package es.alejandro.mtgspoileralert.settings

import androidx.compose.foundation.layout.*
import androidx.compose.material.IconToggleButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SettingsScreen() {
    Column (modifier = Modifier.padding(16.dp)) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            val checkedState = remember { mutableStateOf(false) }

            Text(text = "Listen to core sets")
           Switch(checked = checkedState.value, onCheckedChange = {checkedState.value = it})
        }
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            val checkedState = remember { mutableStateOf(false) }

            Text(text = "Listen to commander sets")
            Switch(checked = checkedState.value, onCheckedChange = {checkedState.value = it})
        }
    }
}