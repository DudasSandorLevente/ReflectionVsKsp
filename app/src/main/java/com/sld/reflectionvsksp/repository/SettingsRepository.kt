package com.sld.reflectionvsksp.repository

import com.sld.reflectionvsksp.api.ISettingsApi
import com.sld.reflectionvsksp.vs.IDisplayModel
import com.sld.reflectionvsksp.vs.ISoundModel

class SettingsRepository(
    private val api: ISettingsApi
) {

    suspend fun loadSoundSettings(): ISoundModel {
        return api.getSoundSettings()
    }

    suspend fun loadDisplaySettings(): IDisplayModel {
        return api.getDisplaySettings()
    }
}