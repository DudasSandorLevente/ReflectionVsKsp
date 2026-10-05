package com.sld.reflectionvsksp.vs

import com.sld.reflectionvsksp.vs.enums.SoundOutputMode

interface ISoundModel {
    val enabled: Boolean
    val volume: Int
    val voiceGuidance: Boolean
    val outputMode: SoundOutputMode
    val muteNotifications: Boolean
}