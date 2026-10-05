package com.sld.reflectionvsksp.api

import com.sld.reflectionvsksp.vs.enums.DisplayTheme
import com.sld.reflectionvsksp.vs.IDisplayModel
import com.sld.reflectionvsksp.vs.IModelFactory
import com.sld.reflectionvsksp.vs.ISoundModel
import com.sld.reflectionvsksp.vs.enums.SoundOutputMode
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.milliseconds

class MockSettingsApi(private val modelFactory: IModelFactory): ISettingsApi {

    override suspend fun getSoundSettings(): ISoundModel {
        delay(500.milliseconds)
        return modelFactory.createSoundModel(
            enabled = true,
            volume = 70,
            voiceGuidance = true,
            outputMode = SoundOutputMode.SYSTEM_DEFAULT,
            muteNotifications = false
        )
    }

    override suspend fun getDisplaySettings(): IDisplayModel {
        delay(700.milliseconds)
        return modelFactory.createDisplayModel(
            darkMode = false,
            brightness = 80,
            showTraffic = true,
            theme = DisplayTheme.SYSTEM
        )
    }

    override suspend fun saveSoundSettings(model: ISoundModel) {
        //TODO("Not yet implemented")
    }

    override suspend fun saveDisplaySettings(model: IDisplayModel) {
        //TODO("Not yet implemented")
    }

}