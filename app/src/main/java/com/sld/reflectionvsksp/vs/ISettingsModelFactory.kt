package com.sld.reflectionvsksp.vs

interface ISettingsModelFactory {

    fun createSoundModel(values: Map<String, String>): ISoundModel
    fun createDisplayModel(values: Map<String, String>): IDisplayModel

}