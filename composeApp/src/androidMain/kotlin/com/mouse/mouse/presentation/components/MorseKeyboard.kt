package com.mouse.mouse.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.Backspace
import androidx.compose.material.icons.rounded.SpaceBar
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mouse.mouse.ui.theme.AppDimensions

/**
 * Morse Keyboard Component
 * 
 * Custom Keyboard für Morse-Code Eingabe:
 * - Große DOT (.) und DASH (-) Pads
 * - Space Button (trennt Buchstaben)
 * - Delete Button
 * 
 * @param onDot Callback wenn DOT gedrückt wird
 * @param onDash Callback wenn DASH gedrückt wird
 * @param onSpace Callback wenn SPACE gedrückt wird
 * @param onDelete Callback wenn DELETE gedrückt wird
 */
@Composable
fun MorseKeyboard(
    onDot: () -> Unit,
    onDash: () -> Unit,
    onSpace: () -> Unit,
    onDelete: () -> Unit
) {
    val haptic = LocalHapticFeedback.current

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(AppDimensions.Spacing.medium)
    ) {
        // ROW 1: SIGNAL PADS (DOT & DASH)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(AppDimensions.Height.telegraphPad),
            horizontalArrangement = Arrangement.spacedBy(AppDimensions.Spacing.medium)
        ) {
            // DOT PAD
            TelegraphPad(
                modifier = Modifier.weight(1f),
                color = MaterialTheme.colorScheme.primary,
                symbol = "●",
                subLabel = "SHORT",
                onClick = {
                    haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                    onDot()
                }
            )

            // DASH PAD
            TelegraphPad(
                modifier = Modifier.weight(1f),
                color = MaterialTheme.colorScheme.secondary,
                symbol = "▬",
                subLabel = "LONG",
                onClick = {
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    onDash()
                }
            )
        }

        // ROW 2: UTILITY BUTTONS
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(AppDimensions.Height.keyboardRow),
            horizontalArrangement = Arrangement.spacedBy(AppDimensions.Spacing.small)
        ) {
            // SPACE BUTTON
            Button(
                onClick = onSpace,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                shape = RoundedCornerShape(AppDimensions.CornerRadius.medium),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant,
                    contentColor = MaterialTheme.colorScheme.onSurfaceVariant
                ),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp)
            ) {
                Icon(Icons.Rounded.SpaceBar, contentDescription = null)
                Spacer(Modifier.width(AppDimensions.Spacing.xSmall))
                Text("SPACE", fontWeight = FontWeight.Bold)
            }

            // DELETE BUTTON
            Button(
                onClick = onDelete,
                modifier = Modifier
                    .width(80.dp)
                    .fillMaxHeight(),
                shape = RoundedCornerShape(AppDimensions.CornerRadius.medium),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                    contentColor = MaterialTheme.colorScheme.error
                ),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp)
            ) {
                Icon(
                    Icons.AutoMirrored.Rounded.Backspace,
                    contentDescription = "Delete"
                )
            }
        }
    }
}

/**
 * Telegraph Pad Component
 * 
 * Großes, touch-freundliches Pad für DOT oder DASH Eingabe
 * Mit Gradient-Hintergrund und dekorativem Kreis
 */
@Composable
fun RowScope.TelegraphPad(
    modifier: Modifier,
    color: Color,
    symbol: String,
    subLabel: String,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxHeight()
            .clip(RoundedCornerShape(AppDimensions.CornerRadius.xLarge))
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        color.copy(alpha = 0.8f),
                        color.copy(alpha = 0.4f)
                    )
                )
            )
            .clickable { onClick() }
            .border(
                AppDimensions.Border.thin,
                color.copy(alpha = 0.5f),
                RoundedCornerShape(AppDimensions.CornerRadius.xLarge)
            ),
        contentAlignment = Alignment.Center
    ) {
        // Dekorativer Hintergrund-Kreis
        Box(
            modifier = Modifier
                .size(100.dp)
                .background(color.copy(alpha = 0.2f), CircleShape)
        )

        // Content (Symbol + Label)
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = symbol,
                fontSize = 56.sp,
                color = Color.White,
                fontWeight = FontWeight.Black
            )
            Text(
                text = subLabel,
                style = MaterialTheme.typography.labelSmall,
                color = Color.White.copy(alpha = 0.6f),
                letterSpacing = AppDimensions.LetterSpacing.medium
            )
        }
    }
}
