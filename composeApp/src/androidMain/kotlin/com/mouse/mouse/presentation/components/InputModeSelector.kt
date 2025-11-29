package com.mouse.mouse.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import com.mouse.mouse.data.model.InputMode
import com.mouse.mouse.ui.theme.AppDimensions

/**
 * Input Mode Selector Component
 * 
 * Toggle zwischen TEXT und MORSE Eingabe-Modus
 * 
 * @param currentMode Der aktuell aktive Modus
 * @param onModeChanged Callback wenn der Modus gewechselt wird
 */
@Composable
fun InputModeSelector(
    currentMode: InputMode,
    onModeChanged: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(AppDimensions.Height.buttonSmall)
            .clip(RoundedCornerShape(AppDimensions.CornerRadius.round))
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .clickable { onModeChanged() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Tab: TEXT TO MORSE
        TabButton(
            text = "TEXT TO MORSE",
            isSelected = currentMode == InputMode.TEXT,
            modifier = Modifier.weight(1f),
            onClick = {
                if (currentMode != InputMode.TEXT) onModeChanged()
            }
        )
        
        // Tab: MORSE TO TEXT
        TabButton(
            text = "MORSE TO TEXT",
            isSelected = currentMode == InputMode.MORSE,
            modifier = Modifier.weight(1f),
            onClick = {
                if (currentMode != InputMode.MORSE) onModeChanged()
            }
        )
    }
}

/**
 * Einzelner Tab Button
 */
@Composable
private fun TabButton(
    text: String,
    isSelected: Boolean,
    modifier: Modifier,
    onClick: () -> Unit
) {
    val bgColor = if (isSelected) {
        MaterialTheme.colorScheme.primary
    } else {
        Color.Transparent
    }
    
    val textColor = if (isSelected) {
        MaterialTheme.colorScheme.onPrimary
    } else {
        Color.Gray
    }

    Box(
        modifier = modifier
            .fillMaxHeight()
            .padding(AppDimensions.Spacing.xxSmall)
            .clip(RoundedCornerShape(AppDimensions.CornerRadius.round))
            .background(bgColor)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            fontWeight = FontWeight.Bold,
            color = textColor,
            style = MaterialTheme.typography.labelMedium
        )
    }
}
