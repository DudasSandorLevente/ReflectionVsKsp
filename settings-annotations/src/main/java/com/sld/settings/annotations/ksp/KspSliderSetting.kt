package com.sld.settings.annotations.ksp

@Target(AnnotationTarget.PROPERTY)
@Retention(AnnotationRetention.RUNTIME)
annotation class KspSliderSetting(
    val title: String, val min: Int, val max: Int, val order: Int
)