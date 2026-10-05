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
import com.sld.settings.annotations.ksp.KspSliderSetting
import com.sld.settings.annotations.ksp.KspSpinnerSetting
import com.sld.settings.annotations.ksp.KspSwitchSetting
import com.sld.settings.annotations.ksp.SettingsModel

class SettingsProcessor(private val environment: SymbolProcessorEnvironment) : SymbolProcessor {

    private val generatedModels = mutableSetOf<String>()

    override fun process(resolver: Resolver): List<KSAnnotated> {

        val symbols = resolver.getSymbolsWithAnnotation(
            SettingsModel::class.qualifiedName!!
        )

        // symbols.filterIsInstance<KSClassDeclaration>().forEach { declaration -> logDeclaration(declaration)}

        symbols.filterIsInstance<KSClassDeclaration>().forEach { declaration ->

            val modelName = declaration.qualifiedName?.asString() ?: return@forEach
            if (!generatedModels.add(modelName)) return@forEach

            logDeclaration(declaration)
            generateModelInfo(declaration)
            generateMapper(declaration)

            declaration.getAllProperties().forEach { property ->

                val propertyName = property.simpleName.asString()
                val propertyType = property.type.resolve().declaration.qualifiedName?.asString()
                environment.logger.warn("  $propertyName: $propertyType")

                property.annotations.forEach { annotation ->

                    val annotationName = annotation.annotationType.resolve().declaration.qualifiedName?.asString()
                    environment.logger.warn("    Annotation: $annotationName")

                    annotation.arguments.forEach { argument ->

                        val name = argument.name?.asString()
                        val value = argument.value
                        environment.logger.warn("    $name = $value")

                    }

                }

                val isSpinner = property.annotations.any { annotation ->
                    annotation.annotationType.resolve().declaration.qualifiedName?.asString() ==
                            KspSpinnerSetting::class.qualifiedName
                }
                if (isSpinner) processSpinnerProperty(property)

            }

        }

        return emptyList()
    }

    private fun logDeclaration(declaration: KSClassDeclaration){
        environment.logger.warn(
            "Found SettingsModel: ${declaration.simpleName.asString()}"
        )
    }

    private fun KSAnnotation.argument(name: String): Any? =
        arguments.firstOrNull {
            it.name?.asString() == name
        }?.value

    private fun generateModelInfo(declaration: KSClassDeclaration) {

        val modelName = declaration.simpleName.asString()
        val packageName = "com.sld.settings.generated"
        val generatedName = "${modelName}Info"
        val sourceFile = declaration.containingFile ?: return
        val output = environment.codeGenerator.createNewFile(
            dependencies = Dependencies(aggregating = false, sourceFile),
            packageName = packageName, fileName = generatedName, extensionName = "kt"
        )

        output.bufferedWriter().use { writer ->
            writer.write(
                """
            package $packageName
            object $generatedName {
                const val MODEL_NAME = "$modelName"
            }
            """.trimIndent()
            )
        }

    }

    private fun processSpinnerProperty(property: KSPropertyDeclaration) {

        val typeDeclaration = property.type.resolve().declaration
        if (typeDeclaration !is KSClassDeclaration || typeDeclaration.classKind != ClassKind.ENUM_CLASS) {
            environment.logger.error(
                "@SpinnerSetting can only be used on enum properties", property
            )
            return
        }

        val enumType = typeDeclaration.qualifiedName?.asString() ?: return
        val enumEntries = typeDeclaration.declarations
            .filterIsInstance<KSClassDeclaration>()
            .filter { it.classKind == ClassKind.ENUM_ENTRY }
            .map { it.simpleName.asString() }
            .toList()
        environment.logger.warn("Spinner: ${property.simpleName.asString()}")
        environment.logger.warn("Enum type: $enumType")
        environment.logger.warn("Options: $enumEntries")

    }

    private fun String.asKotlinLiteral(): String = buildString {
        append('"')
        for (char in this@asKotlinLiteral) {
            append(
                when (char) {
                    '\\' -> "\\\\"
                    '"' -> "\\\""
                    '\n' -> "\\n"
                    '\r' -> "\\r"
                    '\t' -> "\\t"
                    '$' -> "\\$"
                    else -> char.toString()
                }
            )
        }
        append('"')
    }

    private fun generateMapper(declaration: KSClassDeclaration) {

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