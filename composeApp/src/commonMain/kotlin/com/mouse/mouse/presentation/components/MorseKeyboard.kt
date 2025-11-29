package com.mouse.mouse.presentation.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.Backspace
import androidx.compose.material.icons.rounded.SpaceBar
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mouse.mouse.ui.theme.AppDimensions

/**
 * Redesigned Morse Keyboard Component
 * 
 * Modernes, minimalistisches Design:
 * - Große Touch-Targets für DOT und DASH
 * - Separate Buttons für Letter Space und Word Space
 * - Clear visuelles Feedback
 * - Smooth Press-Animationen
 */
@Composable
fun MorseKeyboard(
    onDot: () -> Unit,
    onDash: () -> Unit,
    onLetterSpace: () -> Unit,
    onWordSpace: () -> Unit,
    onDelete: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(AppDimensions.Spacing.small)
    ) {
        // Row 1: DOT und DASH (die Haupteingabe)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp),
            horizontalArrangement = Arrangement.spacedBy(AppDimensions.Spacing.small)
        ) {
            MorseButton(
                modifier = Modifier.weight(1f),
                label = "·",
                sublabel = "DOT",
                color = MaterialTheme.colorScheme.primary,
                onClick = onDot
            )
            
            MorseButton(
                modifier = Modifier.weight(1f),
                label = "─",
                sublabel = "DASH",
                color = MaterialTheme.colorScheme.secondary,
                onClick = onDash
            )
        }
        
        // Row 2: Utility Buttons (Letter Space, Word Space, Delete)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            horizontalArrangement = Arrangement.spacedBy(AppDimensions.Spacing.xSmall)
        ) {
            // Letter Space (trennt Buchstaben)
            UtilityButton(
                modifier = Modifier.weight(1f),
                icon = null,
                text = "LETTER",
                color = MaterialTheme.colorScheme.surfaceVariant,
                textColor = MaterialTheme.colorScheme.onSurface,
                onClick = onLetterSpace
            )
            
            // Word Space (Slash für Wörter)
            UtilityButton(
                modifier = Modifier.weight(1f),
                icon = Icons.Rounded.SpaceBar,
                text = "WORD",
                color = MaterialTheme.colorScheme.surfaceVariant,
                textColor = MaterialTheme.colorScheme.onSurface,
                onClick = onWordSpace
            )
            
            // Delete
            UtilityButton(
                modifier = Modifier.width(72.dp),
                icon = Icons.AutoMirrored.Rounded.Backspace,
                text = null,
                color = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.5f),
                textColor = MaterialTheme.colorScheme.error,
                onClick = onDelete
            )
        }
    }
}

/**
 * Morse Button Component (DOT/DASH)
 * 
 * Große, touch-freundliche Buttons mit Press-Animation
 */
@Composable
private fun RowScope.MorseButton(
    modifier: Modifier,
    label: String,
    sublabel: String,
    color: Color,
    onClick: () -> Unit
) {
    val haptic = LocalHapticFeedback.current
    var isPressed by remember { mutableStateOf(false) }
    
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.95f else 1f,
        label = "button_scale"
    )

    Surface(
        modifier = modifier
            .fillMaxHeight()
            .scale(scale)
            .pointerInput(Unit) {
                detectTapGestures(
                    onPress = {
                        isPressed = true
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        tryAwaitRelease()
                        isPressed = false
                    },
                    onTap = { onClick() }
                )
            },
        shape = RoundedCornerShape(AppDimensions.CornerRadius.large),
        color = color,
        tonalElevation = if (isPressed) 0.dp else 4.dp
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            // Dekorativer Kreis im Hintergrund
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .background(
                        Color.White.copy(alpha = 0.1f),
                        CircleShape
                    )
            )
            
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = label,
                    fontSize = 48.sp,
                    fontWeight = FontWeight.Black,
                    color = Color.White
                )
                Text(
                    text = sublabel,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.White.copy(alpha = 0.7f),
                    letterSpacing = 1.5.sp
                )
            }
        }
    }
}

/**
 * Utility Button Component (Space, Delete)
 * 
 * Kleinere Buttons für sekundäre Aktionen
 */
@Composable
private fun UtilityButton(
    modifier: Modifier,
    icon: androidx.compose.ui.graphics.vector.ImageVector?,
    text: String?,
    color: Color,
    textColor: Color,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = modifier.fillMaxHeight(),
        shape = RoundedCornerShape(AppDimensions.CornerRadius.medium),
        colors = ButtonDefaults.buttonColors(
            containerColor = color,
            contentColor = textColor
        ),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp),
        contentPadding = PaddingValues(horizontal = 12.dp)
    ) {
        if (icon != null) {
            Icon(
                icon,
                contentDescription = text,
                modifier = Modifier.size(20.dp)
            )
            if (text != null) {
                Spacer(Modifier.width(6.dp))
            }
        }
        if (text != null) {
            Text(
                text,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
