package com.sld.reflectionvsksp.ui.view

import androidx.compose.runtime.Composable
import com.sld.reflectionvsksp.settings.SettingItem
import com.sld.reflectionvsksp.settings.SliderItem
import com.sld.reflectionvsksp.settings.SpinnerItem
import com.sld.reflectionvsksp.settings.SwitchItem

@Composable
fun SettingsRow(item: SettingItem, onAction: (SettingsAction) -> Unit) {
    when (item) {
        is SwitchItem -> SwitchSettingRow(item, onAction)
        is SliderItem -> SliderSettingRow(item, onAction)
        is SpinnerItem -> SpinnerSettingRow(item, onAction)
    }
}