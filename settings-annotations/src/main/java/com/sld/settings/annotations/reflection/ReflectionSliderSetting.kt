package com.sld.settings.annotations.reflection

@Target(AnnotationTarget.PROPERTY)
@Retention(AnnotationRetention.RUNTIME)
annotation class ReflectionSliderSetting(
    val title: String, val min: Int, val max: Int, val order: Int
)
