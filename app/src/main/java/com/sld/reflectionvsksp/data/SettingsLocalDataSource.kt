package com.sld.reflectionvsksp.data

import com.sld.reflectionvsksp.data.local.dao.SettingsDao
import com.sld.reflectionvsksp.data.local.entity.SettingValueEntity
import com.sld.reflectionvsksp.data.local.entity.SettingsEntity

class SettingsLocalDataSource(private val settingsDao: SettingsDao) {

    suspend fun getSettings(name: String): Map<String, String>? {
        val result = settingsDao.getSettings(name) ?: return null
        return result.values.associate { it.key to it.value }
    }

    suspend fun saveSettings(name: String, values: Map<String, String>) {
        settingsDao.upsertSettings(SettingsEntity(name = name))
        val settings = settingsDao.findSettings(name) ?: error("Failed to create settings: $name")
        settingsDao.upsertValues(values.map { (key, value) ->
            SettingValueEntity(settingsId = settings.id, key = key, value = value)
        })
    }
}