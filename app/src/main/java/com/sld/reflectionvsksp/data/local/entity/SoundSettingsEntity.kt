package com.sld.reflectionvsksp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "sound_settings")
data class SoundSettingsEntity(
    @PrimaryKey
    val id: Long,
    val enabled: Boolean,
    val volume: Int,
    val voiceGuidance: Boolean,
    val outputMode: String
)