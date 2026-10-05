package com.sld.reflectionvsksp.ui.view

import androidx.compose.runtime.Composable
import com.sld.reflectionvsksp.settings.SettingItem
import com.sld.reflectionvsksp.settings.SliderItem
import com.sld.reflectionvsksp.settings.SpinnerItem
import com.sld.reflectionvsksp.settings.SwitchItem

@Composable
fun SettingsRow(item: SettingItem) {
    when (item) {
        is SwitchItem -> SwitchSettingRow(item)
        is SliderItem -> SliderSettingRow(item)
        is SpinnerItem -> SpinnerSettingRow(item)
    }
}