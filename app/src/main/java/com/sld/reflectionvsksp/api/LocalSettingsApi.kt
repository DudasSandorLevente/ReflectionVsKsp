package com.sld.reflectionvsksp.api

import com.sld.reflectionvsksp.data.SettingsLocalDataSource
import com.sld.reflectionvsksp.vs.IDisplayModel
import com.sld.reflectionvsksp.vs.ISettingsModelFactory
import com.sld.reflectionvsksp.vs.ISoundModel

class LocalSettingsApi(
    private val localDataSource: SettingsLocalDataSource,
    private val modelFactory: ISettingsModelFactory
) : ISettingsApi {

    override suspend fun getSoundSettings(): ISoundModel {
        val values = localDataSource.getSettings("SOUND") ?: error(
            "SOUND settings not found in local database"
        )
        return modelFactory.createSoundModel(values)
    }

    override suspend fun getDisplaySettings(): IDisplayModel {
        val values = localDataSource.getSettings("DISPLAY") ?: error(
            "DISPLAY settings not found in local database"
        )
        return modelFactory.createDisplayModel(values)
    }

    override suspend fun saveSoundSettings(model: ISoundModel) {
        TODO("Implement local save")
    }

    override suspend fun saveDisplaySettings(model: IDisplayModel) {
        TODO("Implement local save")
    }

}