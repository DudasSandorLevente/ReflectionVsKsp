package com.sld.settings.annotations.reflection

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class ReflectionSettingsModel(val name: String)
