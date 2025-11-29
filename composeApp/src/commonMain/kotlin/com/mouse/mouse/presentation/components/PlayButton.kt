package com.mouse.mouse.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material.icons.rounded.Stop
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import com.mouse.mouse.ui.theme.AppDimensions

/**
 * Play/Stop Button Component
 * 
 * Großer Action-Button für Morse Signal Übertragung:
 * - Nicht spielend: "TRANSMIT SIGNAL" Button
 * - Spielend: "STOP TRANSMISSION" Button
 * 
 * @param isPlaying true wenn gerade übertragen wird
 * @param onPlay Callback für Play
 * @param onStop Callback für Stop
 * @param enabled Button aktiviert/deaktiviert
 */
@Composable
fun PlayButton(
    isPlaying: Boolean,
    onPlay: () -> Unit,
    onStop: () -> Unit,
    enabled: Boolean
) {
    val haptic = LocalHapticFeedback.current
    
    Button(
        onClick = {
            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
            if (isPlaying) onStop() else onPlay()
        },
        enabled = enabled || isPlaying,
        modifier = Modifier
            .fillMaxWidth()
            .height(AppDimensions.Height.buttonLarge),
        shape = RoundedCornerShape(AppDimensions.CornerRadius.large),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isPlaying) {
                MaterialTheme.colorScheme.error
            } else {
                MaterialTheme.colorScheme.primary
            },
            disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Icon(
            imageVector = if (isPlaying) {
                Icons.Rounded.Stop
            } else {
                Icons.Rounded.PlayArrow
            },
            contentDescription = null
        )
        
        Spacer(Modifier.width(AppDimensions.Spacing.xSmall))
        
        Text(
            text = if (isPlaying) {
                "STOP TRANSMISSION"
            } else {
                "TRANSMIT SIGNAL"
            },
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold
            )
        )
    }
}
