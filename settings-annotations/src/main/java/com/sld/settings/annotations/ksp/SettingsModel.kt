package com.sld.settings.annotations.ksp

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.SOURCE)
annotation class SettingsModel(val name: String)