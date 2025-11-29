package com.mouse.mouse.domain

import com.mouse.mouse.data.model.OutputMode

/**
 * Platform-agnostic Morse Transmitter Interface
 * 
 * expect/actual Pattern für platform-spezifische Hardware-Zugriffe
 */
expect class MorseTransmitter() {
    var onSignalStateChanged: ((Boolean) -> Unit)?
    var onPlaybackProgress: ((currentIndex: Int) -> Unit)?
    
    suspend fun transmit(morseCode: String, activeModes: List<OutputMode>): Boolean
    fun stop()
    fun isTransmitting(): Boolean
}
