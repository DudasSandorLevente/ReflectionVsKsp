package com.sld.reflectionvsksp.settings

import androidx.room.Room
import com.sld.reflectionvsksp.data.SettingsDatabase
import com.sld.reflectionvsksp.data.SettingsLocalDataSource
import com.sld.reflectionvsksp.data.local.dao.SettingsDao
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val databaseModule = module {

    single<SettingsDatabase> {
        Room.databaseBuilder(
            androidContext(), SettingsDatabase::class.java, "settings.db"
        ).build()
    }

    single<SettingsDao> {
        get<SettingsDatabase>().settingsDao()
    }

    single {
        SettingsLocalDataSource(
            settingsDao = get()
        )
    }

}