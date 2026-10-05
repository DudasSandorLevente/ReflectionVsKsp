package com.sld.reflectionvsksp.data.local.relation

import androidx.room.Embedded
import androidx.room.Relation
import com.sld.reflectionvsksp.data.local.entity.SettingValueEntity
import com.sld.reflectionvsksp.data.local.entity.SettingsEntity

data class SettingsWithValues(
    @Embedded
    val settings: SettingsEntity,
    @Relation(parentColumn = "id", entityColumn = "settingsId")
    val values: List<SettingValueEntity>
)