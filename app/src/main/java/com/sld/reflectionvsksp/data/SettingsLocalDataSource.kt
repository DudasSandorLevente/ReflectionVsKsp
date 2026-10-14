package com.sld.reflectionvsksp.data

import com.sld.reflectionvsksp.data.local.SettingsDatabaseSeeder
import com.sld.reflectionvsksp.data.local.dao.SettingsDao
import com.sld.reflectionvsksp.data.local.entity.SettingValueEntity
import com.sld.reflectionvsksp.data.local.entity.SettingsEntity
import com.sld.reflectionvsksp.data.local.model.SettingsData
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class SettingsLocalDataSource(
    private val settingsDao: SettingsDao,
    private val seeder: SettingsDatabaseSeeder
) {
    private val seedMutex = Mutex()
    private var initialized = false

    private suspend fun ensureInitialized() {

        if (initialized) return
        seedMutex.withLock {
            if (!initialized) {
                seeder.seed()
                initialized = true
            }
        }

    }

    suspend fun getSettings(name: String): Map<String, String>? {

        ensureInitialized()
        val result = settingsDao.getSettings(name) ?: return null
        return result.values.associate { it.key to it.value }

    }

    suspend fun saveSettings(name: String, values: Map<String, String>) {

        ensureInitialized()
        val settingsId = settingsDao.findSettings(name)?.id ?: settingsDao.upsertSettings(
            SettingsEntity(name = name)
        )

        values.forEach { (key, value) ->
            val updatedRows = settingsDao.updateValue(
                settingsId = settingsId, key = key, value = value
            )
            if (updatedRows == 0) {
                settingsDao.upsertValue(
                    SettingValueEntity(settingsId = settingsId, key = key, value = value)
                )
            }
        }

    }

    suspend fun getAllSettings(): List<SettingsData> {

        ensureInitialized()
        return settingsDao.getAllSettings().map { result ->
            SettingsData(name = result.settings.name,
                values = result.values.associate { value -> value.key to value.value }
            )
        }

    }
}