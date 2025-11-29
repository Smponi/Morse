package com.mouse.mouse.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import com.mouse.mouse.ui.theme.AppDimensions

/**
 * Output Card Component
 * 
 * Zeigt die Übersetzung an:
 * - TEXT Modus: Morse-Code Ausgabe
 * - MORSE Modus: Text Übersetzung
 * 
 * @param text Der anzuzeigende Text
 * @param label Label für die Card
 */
@Composable
fun OutputCard(text: String, label: String = "OUTPUT") {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        shape = RoundedCornerShape(AppDimensions.CornerRadius.large),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(AppDimensions.Padding.cardMedium)) {
            Text(
                label,
                style = MaterialTheme.typography.labelMedium,
                color = Color.Gray
            )
            
            Spacer(modifier = Modifier.height(AppDimensions.Spacing.xSmall))
            
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = AppDimensions.Height.outputCardMin),
                contentAlignment = Alignment.CenterStart
            ) {
                if (text.isEmpty()) {
                    Text(
                        "Waiting for input",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray.copy(alpha = 0.5f)
                    )
                } else {
                    Text(
                        text = text,
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = AppDimensions.LetterSpacing.medium
                        ),
                        color = MaterialTheme.colorScheme.secondary
                    )
                }
            }
        }
    }
}
