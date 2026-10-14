package com.sld.reflectionvsksp.data.local

import com.sld.reflectionvsksp.data.local.dao.SettingsDao
import com.sld.reflectionvsksp.data.local.entity.SettingValueEntity
import com.sld.reflectionvsksp.data.local.entity.SettingsEntity

class SettingsDatabaseSeeder(
    private val settingsDao: SettingsDao
) {

    suspend fun seed() {

        seedCategory(name = "SOUND",
            values = mapOf(
                "enabled" to "true", "volume" to "70",
                "voiceGuidance" to "true", "outputMode" to "SYSTEM_DEFAULT",
                "muteNotifications" to "false"
            )
        )

        seedCategory(name = "DISPLAY",
            values = mapOf(
                "darkMode" to "true", "brightness" to "70",
                "showTraffic" to "false", "theme" to "SYSTEM"
            )
        )

        seedCategory(name = "NETWORK",
            values = mapOf(
                "wifiEnabled" to "true", "mobileDataEnabled" to "false",
                "connectionTimeout" to "30"
            )
        )
    }

    private suspend fun seedCategory(name: String, values: Map<String, String>) {

        // Már létező kategóriát nem seedelünk újra.
        if (settingsDao.findSettings(name) != null) return
        val settingsId = settingsDao.upsertSettings(SettingsEntity(name = name))
        settingsDao.upsertValues(
            values.map { (key, value) ->
                SettingValueEntity(settingsId = settingsId, key = key, value = value)
            }
        )

    }

}