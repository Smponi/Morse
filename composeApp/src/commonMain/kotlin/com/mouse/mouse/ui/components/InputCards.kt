package com.mouse.mouse.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import com.mouse.mouse.ui.theme.*
import kotlinx.coroutines.delay
import kotlin.time.Clock
import kotlin.time.TimeSource

@Composable
fun TextInputCard(
    text: String,
    onTextChange: (String) -> Unit,
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
            Text(
                text = "Gib deinen Text ein",
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(Spacing.sm))

            OutlinedTextField(
                value = text,
                onValueChange = onTextChange,
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Dein Text...") },
                minLines = 2,
                maxLines = 4,
                shape = RoundedCornerShape(Radius.sm)
            )

            if (text.isNotEmpty()) {
                Spacer(modifier = Modifier.height(Spacing.xs))
                Text(
                    text = "${text.length} Zeichen",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
fun MorseInputCard(
    onMorseTap: (Long) -> Unit,
    onDeleteLast: () -> Unit,
    onClear: () -> Unit,
    currentMorseSequence: String = "",
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
                .padding(Spacing.lg),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Tippe Morse (kurz = ·, lang = −)",
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(Spacing.md))

            MorseTapButton(onTap = onMorseTap)

            Spacer(modifier = Modifier.height(Spacing.md))

            if (currentMorseSequence.isNotEmpty()) {
                Text(
                    text = currentMorseSequence,
                    style = MorseOutputInline,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(Spacing.sm))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(Spacing.sm)
                ) {
                    OutlinedButton(onClick = onDeleteLast) {
                        Text("⌫ Löschen")
                    }

                    OutlinedButton(onClick = onClear) {
                        Text("Alles löschen")
                    }
                }
            } else {
                Text(
                    text = "Drücke und halte für Eingabe",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
fun MorseTapButton(
    onTap: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    var pressStartTime by remember { mutableStateOf(0L) }
    var isPressed by remember { mutableStateOf(false) }

    Box(
        modifier = modifier
            .size(Size.Control.lg)
            .clip(CircleShape)
            .background(
                color = if (isPressed) {
                    MaterialTheme.colorScheme.primary.copy(alpha = 0.8f)
                } else {
                    MaterialTheme.colorScheme.primary
                }
            )
            .pointerInput(Unit) {
                detectTapGestures(
                    onPress = {
                        pressStartTime = TimeSource.Monotonic.markNow().elapsedNow().inWholeMilliseconds
                        isPressed = true
                        tryAwaitRelease()
                        isPressed = false
                        val duration = TimeSource.Monotonic.markNow().elapsedNow().inWholeMilliseconds - pressStartTime
                        onTap(duration)
                    }
                )
            },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = if (isPressed) "−" else "·",
            style = MaterialTheme.typography.displayLarge,
            color = MaterialTheme.colorScheme.onPrimary
        )
    }
}
