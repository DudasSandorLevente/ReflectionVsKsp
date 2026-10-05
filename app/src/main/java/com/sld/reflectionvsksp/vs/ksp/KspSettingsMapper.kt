package com.sld.reflectionvsksp.vs.ksp

import com.sld.reflectionvsksp.generated.GeneratedKspDisplayModelMapper
import com.sld.reflectionvsksp.generated.GeneratedKspSoundModelMapper
import com.sld.reflectionvsksp.vs.ISettingsMapper
import com.sld.reflectionvsksp.settings.SettingItem

class KspSettingsMapper: ISettingsMapper {

    override fun map(model: Any): List<SettingItem> {
        return when (model) {
            is KspSoundModel -> GeneratedKspSoundModelMapper.map(model)
            is KspDisplayModel -> GeneratedKspDisplayModelMapper.map(model)
            else -> error("Unsupported KSP settings model: ${model::class.qualifiedName}")
        }
    }

}