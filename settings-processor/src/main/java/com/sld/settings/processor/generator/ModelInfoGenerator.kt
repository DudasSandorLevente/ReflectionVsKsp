package com.sld.settings.processor.generator

import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSFile

class ModelInfoGenerator(private val environment: SymbolProcessorEnvironment): IGenerator {

    override fun generate(declaration: KSClassDeclaration) {

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

}