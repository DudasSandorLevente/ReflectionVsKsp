package com.sld.reflectionvsksp.settings

data class SliderItem(
    override val id: String,
    override val title: String,
    val value: Int,
    val min: Int,
    val max: Int
) : SettingItem