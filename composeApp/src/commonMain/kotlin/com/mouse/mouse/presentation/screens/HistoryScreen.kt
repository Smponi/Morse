package com.mouse.mouse.presentation.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.mouse.mouse.presentation.components.HistoryItem
import com.mouse.mouse.presentation.viewmodel.MorseSuiteViewModel
import com.mouse.mouse.ui.theme.AppDimensions

/**
 * Screen für History und Favoriten
 * 
 * @param showFavoritesOnly true = nur Favoriten anzeigen, false = alle Einträge
 */
@Composable
fun HistoryScreen(viewModel: MorseSuiteViewModel, showFavoritesOnly: Boolean) {
    val items = if (showFavoritesOnly) {
        viewModel.history.filter { it.isFavorite }
    } else {
        viewModel.history
    }

    Column(modifier = Modifier.fillMaxSize().padding(AppDimensions.Spacing.medium)) {
        Text(
            text = if (showFavoritesOnly) "FAVORITES" else "RECENT TRANSMISSIONS",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(
                bottom = AppDimensions.Spacing.medium,
                start = AppDimensions.Spacing.xSmall
            )
        )

        if (items.isEmpty()) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(
                    "No records yet.",
                    color = Color.Gray,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(AppDimensions.Spacing.small)) {
                items(items, key = { it.id }) { record ->
                    HistoryItem(
                        record = record,
                        onFavoriteToggle = { viewModel.toggleFavorite(record.id) },
                        onLoad = { viewModel.loadFromHistory(record.text) }
                    )
                }
            }
        }
    }
}
