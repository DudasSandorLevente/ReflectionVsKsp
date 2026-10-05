package com.sld.reflectionvsksp.vs

import com.sld.reflectionvsksp.vs.enums.DisplayTheme

interface IDisplayModel {
    val darkMode: Boolean
    val brightness: Int
    val showTraffic: Boolean
    val theme: DisplayTheme
}