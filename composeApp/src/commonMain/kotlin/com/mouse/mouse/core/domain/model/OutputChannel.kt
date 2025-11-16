package com.mouse.mouse.core.domain.model

enum class OutputChannel {
    FLASH,
    VIBRATION,
    SOUND,
    VISUAL
}

data class ChannelState(
    val channel: OutputChannel,
    val isEnabled: Boolean = false
)
