package com.mouse.mouse.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.VolumeUp
import androidx.compose.material.icons.rounded.FlashOn
import androidx.compose.material.icons.rounded.Vibration
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.mouse.mouse.data.model.OutputMode
import com.mouse.mouse.ui.theme.AppDimensions

/**
 * Output Selector Component
 * 
 * Ermöglicht Auswahl der Output-Methoden:
 * - VIBRATION: Vibrationsmotor
 * - LIGHT: Kamera-Flash
 * - SOUND: Audio (TODO)
 * 
 * @param modes Liste der aktuell aktiven Modi
 * @param onToggle Callback wenn ein Modus getoggelt wird
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OutputSelector(
    modes: List<OutputMode>,
    onToggle: (OutputMode) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(AppDimensions.Spacing.xSmall)
    ) {
        OutputMode.values().forEach { mode ->
            val selected = modes.contains(mode)
            
            FilterChip(
                selected = selected,
                onClick = { onToggle(mode) },
                label = { 
                    Text(
                        mode.name,
                        style = MaterialTheme.typography.labelSmall
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = when (mode) {
                            OutputMode.VIBRATION -> Icons.Rounded.Vibration
                            OutputMode.LIGHT -> Icons.Rounded.FlashOn
                            OutputMode.SOUND -> Icons.AutoMirrored.Rounded.VolumeUp
                        },
                        contentDescription = null,
                        modifier = Modifier.size(AppDimensions.IconSize.small)
                    )
                },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f),
                    selectedLabelColor = MaterialTheme.colorScheme.primary,
                    selectedLeadingIconColor = MaterialTheme.colorScheme.primary
                )
            )
        }
    }
}
