package com.sld.reflectionvsksp.ui.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sld.reflectionvsksp.ui.SettingsViewModel
import androidx.compose.runtime.LaunchedEffect

@Composable
fun SettingsScreen(name: String, viewModel: SettingsViewModel, modifier: Modifier = Modifier) {

    val state by viewModel.state.collectAsStateWithLifecycle()
    LaunchedEffect(viewModel) {
        viewModel.loadCategories()
    }
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        CategorySelector(
            categories = state.categories,
            onCategorySelected = viewModel::load
        )
        SettingsContent(
            state = state,
            onAction = viewModel::onAction,
            modifier = Modifier.weight(1f).fillMaxWidth()
        )
    }
}
