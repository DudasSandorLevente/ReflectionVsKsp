package com.sld.reflectionvsksp.settings.reflection

import com.sld.reflectionvsksp.data.local.model.SettingsData
import com.sld.reflectionvsksp.settings.ISettingsModelFactory
import com.sld.settings.annotations.reflection.ReflectionSettingsModel
import kotlin.reflect.KClass
import kotlin.reflect.KType
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.primaryConstructor

class ReflectionSettingsModelFactory(modelClasses: List<KClass<out Any>>) : ISettingsModelFactory {

    private val modelsByName: Map<String, KClass<out Any>> =
        modelClasses.associateBy { modelClass ->
            modelClass.findAnnotation<ReflectionSettingsModel>()?.name ?: error(
                "Missing @ReflectionSettingsModel on ${modelClass.simpleName}"
            )
        }

    override fun createModel(settings: SettingsData): Any {
        val modelClass = modelsByName[settings.name] ?: error(
            "Unknown settings category: ${settings.name}"
        )
        return createModel(modelClass, settings.values)
    }

    private fun createModel(modelClass: KClass<out Any>, values: Map<String, String>): Any {

        val constructor = modelClass.primaryConstructor ?: error(
            "No primary constructor: ${modelClass.simpleName}"
        )
        val arguments = constructor.parameters.associateWith { parameter ->
            val name = parameter.name ?: error("Unnamed constructor parameter")
            val raw = values[name] ?: error("Missing setting: $name")
            convertValue(raw, parameter.type)
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
        require(kClass.java.isEnum) {
            "Unsupported setting type: $type"
        }
        val enumConstants = requireNotNull(kClass.java.enumConstants) {
            "No enum constants available for ${kClass.simpleName}"
        }
        return enumConstants.filterIsInstance<Enum<*>>().firstOrNull {
            it.name == rawValue
        } ?: throw IllegalArgumentException(
            "Invalid enum value '$rawValue' for ${kClass.simpleName}"
        )

    }

}