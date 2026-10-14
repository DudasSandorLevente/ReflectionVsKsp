package com.sld.reflectionvsksp.settings.reflection

import com.sld.reflectionvsksp.settings.INetworkModel
import com.sld.settings.annotations.reflection.ReflectionSliderSetting
import com.sld.settings.annotations.reflection.ReflectionSpinnerSetting
import com.sld.settings.annotations.reflection.ReflectionSwitchSetting
import com.sld.settings.annotations.reflection.ReflectionSettingsModel

@ReflectionSettingsModel(name = "NETWORK")
data class ReflectionNetworkModel(

    @ReflectionSwitchSetting(title = "Wi-Fi enabled", order = 1)
    override val wifiEnabled: Boolean,

    @ReflectionSwitchSetting(title = "Mobile data enabled", order = 2)
    override val mobileDataEnabled: Boolean,

    @ReflectionSliderSetting(title = "Connection timeout", order = 3, min = 5, max = 60)
    override val connectionTimeout: Int


): INetworkModel