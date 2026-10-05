package com.sld.reflectionvsksp.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sld.reflectionvsksp.repository.SettingsRepository
import com.sld.reflectionvsksp.settings.SettingsCategory
import com.sld.reflectionvsksp.vs.ISettingsMapper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val repository: SettingsRepository,
    private val mapper: ISettingsMapper
) : ViewModel() {

    private val _state = MutableStateFlow(SettingsUiState())
    val state: StateFlow<SettingsUiState> = _state.asStateFlow()

    fun load(category: SettingsCategory) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            try {
                val model = when (category) {
                    SettingsCategory.SOUND -> repository.loadSoundSettings()
                    SettingsCategory.DISPLAY -> repository.loadDisplaySettings()
                }
                val items = mapper.map(model)
                _state.update { it.copy(isLoading = false, items = items, error = null) }

            } catch (e: Exception) {
                _state.update { it.copy(isLoading = false, error = e.message) }
            }
        }
    }
}