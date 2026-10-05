package com.sld.reflectionvsksp.ui.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sld.reflectionvsksp.settings.SettingsCategory

@Composable
fun CategorySelector(onCategorySelected: (SettingsCategory) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Button(
            modifier = Modifier.weight(1f),
            onClick = { onCategorySelected(SettingsCategory.SOUND) }
        ) {
            Text("Sound")
        }
        Button(
            modifier = Modifier.weight(1f),
            onClick = { onCategorySelected(SettingsCategory.DISPLAY) }
        ) {
            Text("Display")
        }
    }
}