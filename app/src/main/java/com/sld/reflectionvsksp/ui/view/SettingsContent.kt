package com.sld.reflectionvsksp.ui.view

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.sld.reflectionvsksp.ui.SettingsUiState

@Composable
fun SettingsContent(state: SettingsUiState, modifier: Modifier) {
    when {
        state.isLoading -> {
            CircularProgressIndicator()
        }
        state.error != null -> {
            Text(text = state.error)
        }
        else -> {
            LazyColumn {
                items(items = state.items, key = { item -> item.id }) { item ->
                    SettingsRow(item)
                }
            }
        }
    }
}