package com.mouse.mouse.presentation.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.mouse.mouse.ui.theme.AppDimensions
import kotlinx.coroutines.delay

/**
 * Camera Scanner Mock UI Component
 * 
 * Simuliert einen OCR/Text-Scanner Screen:
 * - Zeigt animierte Scan-Linie
 * - Nach kurzer Zeit: Simuliertes Scan-Ergebnis
 * 
 * TODO: Später ersetzen durch echte CameraX + ML Kit Integration
 * 
 * @param onScanResult Callback mit dem gescannten Text
 * @param onClose Callback zum Schließen des Scanners
 */
@Composable
fun CameraScannerMockUI(
    onScanResult: (String) -> Unit,
    onClose: () -> Unit
) {
    // Animierte Scan-Linie (hoch-runter)
    val infiniteTransition = rememberInfiniteTransition(label = "scan")
    val scanLineY by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scanline"
    )

    // Simulierter Scan nach 2.5 Sekunden
    LaunchedEffect(Unit) {
        delay(2500)
        onScanResult("SOS HELP")
    }

    // Full-Screen Scanner UI
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        // Pseudo Camera Preview (später: CameraX PreviewView)
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.DarkGray)
        ) {
            Text(
                "CAMERA PREVIEW",
                color = Color.White.copy(0.2f),
                modifier = Modifier.align(Alignment.Center)
            )
        }

        // Overlay UI
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Top Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(AppDimensions.Spacing.medium)
                    .background(
                        Color.Black.copy(0.5f),
                        CircleShape
                    ),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onClose) {
                    Icon(
                        Icons.Rounded.Close,
                        contentDescription = "Close",
                        tint = Color.White
                    )
                }
                Text(
                    "SCANNING TEXT...",
                    color = Color.White,
                    style = MaterialTheme.typography.labelLarge
                )
                Spacer(Modifier.size(48.dp))  // Balance für Close Button
            }

            // Scanner Frame mit animierter Linie
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(AppDimensions.Spacing.xLarge)
            ) {
                // Frame Border
                Box(
                    Modifier
                        .matchParentSize()
                        .border(
                            AppDimensions.Border.medium,
                            Color.White.copy(0.5f),
                            RoundedCornerShape(AppDimensions.CornerRadius.small)
                        )
                )

                // Animierte Scan-Linie
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(2.dp)
                        .align(Alignment.TopCenter)
                        .offset(y = (400 * scanLineY).dp)
                        .background(
                            Brush.horizontalGradient(
                                colors = listOf(
                                    Color.Transparent,
                                    Color(0xFF00E5FF),
                                    Color.Transparent
                                )
                            )
                        )
                )
            }

            // Bottom Instructions
            Text(
                "Align text or morse code within frame",
                color = Color.White.copy(0.7f),
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(bottom = AppDimensions.Spacing.xLarge)
            )
        }
    }
}
