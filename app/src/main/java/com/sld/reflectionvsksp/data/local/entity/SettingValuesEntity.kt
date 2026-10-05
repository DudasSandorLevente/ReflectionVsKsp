package com.sld.reflectionvsksp.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "setting_values",
    foreignKeys = [
        ForeignKey(
            entity = SettingsEntity::class, parentColumns = ["id"],
            childColumns = ["settingsId"], onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("settingsId"), Index(value = ["settingsId", "key"], unique = true)]
)
data class SettingValueEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val settingsId: Long,
    val key: String,
    val value: String
)