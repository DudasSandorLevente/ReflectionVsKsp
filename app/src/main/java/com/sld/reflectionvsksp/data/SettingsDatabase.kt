package com.sld.reflectionvsksp.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.sld.reflectionvsksp.data.local.entity.SettingsEntity
import com.sld.reflectionvsksp.data.local.entity.SettingValueEntity
import com.sld.reflectionvsksp.data.local.dao.SettingsDao

@Database(
    entities = [SettingsEntity::class, SettingValueEntity::class],
    version = 2, exportSchema = false
)
abstract class SettingsDatabase : RoomDatabase() {
    abstract fun settingsDao(): SettingsDao
}