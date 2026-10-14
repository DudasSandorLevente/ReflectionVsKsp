package com.sld.reflectionvsksp.settings.reflection

import com.sld.reflectionvsksp.settings.ISettingsMapper
import com.sld.settings.annotations.reflection.ReflectionSliderSetting
import com.sld.settings.annotations.reflection.ReflectionSpinnerSetting
import com.sld.settings.annotations.reflection.ReflectionSwitchSetting
import com.sld.reflectionvsksp.settings.SettingItem
import com.sld.reflectionvsksp.settings.SliderItem
import com.sld.reflectionvsksp.settings.SpinnerItem
import com.sld.reflectionvsksp.settings.SwitchItem
import kotlin.reflect.KProperty1
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.memberProperties

class ReflectionSettingsMapper: ISettingsMapper {

    override fun map(model: Any): List<SettingItem> {

        return model::class.memberProperties.sortedBy { it.settingOrder() }.mapNotNull { property ->

            val switchAnnotation = property.findAnnotation<ReflectionSwitchSetting>()
            if (switchAnnotation != null) {
                val value = property.getter.call(model) as? Boolean
                    ?: return@mapNotNull null
                return@mapNotNull SwitchItem(
                    id = property.name, title = switchAnnotation.title, value = value
                )
            }

            val sliderAnnotation = property.findAnnotation<ReflectionSliderSetting>()
            if (sliderAnnotation != null) {
                val value = property.getter.call(model) as? Int
                        ?: return@mapNotNull null
                return@mapNotNull SliderItem(
                    id = property.name, title = sliderAnnotation.title,
                    value = value, min = sliderAnnotation.min, max = sliderAnnotation.max
                )
            }

            val spinnerAnnotation = property.findAnnotation<ReflectionSpinnerSetting>()
            if (spinnerAnnotation != null) {
                val value = property.getter.call(model) as? Enum<*>
                        ?: return@mapNotNull null
                val options = value::class.java.enumConstants.map { it.name }
                return@mapNotNull SpinnerItem(
                    id = property.name, title = spinnerAnnotation.title,
                    selected = value.name, options = options
                )
            }
            null
        }

    }

    private fun KProperty1<out Any, *>.settingOrder(): Int =
        findAnnotation<ReflectionSwitchSetting>()?.order
            ?: findAnnotation<ReflectionSliderSetting>()?.order
            ?: findAnnotation<ReflectionSpinnerSetting>()?.order
            ?: Int.MAX_VALUE

}