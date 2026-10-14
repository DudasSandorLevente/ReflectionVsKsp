package com.sld.reflectionvsksp.settings.reflection

import com.sld.reflectionvsksp.settings.IDisplayModel
import com.sld.reflectionvsksp.settings.enums.DisplayTheme
import com.sld.settings.annotations.reflection.ReflectionSliderSetting
import com.sld.settings.annotations.reflection.ReflectionSpinnerSetting
import com.sld.settings.annotations.reflection.ReflectionSwitchSetting
import com.sld.settings.annotations.reflection.ReflectionSettingsModel

@ReflectionSettingsModel(name = "DISPLAY")
data class ReflectionDisplayModel(

    @ReflectionSwitchSetting("Dark mode", order = 0)
    override val darkMode: Boolean,

    @ReflectionSliderSetting(title = "Brightness", min = 0, max = 100, order = 1)
    override val brightness: Int,

    @ReflectionSwitchSetting("Show traffic", order = 3)
    override val showTraffic: Boolean,

    @ReflectionSpinnerSetting("Display theme", order = 4)
    override val theme: DisplayTheme

): IDisplayModel