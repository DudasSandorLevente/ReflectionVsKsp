package com.sld.reflectionvsksp.api

import com.sld.reflectionvsksp.vs.IDisplayModel
import com.sld.reflectionvsksp.vs.ISettingsModelFactory
import com.sld.reflectionvsksp.vs.ISoundModel
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.milliseconds

class MockSettingsApi(private val modelFactory: ISettingsModelFactory) : ISettingsApi {

    override suspend fun getSoundSettings(): ISoundModel {
        delay(500.milliseconds)
        return modelFactory.createSoundModel(
            mapOf(
                "enabled" to "true",
                "volume" to "70",
                "voiceGuidance" to "true",
                "outputMode" to "SYSTEM_DEFAULT",
                "muteNotifications" to "false"
            )
        )
    }

    override suspend fun getDisplaySettings(): IDisplayModel {
        delay(700.milliseconds)
        return modelFactory.createDisplayModel(
            mapOf(
                "darkMode" to "false",
                "brightness" to "80",
                "showTraffic" to "true",
                "theme" to "SYSTEM"
            )
        )
    }

    override suspend fun saveSoundSettings(model: ISoundModel) {
        // TODO
    }

    override suspend fun saveDisplaySettings(model: IDisplayModel) {
        // TODO
    }
}