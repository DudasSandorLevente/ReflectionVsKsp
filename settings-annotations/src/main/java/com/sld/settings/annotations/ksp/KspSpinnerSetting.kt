package com.sld.settings.annotations.ksp

@Target(AnnotationTarget.PROPERTY)
@Retention(AnnotationRetention.RUNTIME)
annotation class KspSpinnerSetting(val title: String, val order: Int)
