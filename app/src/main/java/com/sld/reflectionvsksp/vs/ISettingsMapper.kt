package com.sld.reflectionvsksp.vs

import com.sld.reflectionvsksp.settings.SettingItem

interface ISettingsMapper {
    fun map(model: Any): List<SettingItem>
}