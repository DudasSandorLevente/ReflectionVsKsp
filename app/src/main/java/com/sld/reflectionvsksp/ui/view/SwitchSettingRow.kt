package com.sld.reflectionvsksp.ui.view

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sld.reflectionvsksp.settings.SwitchItem
import com.sld.reflectionvsksp.ui.view.SettingsAction.ValueChanged

@Composable
fun SwitchSettingRow(item: SwitchItem, onAction: (SettingsAction) -> Unit){

    Row(
        modifier = Modifier.fillMaxWidth().padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = item.title, modifier = Modifier.weight(1f))
        Switch(checked = item.value,
            onCheckedChange = { checked ->
                onAction(ValueChanged(id = item.id, value = checked.toString()))
            }
        )
    }

}