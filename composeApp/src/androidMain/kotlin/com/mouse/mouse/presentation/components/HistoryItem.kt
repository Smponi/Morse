package com.mouse.mouse.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextOverflow
import com.mouse.mouse.data.model.MorseRecord
import com.mouse.mouse.ui.theme.AppDimensions
import java.text.SimpleDateFormat
import java.util.*

/**
 * History Item Component
 * 
 * Einzelner Eintrag in der History/Favorites Liste
 * 
 * @param record Der anzuzeigende MorseRecord
 * @param onFavoriteToggle Callback zum Togglen des Favoriten-Status
 * @param onLoad Callback zum Laden des Records zurück ins Input-Feld
 */
@Composable
fun HistoryItem(
    record: MorseRecord,
    onFavoriteToggle: () -> Unit,
    onLoad: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onLoad() },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        shape = RoundedCornerShape(AppDimensions.CornerRadius.medium)
    ) {
        Row(
            modifier = Modifier.padding(AppDimensions.Spacing.medium),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Content
            Column(modifier = Modifier.weight(1f)) {
                // Original Text
                Text(
                    text = record.text,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                
                // Morse Translation
                Text(
                    text = record.morse,
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontFamily = FontFamily.Monospace
                    ),
                    color = MaterialTheme.colorScheme.secondary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                
                // Timestamp
                Text(
                    text = SimpleDateFormat("HH:mm", Locale.getDefault())
                        .format(Date(record.timestamp)),
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.Gray
                )
            }
            
            // Favorite Button
            IconButton(onClick = onFavoriteToggle) {
                Icon(
                    imageVector = if (record.isFavorite) {
                        Icons.Rounded.Star
                    } else {
                        Icons.Outlined.StarBorder
                    },
                    contentDescription = "Favorite",
                    tint = if (record.isFavorite) {
                        MaterialTheme.colorScheme.secondary
                    } else {
                        Color.Gray
                    }
                )
            }
        }
    }
}
