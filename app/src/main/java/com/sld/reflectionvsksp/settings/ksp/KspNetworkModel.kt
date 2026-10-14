package com.sld.reflectionvsksp.settings.ksp

import com.sld.reflectionvsksp.settings.INetworkModel
import com.sld.settings.annotations.ksp.KspSliderSetting
import com.sld.settings.annotations.ksp.KspSwitchSetting
import com.sld.settings.annotations.ksp.SettingsModel

@SettingsModel(name = "NETWORK")
data class KspNetworkModel(

    @KspSwitchSetting(title = "Wi-Fi enabled", order = 1)
    override val wifiEnabled: Boolean,

    @KspSwitchSetting(title = "Mobile data enabled", order = 2)
    override val mobileDataEnabled: Boolean,

    @KspSliderSetting(title = "Connection timeout", order = 3, min = 5, max = 60)
    override val connectionTimeout: Int

): INetworkModel