package com.sld.reflectionvsksp.settings.ksp

import com.sld.reflectionvsksp.settings.ISettingsMapper
import com.sld.reflectionvsksp.settings.SettingItem
import com.sld.reflectionvsksp.vs.ksp.generated.GeneratedSettingsModelRegistry

class KspSettingsMapper: ISettingsMapper {

    override fun map(model: Any): List<SettingItem> {
        return GeneratedSettingsModelRegistry.map(model)
    }

}