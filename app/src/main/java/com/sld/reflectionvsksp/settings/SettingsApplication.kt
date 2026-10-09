package com.sld.reflectionvsksp.settings

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class SettingsApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@SettingsApplication)
            modules(createAppModules(MappingMode.REFLECTION))
        }
    }

}