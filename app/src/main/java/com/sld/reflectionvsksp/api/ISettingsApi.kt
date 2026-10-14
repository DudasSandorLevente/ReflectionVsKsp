package com.sld.reflectionvsksp.api

import com.sld.reflectionvsksp.data.local.model.SettingsData

interface ISettingsApi {

    suspend fun getSettings(): List<SettingsData>
    suspend fun saveSettings(settings: SettingsData)

}