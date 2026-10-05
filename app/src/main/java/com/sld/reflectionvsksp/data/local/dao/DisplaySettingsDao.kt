package com.sld.reflectionvsksp.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.sld.reflectionvsksp.data.local.entity.DisplaySettingsEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface DisplaySettingsDao {

    @Upsert
    suspend fun upsert(settings: DisplaySettingsEntity)

    @Query("SELECT * FROM display_settings WHERE id = :id")
    fun observe(id: Long): Flow<DisplaySettingsEntity?>

    @Query("SELECT * FROM display_settings WHERE id = :id")
    suspend fun get(id: Long): DisplaySettingsEntity?
}