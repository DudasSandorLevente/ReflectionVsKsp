package com.sld.settings.processor.generator

import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.symbol.ClassKind
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSFile
import com.google.devtools.ksp.symbol.KSPropertyDeclaration

class SettingsFactoryGenerator(environment: SymbolProcessorEnvironment): IGenerator {

    private val codeGenerator = environment.codeGenerator
    private fun valueExpression(property: KSPropertyDeclaration): String {

        val name = property.simpleName.asString()
        val type = property.type.resolve()
        val declaration = type.declaration
        val qualifiedType = declaration.qualifiedName?.asString()

        return when (qualifiedType) {
            "kotlin.Boolean" ->
                """values["$name"]?.toBooleanStrict() ?: error("Missing setting: $name")"""

            "kotlin.Int" ->
                """values["$name"]?.toInt() ?: error("Missing setting: $name")"""

            "kotlin.String" ->
                """values["$name"] ?: error("Missing setting: $name")"""

            else -> {
                val classDeclaration = declaration as? KSClassDeclaration

                if (classDeclaration?.classKind == ClassKind.ENUM_CLASS) {
                    """$qualifiedType.valueOf(values["$name"] ?: error("Missing setting: $name"))"""
                } else {
                    """TODO("Unsupported type: $qualifiedType")"""
                }
            }
        }

    }

    override fun generate(declaration: KSClassDeclaration) {

        val packageName = declaration.packageName.asString()
        val modelName = declaration.simpleName.asString()
        val modelType = declaration.qualifiedName!!.asString()
        val factoryName = "Generated${modelName}Factory"
        val properties = declaration.getAllProperties().toList()
        val constructorArguments = properties.joinToString(",\n") { property ->
            val name = property.simpleName.asString()
            val expression = valueExpression(property)
            "            $name = $expression"
        }

        val content = """
        package $packageName.generated

        object $factoryName {

            fun create(
                values: Map<String, String>
            ): $modelType {
                return $modelType($constructorArguments)
            }
        }
        """.trimIndent()

        val file = codeGenerator.createNewFile(
            dependencies = Dependencies(aggregating = false, declaration.containingFile!!),
            packageName = "$packageName.generated",
            fileName = factoryName
        )

        file.bufferedWriter().use { writer ->
            writer.write(content)
        }
    }

}