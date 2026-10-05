package com.sld.reflectionvsksp.vs.reflection

import com.sld.reflectionvsksp.vs.IDisplayModel
import com.sld.reflectionvsksp.vs.IModelFactory
import com.sld.reflectionvsksp.vs.ISoundModel
import com.sld.reflectionvsksp.vs.enums.DisplayTheme
import com.sld.reflectionvsksp.vs.enums.SoundOutputMode

class ReflectionModelFactory : IModelFactory {

    override fun createDisplayModel(
        darkMode: Boolean, brightness: Int, showTraffic: Boolean, theme: DisplayTheme
    ): IDisplayModel {
        return ReflectionDisplayModel(darkMode, brightness, showTraffic, theme)
    }

    override fun createSoundModel(
        enabled: Boolean, volume: Int, voiceGuidance: Boolean, outputMode: SoundOutputMode, muteNotifications: Boolean
    ): ISoundModel {
        return ReflectionSoundModel(enabled, volume, voiceGuidance, outputMode, muteNotifications)
    }

}