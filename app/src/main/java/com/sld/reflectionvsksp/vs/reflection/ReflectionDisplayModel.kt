package com.sld.reflectionvsksp.vs.reflection

import com.sld.settings.annotations.reflection.ReflectionSliderSetting
import com.sld.settings.annotations.reflection.ReflectionSpinnerSetting
import com.sld.settings.annotations.reflection.ReflectionSwitchSetting
import com.sld.reflectionvsksp.vs.enums.DisplayTheme
import com.sld.reflectionvsksp.vs.IDisplayModel

data class ReflectionDisplayModel(

    @ReflectionSwitchSetting("Dark mode", order = 0)
    override val darkMode: Boolean,

    @ReflectionSliderSetting(title = "Brightness", min = 0, max = 100, order = 1)
    override val brightness: Int,

    @ReflectionSwitchSetting("Show traffic", order = 3)
    override val showTraffic: Boolean,

    @ReflectionSpinnerSetting("Theme", order = 4)
    override val theme: DisplayTheme

): IDisplayModel