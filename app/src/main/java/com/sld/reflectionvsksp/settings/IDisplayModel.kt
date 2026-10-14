package com.sld.reflectionvsksp.settings

import com.sld.reflectionvsksp.settings.enums.DisplayTheme

interface IDisplayModel {
    val darkMode: Boolean
    val brightness: Int
    val showTraffic: Boolean
    val theme: DisplayTheme
}