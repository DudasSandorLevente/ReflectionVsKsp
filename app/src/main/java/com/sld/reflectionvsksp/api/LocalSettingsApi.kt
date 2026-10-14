package com.sld.reflectionvsksp.api

import com.sld.reflectionvsksp.data.local.model.SettingsData
import com.sld.reflectionvsksp.data.SettingsLocalDataSource

class LocalSettingsApi(private val localDataSource: SettingsLocalDataSource) : ISettingsApi {

    override suspend fun getSettings(): List<SettingsData> {
        return localDataSource.getAllSettings()
    }

    override suspend fun saveSettings(settings: SettingsData) {

        localDataSource.saveSettings(name = settings.name, values = settings.values)
    }

}