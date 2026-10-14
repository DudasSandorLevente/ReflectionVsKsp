package com.sld.reflectionvsksp.settings

interface ISettingsMapper {
    fun map(model: Any): List<SettingItem>
}