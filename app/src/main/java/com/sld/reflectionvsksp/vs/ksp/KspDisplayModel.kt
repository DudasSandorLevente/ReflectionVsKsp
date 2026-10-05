package com.sld.reflectionvsksp.vs.ksp

import com.sld.reflectionvsksp.vs.enums.DisplayTheme
import com.sld.reflectionvsksp.vs.IDisplayModel
import com.sld.settings.annotations.ksp.KspSliderSetting
import com.sld.settings.annotations.ksp.KspSpinnerSetting
import com.sld.settings.annotations.ksp.KspSwitchSetting
import com.sld.settings.annotations.ksp.SettingsModel

@SettingsModel
data class KspDisplayModel(

    @KspSwitchSetting(title = "Dark mode", order = 0)
    override val darkMode: Boolean,

    @KspSliderSetting(title = "Brightness", min = 0, max = 100, order = 1)
    override val brightness: Int,

    @KspSwitchSetting(title = "Show traffic", order = 2)
    override val showTraffic: Boolean,

    @KspSpinnerSetting(title = "Display theme", order = 3)
    override val theme: DisplayTheme

):IDisplayModel