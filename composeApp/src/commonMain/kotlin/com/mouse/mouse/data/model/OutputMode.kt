package com.mouse.mouse.data.model

/**
 * Definiert die verschiedenen Ausgabemethoden für Morse-Signale
 * 
 * VIBRATION: Vibrationsmotor des Geräts
 * LIGHT: Kamera-Blitz (Taschenlampe)
 * SOUND: Audio-Ausgabe (TODO: Noch nicht implementiert)
 */
enum class OutputMode {
    VIBRATION,
    LIGHT,
    SOUND
}
