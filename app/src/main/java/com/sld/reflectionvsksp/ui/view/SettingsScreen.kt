
package com.sld.reflectionvsksp.ui.view

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.TextButton
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sld.reflectionvsksp.ui.SettingsViewModel

@Composable
fun SettingsScreen(name: String, viewModel: SettingsViewModel, modifier: Modifier = Modifier) {

    val state by viewModel.state.collectAsStateWithLifecycle()
    var selectedCategory by rememberSaveable { mutableStateOf<String?>(null) }

    LaunchedEffect(viewModel) {
        viewModel.loadCategories()
    }
    BackHandler(enabled = selectedCategory != null) {
        selectedCategory = null
    }
    Column(modifier = modifier.fillMaxSize()) {

        if (selectedCategory == null) {

            Text(
                text = "Settings",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.align(Alignment.CenterHorizontally).padding(24.dp)
            )
            Column(
                modifier = Modifier.weight(1f).fillMaxWidth(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CategorySelector(categories = state.categories, onCategorySelected = { category ->
                    selectedCategory = category
                    viewModel.load(category)
                })
            }

        } else {

            Row(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                TextButton(onClick = { selectedCategory = null }) {
                    Text(
                        text = "← Back",
                        style = MaterialTheme.typography.titleMedium
                    )
                }
                Text(
                    text = selectedCategory.orEmpty(),
                    style = MaterialTheme.typography.headlineSmall
                )
            }
            SettingsContent(
                state = state,
                onAction = viewModel::onAction,
                modifier = Modifier.weight(1f).fillMaxWidth()
            )

        }

    }

}
