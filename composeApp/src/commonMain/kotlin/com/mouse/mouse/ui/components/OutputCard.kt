package com.mouse.mouse.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.mouse.mouse.core.domain.model.MorseMode
import com.mouse.mouse.ui.theme.*

@Composable
fun OutputCard(
    mode: MorseMode,
    outputText: String,
    onCopy: () -> Unit,
    onShare: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(Radius.lg),
        elevation = CardDefaults.cardElevation(defaultElevation = Elevation.card)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Spacing.lg)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (mode == MorseMode.TEXT_TO_MORSE) "Morse Code" else "Text",
                    style = MaterialTheme.typography.titleMedium
                )

                Row(horizontalArrangement = Arrangement.spacedBy(Spacing.xs)) {
                    IconButton(onClick = onCopy) {
                        Icon(Icons.Default.ContentCopy, "Kopieren")
                    }

                    IconButton(onClick = onShare) {
                        Icon(Icons.Default.Share, "Teilen")
                    }
                }
            }

            Spacer(modifier = Modifier.height(Spacing.sm))

            if (outputText.isEmpty()) {
                Text(
                    text = if (mode == MorseMode.TEXT_TO_MORSE) {
                        "Morse Code erscheint hier..."
                    } else {
                        "Text erscheint hier..."
                    },
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            } else {
                if (mode == MorseMode.TEXT_TO_MORSE) {
                    Text(
                        text = outputText,
                        style = MorseOutputLarge,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                } else {
                    Text(
                        text = outputText,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }

            // Optional: Visual Timeline Preview placeholder
            if (outputText.isNotEmpty() && mode == MorseMode.TEXT_TO_MORSE) {
                Spacer(modifier = Modifier.height(Spacing.lg))
                TextButton(onClick = { /* TODO: Show visual timeline */ }) {
                    Text("Visual Timeline anzeigen")
                }
            }
        }
    }
}
