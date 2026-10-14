package com.sld.reflectionvsksp.repository

import com.sld.reflectionvsksp.api.ISettingsApi
import com.sld.reflectionvsksp.data.local.model.SettingsData

class SettingsRepository(private val api: ISettingsApi) {

    suspend fun loadSettings(): List<SettingsData> {
        return api.getSettings()
    }

    suspend fun saveSettings(settings: SettingsData) {
        api.saveSettings(settings)
    }

}