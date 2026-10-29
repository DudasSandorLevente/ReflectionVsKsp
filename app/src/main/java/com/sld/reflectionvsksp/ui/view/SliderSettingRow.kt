package com.sld.reflectionvsksp.ui.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sld.reflectionvsksp.settings.SliderItem
import com.sld.reflectionvsksp.ui.view.SettingsAction.ValueChanged

@Composable
fun SliderSettingRow(item: SliderItem, onAction: (SettingsAction) -> Unit) {
    Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
        Text(item.title)
        Slider(
            value = item.value.coerceIn(item.min, item.max).toFloat(),
            onValueChange = { value ->
                onAction(ValueChanged(id = item.id, value = value.toInt().toString()))
            },
            valueRange = item.min.toFloat()..item.max.toFloat()
        )
    }
}