package com.sld.reflectionvsksp.vs.reflection

import com.sld.reflectionvsksp.vs.IDisplayModel
import com.sld.reflectionvsksp.vs.ISettingsModelFactory
import com.sld.reflectionvsksp.vs.ISoundModel
import kotlin.reflect.KClass
import kotlin.reflect.KType
import kotlin.reflect.full.primaryConstructor

class ReflectionSettingsModelFactory : ISettingsModelFactory {

    override fun createSoundModel(values: Map<String, String>): ISoundModel =
        createModel<ReflectionSoundModel>(values)

    override fun createDisplayModel(values: Map<String, String>): IDisplayModel =
        createModel<ReflectionDisplayModel>(values)

    private inline fun <reified T : Any> createModel(values: Map<String, String>): T {

        val constructor = T::class.primaryConstructor ?: error("No primary constructor for ${T::class.simpleName}")
        val arguments = constructor.parameters.associateWith { parameter ->
            val name = parameter.name ?: error("Unnamed constructor parameter")
            val rawValue = values[name] ?: error("Missing setting: $name")
            convertValue(rawValue = rawValue, type = parameter.type)
        }
        return constructor.callBy(arguments)

    }

    private fun convertValue(rawValue: String, type: KType): Any {
        return when (type.classifier) {
            Boolean::class -> rawValue.toBooleanStrict()
            Int::class -> rawValue.toInt()
            String::class -> rawValue
            else -> convertEnum(rawValue, type)
        }
    }

    private fun convertEnum(rawValue: String, type: KType): Any {
        val kClass = type.classifier as? KClass<*> ?: error("Unsupported type: $type")
        require(kClass.java.isEnum) { "Unsupported setting type: $type" }
        return kClass.java.enumConstants.filterIsInstance<Enum<*>>().firstOrNull {
            it.name == rawValue
        } ?: error("Invalid enum value '$rawValue' for ${kClass.simpleName}")
    }

}