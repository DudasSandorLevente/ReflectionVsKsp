package com.sld.settings.processor.generator

import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.symbol.ClassKind
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSFile
import com.sld.settings.annotations.ksp.KspSliderSetting
import com.sld.settings.annotations.ksp.KspSpinnerSetting
import com.sld.settings.annotations.ksp.KspSwitchSetting
import com.sld.settings.processor.util.argument
import com.sld.settings.processor.util.asKotlinLiteral

class SettingsMapperGenerator(private val environment: SymbolProcessorEnvironment): IGenerator {

    override fun generate(declaration: KSClassDeclaration) {

        val modelName = declaration.simpleName.asString()
        val modelType = declaration.qualifiedName!!.asString()
        val generatedPackage = "com.sld.reflectionvsksp.generated"
        val generatedName = "Generated${modelName}Mapper"

        val itemExpressions = declaration.getAllProperties().mapNotNull { property ->

            val propertyName = property.simpleName.asString()

            val annotation = property.annotations.firstOrNull {
                val annotationName = it.annotationType.resolve().declaration.qualifiedName?.asString()
                annotationName in setOf(
                    KspSwitchSetting::class.qualifiedName,
                    KspSliderSetting::class.qualifiedName,
                    KspSpinnerSetting::class.qualifiedName
                )
            } ?: return@mapNotNull null

            val annotationName = annotation.annotationType.resolve().declaration.qualifiedName?.asString()
            val title = annotation.argument("title") as String
            val order = annotation.argument("order") as Int
            val idLiteral = propertyName.asKotlinLiteral()
            val titleLiteral = title.asKotlinLiteral()

            val expression = when (annotationName) {

                KspSwitchSetting::class.qualifiedName -> {
                    """
                    SwitchItem(
                        id = $idLiteral,
                        title = $titleLiteral,
                        value = model.$propertyName
                    )
                    """.trimIndent()
                }
                KspSliderSetting::class.qualifiedName -> {
                    val min = annotation.argument("min") as Int
                    val max = annotation.argument("max") as Int

                    """
                    SliderItem(
                        id = $idLiteral,
                        title = $titleLiteral,
                        value = model.$propertyName,
                        min = $min,
                        max = $max
                    )
                    """.trimIndent()
                }
                KspSpinnerSetting::class.qualifiedName -> {
                    val typeDeclaration = property.type.resolve().declaration
                    if (typeDeclaration !is KSClassDeclaration || typeDeclaration.classKind != ClassKind.ENUM_CLASS) {
                        environment.logger.error("@KspSpinnerSetting requires an enum property", property)
                        return@mapNotNull null
                    }
                    val enumType = typeDeclaration.qualifiedName!!.asString()
                    """
                    SpinnerItem(
                        id = $idLiteral,
                        title = $titleLiteral,
                        selected = model.$propertyName.name,
                        options = $enumType.entries.map { it.name }
                    )
                    """.trimIndent()
                }
                else -> null
            }
            expression?.let { order to it }
        }.toList()

        val sourceFile = declaration.containingFile ?: return
        val output = environment.codeGenerator.createNewFile(
            dependencies = Dependencies(aggregating = false, sourceFile),
            packageName = generatedPackage, fileName = generatedName, extensionName = "kt"
        )

        output.bufferedWriter().use { writer ->
            writer.write(
                """
            package $generatedPackage

            import com.sld.reflectionvsksp.settings.*

            object $generatedName {

                fun map(model: $modelType): List<SettingItem> =
                    listOf(
                        ${itemExpressions.sortedBy { it.first }.joinToString(",\n") { it.second }}
                    )
            }
            """.trimIndent()
            )
        }
    }

}