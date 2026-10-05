package com.sld.reflectionvsksp.ui

import com.sld.reflectionvsksp.settings.SettingItem

data class SettingsUiState(
    val isLoading: Boolean = false,
    val items: List<SettingItem> = emptyList(),
    val error: String? = null
)