package com.sld.settings.annotations.reflection

@Target(AnnotationTarget.PROPERTY)
@Retention(AnnotationRetention.RUNTIME)
annotation class ReflectionSpinnerSetting(
    val title: String, val order: Int
)
