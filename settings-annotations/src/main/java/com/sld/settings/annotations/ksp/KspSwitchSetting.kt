package com.sld.settings.annotations.ksp

@Target(AnnotationTarget.PROPERTY)
@Retention(AnnotationRetention.RUNTIME)
annotation class KspSwitchSetting(
    val title: String, val order: Int
)