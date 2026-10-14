package com.sld.reflectionvsksp.settings

import com.sld.reflectionvsksp.settings.enums.SoundOutputMode

interface ISoundModel {
    val enabled: Boolean
    val volume: Int
    val voiceGuidance: Boolean
    val outputMode: SoundOutputMode
    val muteNotifications: Boolean
}