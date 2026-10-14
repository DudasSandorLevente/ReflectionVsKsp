package com.sld.reflectionvsksp.settings

import com.sld.reflectionvsksp.data.local.model.SettingsData

interface ISettingsModelFactory {

    fun createModel(settings: SettingsData): Any

}