package com.sld.settings.processor.generator

import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSFile

interface IGenerator {

    fun generate(declaration: KSClassDeclaration)

}
