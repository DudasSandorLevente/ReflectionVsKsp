package com.sld.reflectionvsksp.settings

data class SwitchItem(
    override val id: String,
    override val title: String,
    val value: Boolean
) : SettingItem