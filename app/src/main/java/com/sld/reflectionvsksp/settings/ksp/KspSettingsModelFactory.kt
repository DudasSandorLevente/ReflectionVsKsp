package com.sld.reflectionvsksp.settings.ksp

import com.sld.reflectionvsksp.data.local.model.SettingsData
import com.sld.reflectionvsksp.settings.ISettingsModelFactory
import com.sld.reflectionvsksp.vs.ksp.generated.GeneratedSettingsModelRegistry

class KspSettingsModelFactory : ISettingsModelFactory {

    override fun createModel(settings: SettingsData): Any {
        return GeneratedSettingsModelRegistry.create(name = settings.name, values = settings.values)
    }

}