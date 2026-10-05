package com.sld.reflectionvsksp.vs.reflection

import com.sld.settings.annotations.reflection.ReflectionSliderSetting
import com.sld.settings.annotations.reflection.ReflectionSpinnerSetting
import com.sld.settings.annotations.reflection.ReflectionSwitchSetting
import com.sld.reflectionvsksp.vs.ISoundModel
import com.sld.reflectionvsksp.vs.enums.SoundOutputMode

data class ReflectionSoundModel(

    @ReflectionSwitchSetting("Sound enabled", order = 0)
    override val enabled: Boolean,

    @ReflectionSliderSetting(title = "Volume", min = 0, max = 100, order = 1)
    override val volume: Int,

    @ReflectionSwitchSetting("Voice guidance", order = 2)
    override val voiceGuidance: Boolean,

    @ReflectionSpinnerSetting("Output mode", order = 3)
    override val outputMode: SoundOutputMode,

    @ReflectionSwitchSetting("Mute notifications", order = 4)
    override val muteNotifications: Boolean

): ISoundModel
