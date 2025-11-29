package com.mouse.mouse.presentation.screens

import androidx.compose.runtime.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.mouse.mouse.data.model.InputMode
import com.mouse.mouse.presentation.components.*
import com.mouse.mouse.presentation.viewmodel.MorseSuiteViewModel
import com.mouse.mouse.ui.theme.AppDimensions
import kotlinx.coroutines.launch

/**
 * Hauptscreen für Morse-Übersetzung und -Übertragung
 */
@Composable
fun TransmitterScreen(viewModel: MorseSuiteViewModel) {
    val scope = rememberCoroutineScope()
    var showCameraScanner by remember { mutableStateOf(false) }

    if (showCameraScanner) {
        CameraScannerMockUI(
            onScanResult = { resultText ->
                viewModel.onTextInputChange(resultText)
                showCameraScanner = false
            },
            onClose = { showCameraScanner = false }
        )
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    horizontal = AppDimensions.Padding.screenHorizontal,
                    vertical = AppDimensions.Padding.screenVertical
                ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            
            InputModeSelector(
                currentMode = viewModel.inputMode,
                onModeChanged = { viewModel.toggleInputMode() }
            )
            
            Spacer(modifier = Modifier.height(AppDimensions.Spacing.medium))

            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(AppDimensions.Spacing.medium),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                SignalVisualizer(isSignalActive = viewModel.isSignalActive)

                InputCard(
                    text = viewModel.displayTop,
                    label = if(viewModel.inputMode == InputMode.TEXT) "TEXT INPUT" else "MORSE TERMINAL",
                    isReadOnly = viewModel.inputMode == InputMode.MORSE,
                    onTextChange = { viewModel.onTextInputChange(it) },
                    onCameraClick = { showCameraScanner = true }
                )

                if (viewModel.displayBottom.isNotEmpty()) {
                    OutputCard(
                        text = viewModel.displayBottom,
                        label = if(viewModel.inputMode == InputMode.TEXT) "MORSE OUTPUT" else "TEXT TRANSLATION",
                        playbackIndex = viewModel.playbackIndex
                    )
                }
            }

            Spacer(modifier = Modifier.height(AppDimensions.Spacing.medium))

            if (viewModel.inputMode == InputMode.MORSE) {
                MorseKeyboard(
                    onDot = { viewModel.appendMorse(".") },
                    onDash = { viewModel.appendMorse("-") },
                    onSpace = { viewModel.appendSpace() },
                    onDelete = { viewModel.deleteLastMorse() }
                )
                
                Spacer(modifier = Modifier.height(AppDimensions.Spacing.small))
                Button(
                    onClick = { scope.launch { viewModel.transmitSignal() } },
                    modifier = Modifier.fillMaxWidth().height(AppDimensions.Height.buttonMedium),
                    shape = androidx.compose.foundation.shape.RoundedCornerShape(AppDimensions.CornerRadius.medium),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.tertiary)
                ) {
                    Icon(Icons.Default.PlayArrow, null)
                    Spacer(Modifier.width(AppDimensions.Spacing.xSmall))
                    Text("TRANSMIT SIGNAL")
                }

            } else {
                OutputSelector(
                    modes = viewModel.activeModes,
                    onToggle = { viewModel.toggleMode(it) }
                )
                Spacer(modifier = Modifier.height(AppDimensions.Spacing.small))
                PlayButton(
                    isPlaying = viewModel.isPlaying,
                    onPlay = { scope.launch { viewModel.transmitSignal() } },
                    onStop = viewModel::stopTransmission,
                    enabled = viewModel.displayBottom.isNotEmpty()
                )
            }
        }
    }
}
