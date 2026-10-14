package com.sld.settings.processor.util

import com.google.devtools.ksp.symbol.KSAnnotation

internal fun KSAnnotation.argument(name: String): Any? = arguments.firstOrNull {
    it.name?.asString() == name
}?.value

internal fun String.asKotlinLiteral(): String = buildString {

    append('"')
    for (char in this@asKotlinLiteral) {
        append(when (char) {
            '\\' -> "\\\\"
            '"' -> "\\\""
            '\n' -> "\\n"
            '\r' -> "\\r"
            '\t' -> "\\t"
            '$' -> "\\$"
            else -> char.toString()
        })
    }
    append('"')

}