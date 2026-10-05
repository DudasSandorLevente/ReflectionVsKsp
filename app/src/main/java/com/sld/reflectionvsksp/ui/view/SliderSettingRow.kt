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

@Composable
fun SliderSettingRow(item: SliderItem) {
    Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
        Text(item.title)
        Slider(
            value = item.value.toFloat(),
            onValueChange = {
                // később
            },
            valueRange = item.min.toFloat()..item.max.toFloat()
        )
    }
}