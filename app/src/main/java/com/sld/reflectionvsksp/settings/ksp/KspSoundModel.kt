package com.sld.reflectionvsksp.settings.ksp

import com.sld.reflectionvsksp.settings.ISoundModel
import com.sld.reflectionvsksp.settings.enums.SoundOutputMode
import com.sld.settings.annotations.ksp.KspSliderSetting
import com.sld.settings.annotations.ksp.KspSpinnerSetting
import com.sld.settings.annotations.ksp.KspSwitchSetting
import com.sld.settings.annotations.ksp.SettingsModel

@SettingsModel("SOUND")
data class KspSoundModel (

    @KspSwitchSetting(title = "Sound enabled", order = 0)
    override val enabled: Boolean,

    @KspSliderSetting(title = "Volume", min = 0, max = 100, order = 1)
    override val volume: Int,

    @KspSwitchSetting(title = "Voice guidance", order = 2)
    override val voiceGuidance: Boolean,

    @KspSpinnerSetting(title = "Output mode", order = 3)
    override val outputMode: SoundOutputMode,

    @KspSwitchSetting(title = "Mute notifications", order = 4)
    override val muteNotifications: Boolean

): ISoundModel
