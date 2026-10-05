package com.sld.reflectionvsksp.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.sld.reflectionvsksp.data.local.entity.SoundSettingsEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SoundSettingsDao {

    @Upsert
    suspend fun upsert(settings: SoundSettingsEntity)

    @Query("SELECT * FROM sound_settings WHERE id = :id")
    fun observe(id: Long): Flow<SoundSettingsEntity?>

    @Query("SELECT * FROM sound_settings WHERE id = :id")
    suspend fun get(id: Long): SoundSettingsEntity?

}