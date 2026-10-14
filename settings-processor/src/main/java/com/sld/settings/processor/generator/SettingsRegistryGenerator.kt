package com.sld.settings.processor.generator

import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.symbol.KSFile
import com.sld.settings.processor.model.SettingsModelDescriptor
import com.sld.settings.processor.util.asKotlinLiteral

class SettingsRegistryGenerator(
    private val environment: SymbolProcessorEnvironment
): IRegistryGenerator {

    override fun generate(models: List<SettingsModelDescriptor>, sourceFiles: List<KSFile>) {

        environment.logger.warn("Registry generation: ${models.size} models")

        if (models.isEmpty()) return
        val generatedPackage = "com.sld.reflectionvsksp.vs.ksp.generated"
        val generatedName = "GeneratedSettingsModelRegistry"

        val factoryCases = models
            .sortedBy { it.categoryName }
            .joinToString("\n") { model ->
                val factoryType = "${model.modelPackage}.generated.Generated${model.modelName}Factory"
                val categoryLiteral = model.categoryName.asKotlinLiteral()
                "$categoryLiteral -> $factoryType.create(values)"
            }

        val mapperCases = models
            .sortedBy { it.categoryName }
            .joinToString("\n") { model ->
                val modelType = "${model.modelPackage}.${model.modelName}"
                val mapperType = "com.sld.reflectionvsksp.generated.Generated${model.modelName}Mapper"
                "is $modelType -> $mapperType.map(model)"
            }

        val content = """
            package $generatedPackage

            import com.sld.reflectionvsksp.settings.SettingItem

            object $generatedName {

                fun create(
                    name: String,
                    values: Map<String, String>
                ): Any = when (name) {
                    ${factoryCases.prependIndent("            ").trimStart()}
                    else -> error("Unknown settings category: ${'$'}name")
                }

                fun map(model: Any): List<SettingItem> = when (model) {
                    ${mapperCases.prependIndent("            ").trimStart()}
                    else -> error(
                        "Unsupported KSP settings model: ${'$'}{model::class.qualifiedName}"
                    )
                }
            }
        """.trimIndent()

        environment.codeGenerator.createNewFile(
            dependencies = Dependencies(aggregating = true, *sourceFiles.toTypedArray()),
            packageName = generatedPackage, fileName = generatedName
        ).bufferedWriter().use { writer -> writer.write(content) }

    }
}