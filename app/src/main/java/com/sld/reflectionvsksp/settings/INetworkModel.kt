package com.sld.reflectionvsksp.settings

import com.sld.reflectionvsksp.settings.enums.DisplayTheme

interface INetworkModel {

    val wifiEnabled: Boolean
    val mobileDataEnabled: Boolean
    val connectionTimeout: Int

}