package com.sld.settings.processor.generator

import com.google.devtools.ksp.symbol.KSFile
import com.sld.settings.processor.model.SettingsModelDescriptor

interface IRegistryGenerator {

    fun generate(models: List<SettingsModelDescriptor>, sourceFiles: List<KSFile>)

}
