package com.sld.reflectionvsksp.ui.view

sealed interface SettingsAction {

    data class ValueChanged(val id: String, val value: String) : SettingsAction

}