package com.sld.reflectionvsksp.settings

import com.sld.reflectionvsksp.api.ISettingsApi
import com.sld.reflectionvsksp.api.LocalSettingsApi
import com.sld.reflectionvsksp.api.MockSettingsApi
import com.sld.reflectionvsksp.repository.SettingsRepository
import com.sld.reflectionvsksp.ui.SettingsViewModel
import com.sld.reflectionvsksp.settings.ksp.KspSettingsMapper
import com.sld.reflectionvsksp.settings.ksp.KspSettingsModelFactory
import com.sld.reflectionvsksp.settings.reflection.ReflectionDisplayModel
import com.sld.reflectionvsksp.settings.reflection.ReflectionNetworkModel
import com.sld.reflectionvsksp.settings.reflection.ReflectionSettingsMapper
import com.sld.reflectionvsksp.settings.reflection.ReflectionSettingsModelFactory
import com.sld.reflectionvsksp.settings.reflection.ReflectionSoundModel
import org.koin.core.module.Module
import org.koin.dsl.module
import org.koin.core.module.dsl.viewModel

enum class MappingMode {
    REFLECTION,
    KSP
}
enum class DataSourceMode {
    MOCK, LOCAL
}

private val commonModule = module {

    single {
        SettingsRepository(api = get())
    }
    viewModel {
        SettingsViewModel(repository = get(), modelFactory = get(), mapper = get())
    }

}

private val mockApiModule = module {

    single<ISettingsApi> {
        MockSettingsApi()
    }

}

private val localApiModule = module {

    single<ISettingsApi> {
        LocalSettingsApi(localDataSource = get())
    }

}

private val reflectionModule = module {

    single<ISettingsModelFactory> {
        ReflectionSettingsModelFactory(
            modelClasses = listOf(
                ReflectionSoundModel::class,
                ReflectionDisplayModel::class,
                ReflectionNetworkModel::class
            )
        )
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

fun createAppModules(mappingMode: MappingMode, sourceMode: DataSourceMode): List<Module> {

    return listOf(
        commonModule,
        when (sourceMode) {
            DataSourceMode.MOCK -> mockApiModule
            DataSourceMode.LOCAL -> localApiModule
        },
        when (mappingMode) {
            MappingMode.REFLECTION -> reflectionModule
            MappingMode.KSP -> kspModule
        },
        databaseModule
    )

}