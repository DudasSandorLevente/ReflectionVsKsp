package com.sld.reflectionvsksp.vs.ksp

import com.sld.reflectionvsksp.vs.ISettingsModelFactory
import com.sld.reflectionvsksp.vs.ISoundModel
import com.sld.reflectionvsksp.vs.IDisplayModel
import com.sld.reflectionvsksp.vs.ksp.generated.GeneratedKspSoundModelFactory
import com.sld.reflectionvsksp.vs.ksp.generated.GeneratedKspDisplayModelFactory

class KspSettingsModelFactory : ISettingsModelFactory {

    override fun createSoundModel(values: Map<String, String>): ISoundModel {
        return GeneratedKspSoundModelFactory.create(values)
    }

    override fun createDisplayModel(values: Map<String, String>): IDisplayModel {
        return GeneratedKspDisplayModelFactory.create(values)
    }

}