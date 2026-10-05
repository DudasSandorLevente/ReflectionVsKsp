package com.sld.reflectionvsksp.vs

import com.sld.reflectionvsksp.vs.enums.DisplayTheme
import com.sld.reflectionvsksp.vs.enums.SoundOutputMode

interface IModelFactory {

    fun createDisplayModel(
        darkMode: Boolean, brightness: Int, showTraffic: Boolean, theme: DisplayTheme
    ): IDisplayModel
    fun createSoundModel(
        enabled: Boolean, volume: Int, voiceGuidance: Boolean, outputMode: SoundOutputMode, muteNotifications: Boolean
    ): ISoundModel

}