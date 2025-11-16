package com.mouse.mouse.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.mouse.mouse.core.domain.model.OutputChannel
import com.mouse.mouse.core.domain.model.PlaybackState
import com.mouse.mouse.ui.theme.*

@Composable
fun PlaybackPanel(
    playbackState: PlaybackState,
    enabledChannels: Set<OutputChannel>,
    onPlayClick: () -> Unit,
    onChannelToggle: (OutputChannel) -> Unit,
    isPlayEnabled: Boolean,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        tonalElevation = Elevation.bottomPanel,
        shadowElevation = Elevation.bottomPanel
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Layout.screenPaddingHorizontal)
                .padding(vertical = Spacing.lg)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(Spacing.md),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Play/Stop Button
                Button(
                    onClick = onPlayClick,
                    modifier = Modifier.weight(1f).height(Size.Control.md),
                    enabled = isPlayEnabled,
                    shape = RoundedCornerShape(Radius.pill)
                ) {
                    Text(
                        text = if (playbackState == PlaybackState.PLAYING) {
                            "◼ STOP"
                        } else {
                            "▶ PLAY"
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(Spacing.md))

            // Channel Chips
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(Layout.chipSpacing),
                verticalAlignment = Alignment.CenterVertically
            ) {
                ChannelChip(
                    channel = OutputChannel.FLASH,
                    label = "Flash",
                    isEnabled = enabledChannels.contains(OutputChannel.FLASH),
                    onToggle = { onChannelToggle(OutputChannel.FLASH) }
                )

                ChannelChip(
                    channel = OutputChannel.VIBRATION,
                    label = "Vibration",
                    isEnabled = enabledChannels.contains(OutputChannel.VIBRATION),
                    onToggle = { onChannelToggle(OutputChannel.VIBRATION) }
                )

                ChannelChip(
                    channel = OutputChannel.SOUND,
                    label = "Sound",
                    isEnabled = enabledChannels.contains(OutputChannel.SOUND),
                    onToggle = { onChannelToggle(OutputChannel.SOUND) }
                )

                ChannelChip(
                    channel = OutputChannel.VISUAL,
                    label = "Visuell",
                    isEnabled = enabledChannels.contains(OutputChannel.VISUAL),
                    onToggle = { onChannelToggle(OutputChannel.VISUAL) }
                )
            }
        }
    }
}

@Composable
fun ChannelChip(
    channel: OutputChannel,
    label: String,
    isEnabled: Boolean,
    onToggle: () -> Unit,
    modifier: Modifier = Modifier
) {
    val backgroundColor by animateColorAsState(
        targetValue = if (isEnabled) {
            when (channel) {
                OutputChannel.FLASH -> ChannelFlash
                OutputChannel.VIBRATION -> ChannelVibration
                OutputChannel.SOUND -> ChannelSound
                OutputChannel.VISUAL -> ChannelVisual
            }
        } else {
            ChannelInactiveBackground
        }
    )

    val contentColor by animateColorAsState(
        targetValue = if (isEnabled) {
            ChannelActiveContent
        } else {
            ChannelInactiveContent
        }
    )

    FilterChip(
        selected = isEnabled,
        onClick = onToggle,
        label = {
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium,
                color = contentColor
            )
        },
        modifier = modifier,
        shape = RoundedCornerShape(Radius.pill),
        colors = FilterChipDefaults.filterChipColors(
            containerColor = backgroundColor,
            selectedContainerColor = backgroundColor,
            labelColor = contentColor,
            selectedLabelColor = contentColor
        )
    )
}
