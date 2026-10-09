package com.sld.reflectionvsksp.api

import com.sld.reflectionvsksp.vs.IDisplayModel
import com.sld.reflectionvsksp.vs.ISoundModel

interface ISettingsApi {

    suspend fun getSoundSettings(): ISoundModel
    suspend fun getDisplaySettings(): IDisplayModel
    suspend fun saveSoundSettings(model: ISoundModel)
    suspend fun saveDisplaySettings(model: IDisplayModel)

}