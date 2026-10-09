package com.sld.reflectionvsksp.settings

import com.sld.reflectionvsksp.api.ISettingsApi
import com.sld.reflectionvsksp.api.MockSettingsApi
import com.sld.reflectionvsksp.repository.SettingsRepository
import com.sld.reflectionvsksp.ui.SettingsViewModel
import com.sld.reflectionvsksp.vs.ISettingsMapper
import com.sld.reflectionvsksp.vs.ISettingsModelFactory
import com.sld.reflectionvsksp.vs.ksp.KspSettingsMapper
import com.sld.reflectionvsksp.vs.ksp.KspSettingsModelFactory
import com.sld.reflectionvsksp.vs.reflection.ReflectionSettingsMapper
import com.sld.reflectionvsksp.vs.reflection.ReflectionSettingsModelFactory
import org.koin.core.module.Module
import org.koin.dsl.module
import org.koin.core.module.dsl.viewModel

enum class MappingMode {
    REFLECTION,
    KSP
}

private val commonModule = module {
    single<ISettingsApi> {
        MockSettingsApi(modelFactory = get<ISettingsModelFactory>())
    }
    single {
        SettingsRepository(api = get())
    }
    viewModel {
        SettingsViewModel(repository = get(), mapper = get())
    }
}

private val reflectionModule = module {
    single<ISettingsModelFactory> {
        ReflectionSettingsModelFactory()
    }
    single<ISettingsMapper> {
        ReflectionSettingsMapper()
    }
}

private val kspModule = module {
    single<ISettingsModelFactory> {
        KspSettingsModelFactory()
    }
    single<ISettingsMapper> {
        KspSettingsMapper()
    }
}

fun createAppModules(mode: MappingMode): List<Module> {

    return listOf(
        commonModule,
        when (mode) {
            MappingMode.REFLECTION -> reflectionModule
            MappingMode.KSP -> kspModule
        },
        databaseModule
    )

}