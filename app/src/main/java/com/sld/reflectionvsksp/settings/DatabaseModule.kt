package com.sld.reflectionvsksp.settings

import androidx.room.Room
import com.sld.reflectionvsksp.data.SettingsDatabase
import com.sld.reflectionvsksp.data.SettingsLocalDataSource
import com.sld.reflectionvsksp.data.local.dao.SettingsDao
import org.koin.core.module.Module
import org.koin.dsl.module
import org.koin.android.ext.koin.androidContext

private val roomDatabaseModule : () -> Module = {
    module {
        single<SettingsDatabase> {
            Room.databaseBuilder(androidContext(),
                SettingsDatabase::class.java, "settings.db"
            ).build()
        }
        single<SettingsDao> { get<SettingsDatabase>().settingsDao() }
        single { SettingsLocalDataSource(settingsDao = get()) }
    }
}

val databaseModule = roomDatabaseModule()
//val databaseModule =