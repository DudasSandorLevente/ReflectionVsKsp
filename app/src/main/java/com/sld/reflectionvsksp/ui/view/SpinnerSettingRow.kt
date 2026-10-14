package com.sld.reflectionvsksp.ui.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sld.reflectionvsksp.settings.SpinnerItem
import com.sld.reflectionvsksp.ui.view.SettingsAction.ValueChanged

@Composable
fun SpinnerSettingRow(item: SpinnerItem, onAction: (SettingsAction) -> Unit) {

    var expanded by remember { mutableStateOf(false) }

    Column(modifier = Modifier.padding(16.dp)) {

        Text(item.title)
        Button(onClick = { expanded = true }) { Text(item.selected) }
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            item.options.forEach { option ->
                DropdownMenuItem(text = { Text(option) },
                    onClick = {
                        expanded = false
                        onAction(ValueChanged(id = item.id, value = option))
                    }
                )
            }
        }

    }

}