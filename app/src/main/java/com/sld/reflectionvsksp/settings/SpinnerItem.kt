package com.sld.reflectionvsksp.settings

data class SpinnerItem(
    override val id: String,
    override val title: String,
    val selected: String,
    val options: List<String>
) : SettingItem