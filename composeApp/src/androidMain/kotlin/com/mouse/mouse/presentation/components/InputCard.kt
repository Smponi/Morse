package com.mouse.mouse.presentation.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.PhotoCamera
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mouse.mouse.ui.theme.AppDimensions

/**
 * Input Card Component
 * 
 * Zeigt entweder:
 * - Ein editierbares Textfeld (TEXT Modus)
 * - Ein Read-Only Morse Terminal (MORSE Modus)
 * 
 * @param text Der anzuzeigende Text
 * @param label Label für die Card
 * @param isReadOnly true = Morse Terminal (nicht editierbar), false = Text Input
 * @param onTextChange Callback wenn Text geändert wird
 * @param onCameraClick Callback für Camera Scanner Button
 */
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun InputCard(
    text: String,
    label: String,
    isReadOnly: Boolean,
    onTextChange: (String) -> Unit,
    onCameraClick: () -> Unit
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    Card(
        colors = CardDefaults.cardColors(
            // Morse Terminal: Dunkler Hintergrund für besseren Kontrast
            containerColor = if (isReadOnly) {
                Color(0xFF0D1117)
            } else {
                MaterialTheme.colorScheme.surfaceVariant
            }
        ),
        shape = RoundedCornerShape(AppDimensions.CornerRadius.large),
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(
                min = if (isReadOnly) {
                    AppDimensions.Height.inputCardMinMorse
                } else {
                    AppDimensions.Height.inputCardMinText
                }
            )
            .border(
                width = if (isReadOnly) AppDimensions.Border.thin else AppDimensions.Border.thin.times(0),
                color = if (isReadOnly) {
                    MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)
                } else {
                    Color.Transparent
                },
                shape = RoundedCornerShape(AppDimensions.CornerRadius.large)
            )
    ) {
        Column(modifier = Modifier.padding(AppDimensions.Padding.cardMedium)) {
            // Header Row (Label + Camera Button)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = label,
                    style = MaterialTheme.typography.labelMedium,
                    color = if (isReadOnly) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        Color.Gray
                    }
                )

                IconButton(onClick = onCameraClick) {
                    Icon(
                        Icons.Rounded.PhotoCamera,
                        contentDescription = "Scan",
                        tint = if (isReadOnly) {
                            MaterialTheme.colorScheme.primary
                        } else {
                            MaterialTheme.colorScheme.onSurfaceVariant
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(AppDimensions.Spacing.xSmall))

            // Content Area
            if (isReadOnly) {
                // Morse Terminal Display (Read-Only)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 100.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    if (text.isEmpty()) {
                        Text(
                            "TAP PADS TO INPUT",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.2f),
                            fontFamily = FontFamily.Monospace
                        )
                    } else {
                        Text(
                            text = text,
                            style = MaterialTheme.typography.headlineLarge.copy(
                                fontFamily = FontFamily.Monospace,
                                fontWeight = FontWeight.Bold,
                                lineHeight = 40.sp,
                                letterSpacing = AppDimensions.LetterSpacing.wide
                            ),
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            } else {
                // Text Input Field (Editable)
                TextField(
                    value = text,
                    onValueChange = onTextChange,
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        cursorColor = MaterialTheme.colorScheme.primary
                    ),
                    textStyle = MaterialTheme.typography.headlineSmall,
                    placeholder = { Text("Type here...", color = Color.Gray.copy(0.5f)) },
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(onDone = {
                        keyboardController?.hide()
                        focusManager.clearFocus()
                    })
                )
            }
        }
    }
}
