package com.sld.reflectionvsksp.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sld.reflectionvsksp.repository.SettingsRepository
import com.sld.reflectionvsksp.settings.ISettingsMapper
import com.sld.reflectionvsksp.settings.ISettingsModelFactory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import com.sld.reflectionvsksp.data.local.model.SettingsData
import com.sld.reflectionvsksp.settings.SwitchItem
import com.sld.reflectionvsksp.settings.SliderItem
import com.sld.reflectionvsksp.settings.SpinnerItem
import com.sld.reflectionvsksp.ui.view.SettingsAction
import kotlinx.coroutines.Job
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.milliseconds

class SettingsViewModel(
    private val repository: SettingsRepository,
    private val modelFactory: ISettingsModelFactory,
    private val mapper: ISettingsMapper
) : ViewModel() {

    private val _state = MutableStateFlow(SettingsUiState())
    val state: StateFlow<SettingsUiState> = _state.asStateFlow()
    private var cachedSettings: List<SettingsData> = emptyList()

    private var selectedCategory: String? = null

    private val saveJobs = mutableMapOf<String, Job>()
    private val saveMutex = Mutex()
    private val saveDelayMs = 300L

    fun loadCategories() {

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            try {

                cachedSettings = repository.loadSettings()
                Log.d("SettingsDebug", "Categories: ${cachedSettings.map { it.name }}")

                _state.update {
                    it.copy(
                        isLoading = false,
                        categories = cachedSettings.map { data -> data.name },
                        error = null
                    )
                }

            } catch (e: Exception) {
                _state.update { it.copy(isLoading = false, error = e.message) }
            }
        }

    }

    fun load(category: String) {

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            try {
                val data = cachedSettings.firstOrNull {
                    it.name == category
                } ?: error("Settings category not found: $category")
                val model = modelFactory.createModel(data)
                val items = mapper.map(model)
                selectedCategory = category
                _state.update { it.copy(isLoading = false, items = items, error = null) }
            } catch (e: Exception) {
                _state.update { it.copy(isLoading = false, error = e.message) }
            }
        }

    }

    fun onAction(action: SettingsAction) {
        when (action) {
            is SettingsAction.ValueChanged -> {
                updateValue(action.id, action.value)
                scheduleSave()
            }
        }
    }

    private fun updateValue(id: String, value: String) {

        _state.update { currentState ->

            currentState.copy(
                items = currentState.items.map { item ->

                    if (item.id != id) { item }
                    else {
                        when (item) {
                            is SwitchItem -> {
                                val newValue = value.toBooleanStrictOrNull()
                                if (newValue != null) { item.copy(value = newValue) } else { item }
                            }
                            is SliderItem -> {
                                val newValue = value.toIntOrNull()
                                if (newValue != null) {
                                    item.copy(value = newValue.coerceIn(item.min, item.max))
                                } else { item }
                            }
                            is SpinnerItem -> {
                                if (value in item.options) {
                                    item.copy(selected = value)
                                } else { item }
                            }
                        }
                    }

                }
            )

        }

    }

    private fun prepareCurrentSettings(): SettingsData? {

        val category = selectedCategory ?: return null
        val currentData = cachedSettings.firstOrNull { it.name == category } ?: return null
        val updatedValues = currentData.values.toMutableMap()

        _state.value.items.forEach { item ->
            val value = when (item) {
                is SwitchItem -> item.value.toString()
                is SliderItem -> item.value.toString()
                is SpinnerItem -> item.selected
            }
            updatedValues[item.id] = value
        }

        val updatedData = currentData.copy(values = updatedValues)
        cachedSettings = cachedSettings.map { if (it.name == category) updatedData else it }

        return updatedData
    }
    private fun scheduleSave() {

        val updatedData = prepareCurrentSettings() ?: return
        val category = updatedData.name

        saveJobs[category]?.cancel()
        saveJobs[category] = viewModelScope.launch {

            delay(saveDelayMs.milliseconds)
            try {
                saveMutex.withLock { repository.saveSettings(updatedData) }
            } catch (e: Exception) {
                _state.update { it.copy(error = e.message) }
            }

        }

    }

}