package com.mouse.mouse.presentation.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ContentCopy
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.text.AnnotatedString
import kotlinx.coroutines.launch
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import com.mouse.mouse.ui.theme.AppDimensions

/**
 * Output Card Component mit animiertem Playback Progress
 * 
 * Zeigt die Übersetzung an mit animiertem Progress während Wiedergabe
 */
@Composable
fun OutputCard(
    text: String,
    label: String = "OUTPUT",
    playbackIndex: Int = -1
) {
    val clipboardManager = LocalClipboardManager.current
    val scope = rememberCoroutineScope()
    
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        shape = RoundedCornerShape(AppDimensions.CornerRadius.large),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(AppDimensions.Padding.cardMedium)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    label,
                    style = MaterialTheme.typography.labelMedium,
                    color = Color.Gray
                )
                
                if (text.isNotEmpty()) {
                    IconButton(
                        onClick = {
                            scope.launch {
                                clipboardManager.setText(AnnotatedString(text))
                            }
                        }
                    ) {
                        Icon(
                            Icons.Rounded.ContentCopy,
                            contentDescription = "Copy",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(AppDimensions.IconSize.small)
                        )
                    }
                }
            }
            
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
                    AnimatedMorseText(
                        text = text,
                        playbackIndex = playbackIndex
                    )
                }
            }
        }
    }
}

@Composable
private fun AnimatedMorseText(
    text: String,
    playbackIndex: Int
) {
    val isPlaying = playbackIndex >= 0
    
    val annotatedText = buildAnnotatedString {
        text.forEachIndexed { index, char ->
            val alpha = when {
                !isPlaying -> 1f
                index < playbackIndex -> 0.3f
                index == playbackIndex -> 1f
                else -> 1f
            }
            
            val animatedAlpha by animateFloatAsState(
                targetValue = alpha,
                animationSpec = tween(durationMillis = 150),
                label = "char_alpha_$index"
            )
            
            withStyle(
                style = SpanStyle(
                    color = MaterialTheme.colorScheme.secondary.copy(alpha = animatedAlpha),
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = AppDimensions.LetterSpacing.medium
                )
            ) {
                append(char)
            }
        }
    }
    
    Text(
        text = annotatedText,
        style = MaterialTheme.typography.headlineSmall
    )
}
