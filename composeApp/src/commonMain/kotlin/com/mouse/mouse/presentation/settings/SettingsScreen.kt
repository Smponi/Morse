package com.mouse.mouse.presentation.settings

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mouse.mouse.ui.theme.Elevation
import com.mouse.mouse.ui.theme.Layout
import com.mouse.mouse.ui.theme.Radius
import com.mouse.mouse.ui.theme.Spacing

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onNavigateBack: () -> Unit
) {
    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Einstellungen") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Zurück")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = Layout.screenPaddingHorizontal)
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.spacedBy(Spacing.lg)
        ) {
            Spacer(modifier = Modifier.height(Spacing.sm))

            SettingsSection(title = "Ausgabekanäle") {
                var flashEnabled by remember { mutableStateOf(false) }
                var vibrationEnabled by remember { mutableStateOf(true) }
                var soundEnabled by remember { mutableStateOf(true) }
                var visualEnabled by remember { mutableStateOf(true) }

                SettingSwitch(
                    title = "Blitz erlauben",
                    checked = flashEnabled,
                    onCheckedChange = { flashEnabled = it }
                )

                SettingSwitch(
                    title = "Vibration erlauben",
                    checked = vibrationEnabled,
                    onCheckedChange = { vibrationEnabled = it }
                )

                SettingSwitch(
                    title = "Sound erlauben",
                    checked = soundEnabled,
                    onCheckedChange = { soundEnabled = it }
                )

                SettingSwitch(
                    title = "Visuelle Darstellung",
                    checked = visualEnabled,
                    onCheckedChange = { visualEnabled = it }
                )
            }

            SettingsSection(title = "Morse-Geschwindigkeit") {
                var speed by remember { mutableStateOf(0.5f) }

                Text(
                    text = "Geschwindigkeit",
                    style = MaterialTheme.typography.bodyMedium
                )

                Slider(
                    value = speed,
                    onValueChange = { speed = it },
                    valueRange = 0f..1f
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Langsam",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "Schnell",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            SettingsSection(title = "Sicherheit") {
                var flashReduced by remember { mutableStateOf(true) }

                SettingSwitch(
                    title = "Flacker-reduzierter Modus",
                    description = "Reduziert schnelle Blinkmuster für bessere Sicherheit",
                    checked = flashReduced,
                    onCheckedChange = { flashReduced = it }
                )
            }

            SettingsSection(title = "Datenschutz") {
                var historyEnabled by remember { mutableStateOf(true) }

                SettingSwitch(
                    title = "Verlauf speichern",
                    checked = historyEnabled,
                    onCheckedChange = { historyEnabled = it }
                )

                Spacer(modifier = Modifier.height(Spacing.sm))

                OutlinedButton(
                    onClick = { /* TODO: Clear history */ },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Verlauf löschen")
                }
            }

            Spacer(modifier = Modifier.height(Spacing.xl))
        }
    }
}

@Composable
fun SettingsSection(
    title: String,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(Radius.lg),
        elevation = CardDefaults.cardElevation(defaultElevation = Elevation.card)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Layout.cardPadding),
            verticalArrangement = Arrangement.spacedBy(Spacing.md)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium
            )

            content()
        }
    }
}

@Composable
fun SettingSwitch(
    title: String,
    description: String? = null,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium
            )
            if (description != null) {
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange
        )
    }
}
