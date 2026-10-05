package com.sld.reflectionvsksp.settings

import com.sld.reflectionvsksp.api.ISettingsApi
import com.sld.reflectionvsksp.api.MockSettingsApi
import com.sld.reflectionvsksp.repository.SettingsRepository
import com.sld.reflectionvsksp.vs.IModelFactory
import com.sld.reflectionvsksp.vs.ksp.KspModelFactory
import com.sld.reflectionvsksp.vs.reflection.ReflectionModelFactory
import org.koin.core.module.Module
import org.koin.dsl.module
import org.koin.plugin.module.dsl.viewModel
import com.sld.reflectionvsksp.ui.SettingsViewModel
import com.sld.reflectionvsksp.vs.ISettingsMapper
import com.sld.reflectionvsksp.vs.ksp.KspSettingsMapper
import com.sld.reflectionvsksp.vs.reflection.ReflectionSettingsMapper
import org.koin.core.module.dsl.viewModel

private val reflectionAndMockModule : () -> Module = {
    module {
        single<IModelFactory> {
            ReflectionModelFactory()
        }
        single<ISettingsMapper> {
            ReflectionSettingsMapper()
        }
        single<ISettingsApi> {
            MockSettingsApi(modelFactory = get())
        }
        single {
            SettingsRepository(api = get())
        }
        viewModel {
            SettingsViewModel(repository = get(), mapper = get())
        }
    }
}

private val kspAndMockModule : () -> Module = {
    module {
        single<IModelFactory> {
            KspModelFactory()
        }
        single<ISettingsMapper> {
            KspSettingsMapper()
        }
        single<ISettingsApi> {
            MockSettingsApi(modelFactory = get())
        }
        single {
            SettingsRepository(api = get())
        }
        viewModel {
            SettingsViewModel(repository = get(), mapper = get())
        }
    }
}


val appModule = reflectionAndMockModule()
//val appModule = kspAndMockModule()