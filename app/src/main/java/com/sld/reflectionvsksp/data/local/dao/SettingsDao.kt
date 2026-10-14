package com.sld.reflectionvsksp.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.sld.reflectionvsksp.data.local.entity.SettingValueEntity
import com.sld.reflectionvsksp.data.local.entity.SettingsEntity
import com.sld.reflectionvsksp.data.local.relation.SettingsWithValues

@Dao
interface SettingsDao {

    @Upsert
    suspend fun upsertSettings(settings: SettingsEntity): Long

    @Upsert
    suspend fun upsertValue(value: SettingValueEntity)

    @Upsert
    suspend fun upsertValues(values: List<SettingValueEntity>)

    @Query("SELECT * FROM settings WHERE name = :name LIMIT 1")
    suspend fun findSettings(name: String): SettingsEntity?

    @Transaction
    @Query("SELECT * FROM settings WHERE name = :name LIMIT 1")
    suspend fun getSettings(name: String): SettingsWithValues?

    @Transaction
    @Query("SELECT * FROM settings ORDER BY id")
    suspend fun getAllSettings(): List<SettingsWithValues>

    @Query("""UPDATE setting_values SET value = :value WHERE settingsId = :settingsId AND `key` = :key""")
    suspend fun updateValue(settingsId: Long, key: String, value: String): Int

}