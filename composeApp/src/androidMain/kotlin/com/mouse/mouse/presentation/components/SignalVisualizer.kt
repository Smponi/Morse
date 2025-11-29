package com.mouse.mouse.presentation.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Bolt
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.mouse.mouse.ui.theme.AppDimensions

/**
 * Signal Visualizer Component
 * 
 * Visuelles Feedback für Morse-Signal Status:
 * - Aktiv: Leuchtet in Primary-Farbe und pulsiert
 * - Inaktiv: Grau und statisch
 * 
 * @param isSignalActive true wenn gerade ein Signal gesendet wird
 */
@Composable
fun SignalVisualizer(isSignalActive: Boolean) {
    // Animationen für smooth transitions
    val color by animateColorAsState(
        targetValue = if (isSignalActive) {
            MaterialTheme.colorScheme.primary
        } else {
            MaterialTheme.colorScheme.surfaceVariant
        },
        label = "color"
    )
    
    val scale by animateFloatAsState(
        targetValue = if (isSignalActive) 1.15f else 1f,
        label = "scale"
    )

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.size(AppDimensions.Height.visualizer)
    ) {
        Box(
            modifier = Modifier
                .size(80.dp)
                .scale(scale)
                .clip(CircleShape)
                .background(color)
                .border(
                    AppDimensions.Border.thick,
                    Color.White.copy(alpha = 0.1f),
                    CircleShape
                )
        ) {
            Icon(
                Icons.Rounded.Bolt,
                contentDescription = null,
                tint = if (isSignalActive) {
                    Color.White
                } else {
                    Color.White.copy(0.3f)
                },
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(AppDimensions.IconSize.large)
            )
        }
    }
}
