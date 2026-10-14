package com.sld.reflectionvsksp.api

import com.sld.reflectionvsksp.data.local.model.SettingsData
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.milliseconds

class MockSettingsApi : ISettingsApi {

    override suspend fun getSettings(): List<SettingsData> {

        delay(500.milliseconds)
        return listOf(
            SettingsData(
                name = "SOUND",
                values = mapOf(
                    "enabled" to "true",
                    "volume" to "70",
                    "voiceGuidance" to "true",
                    "outputMode" to "SYSTEM_DEFAULT",
                    "muteNotifications" to "false"
                )
            ),
            SettingsData(
                name = "DISPLAY",
                values = mapOf(
                    "darkMode" to "false",
                    "brightness" to "80",
                    "showTraffic" to "true",
                    "theme" to "SYSTEM"
                )
            )
        )

    }

    override suspend fun saveSettings(settings: SettingsData) {
        // Később implementáljuk
    }
}