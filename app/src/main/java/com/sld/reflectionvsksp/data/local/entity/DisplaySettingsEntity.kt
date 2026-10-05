package com.sld.reflectionvsksp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "display_settings")
data class DisplaySettingsEntity(
    @PrimaryKey
    val id: Long,
    val darkMode: Boolean,
    val brightness: Int,
    val showTraffic: Boolean,
    val theme: String
)