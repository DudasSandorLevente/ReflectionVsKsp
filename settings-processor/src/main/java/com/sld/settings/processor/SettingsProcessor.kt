package com.sld.settings.processor

import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.symbol.ClassKind
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSAnnotation
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSPropertyDeclaration
import com.google.devtools.ksp.validate
import com.sld.settings.annotations.ksp.KspSliderSetting
import com.sld.settings.annotations.ksp.KspSpinnerSetting
import com.sld.settings.annotations.ksp.KspSwitchSetting
import com.sld.settings.annotations.ksp.SettingsModel
import com.sld.settings.processor.generator.IGenerator
import com.sld.settings.processor.generator.IRegistryGenerator
import com.sld.settings.processor.generator.ModelInfoGenerator
import com.sld.settings.processor.generator.SettingsFactoryGenerator
import com.sld.settings.processor.generator.SettingsMapperGenerator
import com.sld.settings.processor.generator.SettingsRegistryGenerator
import com.sld.settings.processor.model.SettingsModelDescriptor
import com.sld.settings.processor.util.argument

class SettingsProcessor(private val environment: SymbolProcessorEnvironment) : SymbolProcessor {

    private val modelInfoGenerator: IGenerator = ModelInfoGenerator(environment)
    private val mapperGenerator: IGenerator = SettingsMapperGenerator(environment)
    private val factoryGenerator: IGenerator = SettingsFactoryGenerator(environment)
    private val registryGenerator: IRegistryGenerator = SettingsRegistryGenerator(environment)
    private val collectedModels = mutableMapOf<String, SettingsModelDescriptor>()

    private var registryGenerated = false
    override fun process(resolver: Resolver): List<KSAnnotated> {

        val symbols = resolver.getSymbolsWithAnnotation(
            SettingsModel::class.qualifiedName!!
        ).toList()
        symbols.filter { it.validate() }
            .filterIsInstance<KSClassDeclaration>().forEach { declaration ->

                val qualifiedName = declaration.qualifiedName?.asString() ?: return@forEach
                val annotation = declaration.annotations.first {
                    it.annotationType.resolve().declaration.qualifiedName?.asString() ==
                            SettingsModel::class.qualifiedName
                }
                val descriptor = SettingsModelDescriptor(
                    categoryName = annotation.argument("name") as String,
                    modelName = declaration.simpleName.asString(),
                    modelPackage = declaration.packageName.asString()
                )

                if (collectedModels.putIfAbsent(qualifiedName, descriptor) != null) return@forEach

                modelInfoGenerator.generate(declaration)
                mapperGenerator.generate(declaration)
                factoryGenerator.generate(declaration)

            }

        if (!registryGenerated && collectedModels.isNotEmpty()) {

            val sourceFiles = resolver.getSymbolsWithAnnotation(
                SettingsModel::class.qualifiedName!!
            ).filterIsInstance<KSClassDeclaration>().mapNotNull {
                it.containingFile
            }.distinct().toList()

            registryGenerator.generate(
                models = collectedModels.values.toList(),
                sourceFiles = sourceFiles
            )

            registryGenerated = true
        }

        return symbols.filterNot { it.validate() }
    }

}